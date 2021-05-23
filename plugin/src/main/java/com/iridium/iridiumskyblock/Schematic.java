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
 * Serialized in the database.
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

        int minX = pos1.getBlockX();
        int minY = pos1.getBlockY();
        int minZ = pos1.getBlockZ();
        int maxX = pos2.getBlockX();
        int maxY = pos2.getBlockY();
        int maxZ = pos2.getBlockZ();

        this.length = (maxX - minX) + 1;
        this.height = (maxY - minY) + 1;
        this.width = (maxZ - minZ) + 1;

        blockData = new BlockData[length][height][width];

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType().equals(Material.AIR)) continue;
                    blockData[x - minX][y - minY][z - minZ] = new BlockData(block);
                }
            }
        }
    }

}
