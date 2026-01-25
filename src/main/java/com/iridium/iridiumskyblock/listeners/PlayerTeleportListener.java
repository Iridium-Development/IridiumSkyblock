package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null) return;

        boolean fromSkyblock = from.getWorld() != null &&
                IridiumSkyblock.getInstance().getIslandManager().isInSkyblockWorld(from.getWorld());
        boolean toSkyblock = IridiumSkyblock.getInstance().getIslandManager().isInSkyblockWorld(to.getWorld());

        // If teleporting to a skyblock world or changing dimensions within skyblock
        boolean dimensionChange = from.getWorld() != null && !from.getWorld().equals(to.getWorld());

        if (toSkyblock && (dimensionChange || !fromSkyblock)) {
            // Update border after teleport completes with appropriate delay
            int delay = dimensionChange ? 5 : 3; // Longer delay for dimension changes

            Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> {
                if (player.isOnline()) {
                    IridiumSkyblock.getInstance().getIslandManager().sendIslandBorder(player);
                }
            }, delay);
        }
    }
}