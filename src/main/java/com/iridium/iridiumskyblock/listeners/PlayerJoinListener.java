package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Send border after player is fully loaded
        Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> {
            if (player.isOnline()) {
                IridiumSkyblock.getInstance().getIslandManager().sendIslandBorder(player);
            }
        }, 5L);
    }
}