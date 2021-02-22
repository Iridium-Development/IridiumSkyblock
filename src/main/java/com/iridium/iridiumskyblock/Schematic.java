package com.iridium.iridiumskyblock;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

/**
 * Class which represents a schematic.
 * Used for our own schematic storage system.
 */
@Getter
@NoArgsConstructor
public class Schematic {

    private Material[][][] materials;
    private byte[][][] data;
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

        materials = new Material[length][height][width];
        data = new byte[length][height][width];

        for (int x = minx; x < maxx; x++) {
            for (int y = miny; y < maxy; y++) {
                for (int z = minz; z < maxz; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType().equals(Material.AIR)) continue;
                    BlockState blockState = block.getState();
                    materials[x - minx][y - miny][z - minz] = blockState.getType();
                    data[x - minx][y - miny][z - minz] = blockState.getRawData();
                }
            }
        }
    }

    /**
     * Applies block data of the schematic to the specified block.
     *
     * @param block The block whose data should be updated
     */
    public void setBlock(Block block) {
        int x = block.getLocation().getBlockX();
        int y = block.getLocation().getBlockY();
        int z = block.getLocation().getBlockZ();

        if (materials[x][y][z] == null) return;
        BlockState blockState = block.getState();
        blockState.setType(materials[x][y][z]);
        blockState.setRawData(data[x][y][z]);
        blockState.update(true, false);
    }

}
