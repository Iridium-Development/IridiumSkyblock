package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Optional;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (IridiumSkyblock.getInstance().getConfiguration().respawnOnIsland) {
            User user = IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer());
            user.getIsland().ifPresent(island -> {
                if (!island.isInIsland(event.getRespawnLocation())) {
                    event.setRespawnLocation(LocationUtils.getSafeLocation(island.getHome(), island));
                    island.getPlayersOnIsland().add(event.getPlayer());
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void monitorRespawn(PlayerRespawnEvent event) {
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getRespawnLocation());
        island.ifPresent(targetIsland -> PlayerUtils.sendBorder(event.getPlayer(), targetIsland));
    }

}
