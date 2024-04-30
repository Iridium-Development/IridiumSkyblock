package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

@AllArgsConstructor
public class PlayerRespawnEventListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {

        if(!IridiumSkyblock.getInstance().getConfiguration().spawnOnIsland) { return; }
        if(!event.isBedSpawn() && !event.isAnchorSpawn()) event.setRespawnLocation(PlayerUtils.getSpawn(event.getPlayer()));
    }
}
