package com.iridium.iridiumskyblock.utils;

import org.bukkit.Location;
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

}
