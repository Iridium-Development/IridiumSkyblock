package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

import java.util.Objects;
import java.util.Optional;

public class EntityPortalListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityPortal(EntityPortalEvent event) {

        Entity entity = event.getEntity();

        Optional<Island> islandCheck = IridiumSkyblock.getInstance().getTeamManager().getTeamViaLocation(entity.getLocation());

        // We want to allow teleportation from a non-skyblock world into a skyblock world, or from no island to an island.
        // We don't care if the entity is not within an island.
        if(!islandCheck.isPresent()) {
            return;
        }

        World worldFrom = event.getFrom().getWorld();
        World worldTo = event.getTo().getWorld();

        World overworld = IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.NORMAL);
        World nether = IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.NETHER);
        World end = IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.THE_END);

        // We don't care if the entity travels within the same world or dimension.
        if((worldTo != null) && (worldFrom == worldTo || worldFrom.getEnvironment() == worldTo.getEnvironment())) {
            return;
        }

        Island island = islandCheck.get();
        int level = island.getLevel();
        Location entityLocation = entity.getLocation();
        World destination;

        // ==================
        // |  NETHER CHECK  |
        // ==================

        if (entityLocation.getBlock().getType() != Material.END_PORTAL) {
            // We don't teleport if the Nether is disabled.
            if(worldTo == null && nether == null) {
                event.setCancelled(true);
                return;
            }

            // We only teleport if the island's level allows it.
            if(level < IridiumSkyblock.getInstance().getConfiguration().netherUnlockLevel) {
                event.setCancelled(true);
                return;
            }

            // Set teleport location
            destination = Objects.equals(worldFrom, nether) ? overworld : nether;
        }

        // ===============
        // |  END CHECK  |
        // ===============

        else {
            // We don't teleport if the End is disabled.
            if(worldTo == null && end == null) {
                event.setCancelled(true);
                return;
            }

            // We only teleport if the island's level allows it.
            if(level < IridiumSkyblock.getInstance().getConfiguration().endUnlockLevel) {
                event.setCancelled(true);
                return;
            }

            // Set teleport location
            destination = Objects.equals(worldFrom, end) ? overworld : end;
        }

        event.setTo(island.getCenter(destination));

        // Finalize the teleport
        Location location = LocationUtils.getSafeLocation(island.getCenter(destination), island);

        if (location == null) {
            event.setCancelled(true);
            return;
        }

        location.setY(location.getY() + 1);
        // version example: 1.20.4-R0.1-SNAPSHOT (we need 20)
        // will need to be updated for 26.x
        if (Integer.parseInt(Bukkit.getBukkitVersion().substring(2, 4)) >= 15) event.setCanCreatePortal(false);
        event.setTo(location);
    }
}
