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
     * The default constructor. Will most likely change soon.
     *
     * @param pos1 The minimum corner point position of this schematic
     * @param pos2 The maximum corner point position of this schematic
     */
    public Schematic(Location pos1, Location pos2) {
        World world = pos1.getWorld();

        int minx = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int miny = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int minz = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxx = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int maxy = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int maxz = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        this.length = maxx - minx;
        this.height = maxy - miny;
        this.width = maxz - minz;

        blockData = new BlockData[length][height][width];

        for (int x = minx; x < maxx; x++) {
            for (int y = miny; y < maxy; y++) {
                for (int z = minz; z < maxz; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType().equals(Material.AIR)) continue;
                    blockData[x - minx][y - miny][z - minz] = new BlockData(block);
                }
            }
        }
    }

}
