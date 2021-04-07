package com.iridium.iridiumskyblock;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Class which represents a schematic.
 * Used for our own schematic storage system.
 */
@Getter
@NoArgsConstructor
public class Schematic {

    private BlockData[][][] blockData;
    private int length;
    private int height;
    private int width;

    /**
     * The default constructor.
     *
     * @param pos1 The minimum corner point position of this schematic
     * @param pos2 The maximum corner point position of this schematic
     */
    public Schematic(Location pos1, Location pos2) {
        World world = pos1.getWorld();

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        this.length = maxX - minX;
        this.height = maxY - minY;
        this.width = maxZ - minZ;

        blockData = new BlockData[length][height][width];

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType().equals(Material.AIR)) continue;
                    blockData[x - minX][y - minY][z - minZ] = new BlockData(block);
                }
            }
        }
    }

}
