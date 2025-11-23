package com.iridium.iridiumskyblock.generators.blockPopulators;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class KelpBlockPopulator extends BlockPopulator {

    List<Biome> greenBiomes = Arrays.asList(
            Biome.OCEAN,
            Biome.DEEP_OCEAN,
            Biome.LUKEWARM_OCEAN,
            Biome.DEEP_LUKEWARM_OCEAN,
            Biome.COLD_OCEAN,
            Biome.DEEP_COLD_OCEAN
    );

    List<Material> greenBlocks = Arrays.asList(
            Material.GRAVEL,
            Material.SAND,
            Material.DIRT
    );

    @Override
    public void populate(World world, Random random, Chunk chunk) {

        // If biome isn't in chunk, skip chunk
        //for(Biome biome: greenBiomes) {
        //    if(!chunk.contains(biome)) return;
        //}

        // minHeight() does not exist pre-1.17
        int minHeight = 0;
        try {
            minHeight = world.getMinHeight();
        } catch(NoSuchMethodError ignored) {}

        // Build x and z coordinates
        int xHits = random.nextInt(16);
        int zHits = random.nextInt(16);

        List<Integer> xCoords = new ArrayList<>();
        List<Integer> zCoords = new ArrayList<>();

        for(int i = 0; i < xHits; i++) {
            int coord = random.nextInt(16);
            xCoords.add(coord);
        }

        for(int i = 0; i < zHits; i++) {
            int coord = random.nextInt(16);
            zCoords.add(coord);
        }

        // Chunksnapshots are generally safer to work on
        ChunkSnapshot chunkSnapshot = chunk.getChunkSnapshot();

        for(Integer x: xCoords) {
            for(Integer z: zCoords) {
                for(int y = world.getMaxHeight() - 1; y > minHeight; y--) {

                    Block block = chunk.getBlock(x, y, z);

                    // If NOT in biome, skip column
                    // Since biomes have a Y value now, too
                    if(greenBiomes.stream().noneMatch(biomeType -> biomeType == block.getBiome())) { break; }

                    BlockData blockData = chunkSnapshot.getBlockData(x, y, z);

                    // If NOT in water, skip block
                    if (blockData.getMaterial() != Material.WATER) { continue; }

                    // If NOT above block, skip block
                    Material underBlock = chunkSnapshot.getBlockData(x, y - 1, z).getMaterial();
                    if (greenBlocks.stream().noneMatch(blockType -> blockType == underBlock)) { continue; }

                    // Set kelp
                    block.setType(Material.KELP_PLANT, true);

                    // Grow kelp
                    int count = 1;
                    for(int age = random.nextInt(2, 25); age < 25; age++) {
                        Material overBlock = chunkSnapshot.getBlockData(x, y + count, z).getMaterial();
                        Material overBlock2 = chunkSnapshot.getBlockData(x, y + count + 1, z).getMaterial();
                        if(overBlock != Material.WATER
                                || (overBlock == Material.WATER && overBlock2 != Material.WATER)) {
                            break;
                        }
                        chunk.getBlock(x, y + count, z).setType(Material.KELP_PLANT, true);
                        count++;
                    }

                    // Skip rest of column
                    break;
                }
            }
        }
    }
}
