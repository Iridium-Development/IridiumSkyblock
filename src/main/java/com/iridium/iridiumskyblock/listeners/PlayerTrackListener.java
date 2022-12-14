package com.iridium.iridiumskyblock.listeners;

import com.google.common.base.Objects;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.antlr.v4.parse.ANTLRParser.labeledAlt_return;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class PlayerTrackListener implements Listener {

    BukkitTask timer;
    Map<Player, Island> island = new HashMap<>();

    private boolean equalsIsland(Island a, Island b) {
        if (a != null)
            return a.equals(b);
        if (b != null)
            return b != a;
        return true;
    }

    public PlayerTrackListener(IridiumSkyblock iridiumSkyblock) {
        if (IridiumSkyblock.getInstance().getConfiguration().trackTicks > 0) {
            timer = new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Optional<Island> standingIsland = IridiumSkyblock.getInstance().getIslandManager()
                                .getIslandViaPlayerLocation(player);

                        standingIsland.ifPresent(island -> PlayerUtils.sendBorder(player, island));

                        if (!equalsIsland(island.get(player), standingIsland.orElse(null))) {
                            if (island.get(player) != null &&
                                    standingIsland.isPresent()) {
                                if (IridiumSkyblock.getInstance().getConfiguration().putBackOnIslandLeave) {
                                    IridiumSkyblock.getInstance().getIslandManager().teleportHome(player,
                                            island.get(player),
                                            0);
                                    return;
                                }
                            } else if (standingIsland.isPresent()) {
                                if (!IridiumSkyblock.getInstance().getIslandManager().enterIsland(player,
                                        standingIsland.get())) {
                                    PlayerUtils.teleportSpawn(player);
                                }
                            } else if (island.get(player) != null && !standingIsland.isPresent()) {
                                if (IridiumSkyblockAPI.getInstance().isIslandWorld(player.getWorld())) {
                                    if (IridiumSkyblock.getInstance().getConfiguration().putBackOnIslandLeave) {
                                        IridiumSkyblock.getInstance().getIslandManager().teleportHome(player,
                                                island.get(player),
                                                0);
                                        return;
                                    }
                                }
                            }
                            island.put(player, standingIsland.orElse(null));
                        }
                    }
                }
            }.runTaskTimer(iridiumSkyblock, IridiumSkyblock.getInstance().getConfiguration().trackTicks,
                    IridiumSkyblock.getInstance().getConfiguration().trackTicks);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        island.put(player,
                IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player).orElse(null));
    }

    @EventHandler
    public void onJoin(PlayerQuitEvent event) {
        island.remove(event.getPlayer());
    }

    public void disable() {
        if (timer != null)
            timer.cancel();
    }

    public void track(@NotNull Player player, @NotNull Island island2) {
        island.put(player, island2);
    }
}
