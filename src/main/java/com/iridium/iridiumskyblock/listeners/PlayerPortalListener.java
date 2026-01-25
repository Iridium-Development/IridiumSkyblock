package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Optional;

public class PlayerPortalListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();

        // Only handle portals in skyblock worlds
        if (!IridiumSkyblock.getInstance().getIslandManager().isInSkyblockWorld(from.getWorld())) {
            return;
        }

        // Get the player's current island
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> islandOptional = user.getCurrentIsland(from);

        if (!islandOptional.isPresent()) {
            event.setCancelled(true);
            return;
        }

        Island island = islandOptional.get();
        World targetWorld = null;
        Location destination = null;

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            World fromWorld = from.getWorld();
            String normalWorldName = IridiumSkyblock.getInstance().getConfiguration().worldName;
            String netherWorldName = IridiumSkyblock.getInstance().getConfiguration().worldName + "_nether";

            // Detect which world we're in by name since both are NORMAL environment
            if (fromWorld.getName().equals(normalWorldName)) {
                // Going from overworld to "nether" (second overworld)
                targetWorld = IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.NETHER);
                if (targetWorld != null) {
                    destination = calculateDestination(from, island, targetWorld);
                }
            } else if (fromWorld.getName().equals(netherWorldName)) {
                // Going from "nether" back to overworld
                targetWorld = IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.NORMAL);
                if (targetWorld != null) {
                    destination = calculateDestination(from, island, targetWorld);
                }
            }
        } else if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            World fromWorld = from.getWorld();

            if (fromWorld.getEnvironment() == World.Environment.THE_END) {
                // Going from end to overworld
                targetWorld = IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.NORMAL);
                if (targetWorld != null) {
                    destination = island.getHome();
                }
            } else {
                // Going to the end
                targetWorld = IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.THE_END);
                if (targetWorld != null) {
                    destination = island.getCenter(targetWorld).clone();
                    destination.setY(64);
                }
            }
        }

        if (destination != null && targetWorld != null) {
            event.setCancelled(true);

            final Location finalDestination = destination;

            Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> {
                if (player.isOnline()) {
                    player.teleport(finalDestination);

                    User updatedUser = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                    updatedUser.setCurrentIsland(Optional.of(island));

                    Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> {
                        if (player.isOnline()) {
                            IridiumSkyblock.getInstance().getIslandManager().sendIslandBorder(player);
                        }
                    }, 5L);
                }
            }, 1L);
        } else {
            event.setCancelled(true);
            player.sendMessage(com.iridium.iridiumcore.utils.StringUtils.color("This world is disabled!"));
        }
    }

    /**
     * Calculate destination with 1:1 coordinate mapping
     * Works for all overworld-type dimensions
     */
    private Location calculateDestination(Location fromLocation, Island island, World targetWorld) {
        Location fromCenter = island.getCenter(fromLocation.getWorld());
        Location targetCenter = island.getCenter(targetWorld);

        double offsetX = fromLocation.getX() - fromCenter.getX();
        double offsetZ = fromLocation.getZ() - fromCenter.getZ();

        double targetX = targetCenter.getX() + offsetX;
        double targetZ = targetCenter.getZ() + offsetZ;

        Location destination = new Location(targetWorld, targetX, 64, targetZ);
        return findSafeLocation(destination, island);
    }

    /**
     * Find a safe location in any overworld-type dimension
     */
    private Location findSafeLocation(Location location, Island island) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int z = location.getBlockZ();

        for (int y = world.getMaxHeight() - 1; y > world.getMinHeight(); y--) {
            Location checkLoc = new Location(world, x, y, z);
            if (island.isInIsland(checkLoc) &&
                    world.getBlockAt(x, y, z).getType().isSolid() &&
                    !world.getBlockAt(x, y + 1, z).getType().isSolid() &&
                    !world.getBlockAt(x, y + 2, z).getType().isSolid()) {
                return new Location(world, x + 0.5, y + 1, z + 0.5);
            }
        }

        return island.getHome();
    }
}