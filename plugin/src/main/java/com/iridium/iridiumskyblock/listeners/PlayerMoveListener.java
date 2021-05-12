package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        if (event.getTo().getBlockX() != event.getFrom().getBlockX() || event.getTo().getBlockZ() != event.getFrom().getBlockZ()) {
            if (user.getTeleportingTask() != null) {
                user.getTeleportingTask().cancel();
                user.setTeleportingTask(null);
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportCanceled
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
                );
            }
            if (user.isFlying()) {
                Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(player.getLocation());
                if (island.isPresent()) {
                    IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island.get(), "flight");
                    if (!islandBooster.isActive() && !player.hasPermission("iridiumskyblock.fly")) {
                        user.setFlying(false);
                        if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
                            player.setFlying(false);
                            player.setAllowFlight(false);
                            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().flightDisabled
                                    .replace("%player%", player.getName())
                                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
                            );
                        }
                    }
                }
            }
        }
    }

}
