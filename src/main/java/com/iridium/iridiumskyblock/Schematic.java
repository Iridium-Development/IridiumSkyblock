package com.iridium.iridiumskyblock;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

@Getter
public class Schematic {
    private final BlockData[][][] blockData;
    private final int length;
    private final int height;
    private final int width;

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
                    blockData[x - minx][y - miny][z - minz] = new BlockData(block.getState());
                }
            }
        }

    }

    public static class BlockData {

        private final Material material;
        private final byte data;

        public BlockData(BlockState blockState) {
            this.material = blockState.getType();
            this.data = blockState.getRawData();
        }

        public void setBlock(Block block) {
            BlockState blockState = block.getState();
            blockState.setType(material);
            blockState.setRawData(data);
            blockState.update(true, false);
        }
    }
}
