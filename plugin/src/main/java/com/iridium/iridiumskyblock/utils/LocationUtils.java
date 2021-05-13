package com.iridium.iridiumskyblock.utils;

import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * Various utils which perform operations on {@link org.bukkit.Location}'s.
 */
public class LocationUtils {

    /**
     * Is a location safe to teleport a player to
     *
     * @param location The specified Location
     */
    public static boolean isSafe(@NotNull Location location) {
        Block block = location.getBlock();
        Block above = location.clone().add(0, 1, 0).getBlock();
        Block below = location.clone().subtract(0, 1, 0).getBlock();
        return block.isPassable() && !block.isLiquid() && above.isPassable() && !above.isLiquid() && !below.isPassable() && !below.isLiquid();
    }

    /**
     * Gets a safe location on the island
     *
     * @param location The location we want to teleport
     * @param island   The island we are inside
     * @return A safe Location, if none found return original location
     */
    public static Location getSafeLocation(@NotNull Location location, Island island) {
        World world = location.getWorld();
        if (world == null) return location;
        if (island == null) return location;
        if (isSafe(location)) return location;

        Location highest = getHighestLocation(location.getBlockX(), location.getBlockZ(), world);
        if (isSafe(highest)) return highest;

        Location pos1 = island.getPos1(world);
        Location pos2 = island.getPos2(world);
        for (int x = pos1.getBlockX(); x <= pos2.getBlockX(); x++) {
            for (int z = pos1.getBlockZ(); z <= pos2.getBlockZ(); z++) {
                Location newLocation = getHighestLocation(x, z, world);
                if (isSafe(newLocation)) return newLocation;
            }
        }
        return location;
    }

    /**
     * Gets the highest Location in a world
     * Mojang was dum and changed how this worked
     *
     * @param x     the x coord
     * @param z     the z coord
     * @param world The world
     * @return The highest AIR location
     */
    private static Location getHighestLocation(int x, int z, World world) {
        Block block = world.getHighestBlockAt(x, z);
        while (!block.isPassable()) {
            block = block.getLocation().add(0, 1, 0).getBlock();
        }
        return block.getLocation().add(0.5, 0, 0.5);
    }

}
