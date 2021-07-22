package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import java.util.Optional;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRespawn(PlayerRespawnEvent event) {

        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);

        if (IridiumSkyblock.getInstance().getConfiguration().respawnOnIsland) {
            user.getIsland().ifPresent(island -> {
                if (!island.isInIsland(event.getRespawnLocation())) {
                    event.setRespawnLocation(LocationUtils.getSafeLocation(island.getHome(), island));
                }
            });
        } else if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getRespawnLocation().getWorld())) {
                if (user.isFlying()) {
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void monitorRespawn(PlayerRespawnEvent event) {
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getRespawnLocation());
        island.ifPresent(targetIsland -> PlayerUtils.sendBorder(event.getPlayer(), targetIsland));
    }

}
