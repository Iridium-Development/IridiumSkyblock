package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

import java.util.Optional;

public class PortalCreateListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPortalCreate(PortalCreateEvent event) {
        // Only check portal creation in skyblock worlds
        if (!IridiumSkyblock.getInstance().getIslandManager().isInSkyblockWorld(event.getWorld())) {
            return;
        }

        // Check if the portal is being created on an island
        if (event.getBlocks().isEmpty()) {
            return;
        }

        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager()
                .getTeamViaLocation(event.getBlocks().get(0).getLocation());

        // Allow portal creation only within an island
        if (!island.isPresent()) {
            event.setCancelled(true);
        }
    }
}