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
            // Player is not on an island, cancel the portal
            event.setCancelled(true);
            return;
        }

        Island island = islandOptional.get();
        World targetWorld = null;
        Location destination = null;

        // Determine which world to teleport to based on the portal type
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            World fromWorld = from.getWorld();

            if (fromWorld.getEnvironment() == World.Environment.NORMAL) {
                // Going from overworld to nether
                targetWorld = IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.NETHER);
                if (targetWorld != null) {
                    // Calculate the corresponding nether location
                    destination = calculateNetherLocation(from, island, targetWorld);
                }
            } else if (fromWorld.getEnvironment() == World.Environment.NETHER) {
                // Going from nether to overworld
                targetWorld = IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.NORMAL);
                if (targetWorld != null) {
                    // Calculate the corresponding overworld location
                    destination = calculateOverworldLocation(from, island, targetWorld);
                }
            }
        } else if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            World fromWorld = from.getWorld();

            if (fromWorld.getEnvironment() == World.Environment.THE_END) {
                // Going from end to overworld - teleport to island home
                targetWorld = IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.NORMAL);
                if (targetWorld != null) {
                    destination = island.getHome();
                }
            } else {
                // Going to the end
                targetWorld = IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.THE_END);
                if (targetWorld != null) {
                    // Teleport to the island's end location
                    destination = island.getCenter(targetWorld).clone();
                    destination.setY(64); // Safe Y level in the end
                }
            }
        }

        // If we have a valid destination, set it and cancel the default behavior
        if (destination != null && targetWorld != null) {
            event.setCancelled(true);

            final Location finalDestination = destination;
            final World finalTargetWorld = targetWorld;

            // Teleport the player after a short delay to ensure smooth transition
            Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> {
                if (player.isOnline()) {
                    player.teleport(finalDestination);

                    // Update the user's current island cache for the new location
                    User updatedUser = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                    updatedUser.setCurrentIsland(Optional.of(island));

                    // Update border after portal teleport with a longer delay
                    Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> {
                        if (player.isOnline()) {
                            IridiumSkyblock.getInstance().getIslandManager().sendIslandBorder(player);
                        }
                    }, 5L); // Increased delay to 5 ticks
                }
            }, 1L);
        } else {
            // Target world is disabled or not available
            event.setCancelled(true);
            player.sendMessage(com.iridium.iridiumcore.utils.StringUtils.color(
                    "This world is disabled!")
            );
        }
    }

    /**
     * Calculate the nether location based on the overworld location
     * Nether coordinates are 1/8 of overworld coordinates
     */
    private Location calculateNetherLocation(Location overworldLocation, Island island, World netherWorld) {
        // Get the island center in both worlds
        Location overworldCenter = island.getCenter(overworldLocation.getWorld());
        Location netherCenter = island.getCenter(netherWorld);

        // Calculate offset from island center in overworld
        double offsetX = overworldLocation.getX() - overworldCenter.getX();
        double offsetZ = overworldLocation.getZ() - overworldCenter.getZ();

        // Apply 1/8 scale for nether
        double netherX = netherCenter.getX() + (offsetX / 8.0);
        double netherZ = netherCenter.getZ() + (offsetZ / 8.0);

        // Find safe Y level
        Location destination = new Location(netherWorld, netherX, 64, netherZ);
        destination = findSafeNetherLocation(destination, island);

        return destination;
    }

    /**
     * Calculate the overworld location based on the nether location
     * Overworld coordinates are 8x nether coordinates
     */
    private Location calculateOverworldLocation(Location netherLocation, Island island, World overworldWorld) {
        // Get the island center in both worlds
        Location netherCenter = island.getCenter(netherLocation.getWorld());
        Location overworldCenter = island.getCenter(overworldWorld);

        // Calculate offset from island center in nether
        double offsetX = netherLocation.getX() - netherCenter.getX();
        double offsetZ = netherLocation.getZ() - netherCenter.getZ();

        // Apply 8x scale for overworld
        double overworldX = overworldCenter.getX() + (offsetX * 8.0);
        double overworldZ = overworldCenter.getZ() + (offsetZ * 8.0);

        // Find safe Y level
        Location destination = new Location(overworldWorld, overworldX, 64, overworldZ);
        destination = findSafeOverworldLocation(destination, island);

        return destination;
    }

    /**
     * Find a safe location in the nether (not in lava, not suffocating)
     */
    private Location findSafeNetherLocation(Location location, Island island) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int z = location.getBlockZ();

        // Search for safe Y level between 32 and 120
        for (int y = 64; y <= 120; y++) {
            Location checkLoc = new Location(world, x, y, z);
            if (island.isInIsland(checkLoc) && isSafeLocation(checkLoc)) {
                return checkLoc.add(0.5, 0, 0.5); // Center of block
            }
        }

        // Fallback to island center if no safe location found
        Location center = island.getCenter(world).clone();
        center.setY(64);
        return center;
    }

    /**
     * Find a safe location in the overworld
     */
    private Location findSafeOverworldLocation(Location location, Island island) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int z = location.getBlockZ();

        // Find the highest solid block
        for (int y = world.getMaxHeight() - 1; y > world.getMinHeight(); y--) {
            Location checkLoc = new Location(world, x, y, z);
            if (island.isInIsland(checkLoc) &&
                    world.getBlockAt(x, y, z).getType().isSolid() &&
                    !world.getBlockAt(x, y + 1, z).getType().isSolid() &&
                    !world.getBlockAt(x, y + 2, z).getType().isSolid()) {
                return new Location(world, x + 0.5, y + 1, z + 0.5);
            }
        }

        // Fallback to island home
        return island.getHome();
    }

    /**
     * Check if a location is safe (2 blocks of air above solid ground)
     */
    private boolean isSafeLocation(Location location) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        // Check if standing block is solid
        if (!world.getBlockAt(x, y - 1, z).getType().isSolid()) {
            return false;
        }

        // Check if there's air for player to stand
        if (world.getBlockAt(x, y, z).getType().isSolid() ||
                world.getBlockAt(x, y + 1, z).getType().isSolid()) {
            return false;
        }

        // Check if not lava
        return !world.getBlockAt(x, y - 1, z).isLiquid() &&
                !world.getBlockAt(x, y, z).isLiquid() &&
                !world.getBlockAt(x, y + 1, z).isLiquid();
    }
}