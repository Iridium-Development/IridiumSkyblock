package com.iridium.iridiumskyblock.generators.blockPopulators;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KelpBlockPopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        SimplexNoiseGenerator generator = new SimplexNoiseGenerator(random);
        ChunkSnapshot chunkSnapshot = chunk.getChunkSnapshot();

        int minHeight = 0;

        List<Material> air = Arrays.asList(
                Material.AIR,
                Material.CAVE_AIR,
                Material.VOID_AIR
        );

        List<Material> greenBlocks = Arrays.asList(
                Material.GRAVEL,
                Material.SAND,
                Material.DIRT
        );

        List<Biome> greenBiomes = Arrays.asList(
                Biome.OCEAN,
                Biome.DEEP_OCEAN,
                Biome.LUKEWARM_OCEAN,
                Biome.DEEP_LUKEWARM_OCEAN,
                Biome.COLD_OCEAN,
                Biome.DEEP_COLD_OCEAN
        );

        try {
            minHeight = world.getMinHeight();
        } catch(NoSuchMethodError ignored) {}

        for(int x=0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                // Range is from -1 to 1.
                if (generator.getNoise(x, z) > 0.065) { continue; }

                for(int y = minHeight; y < world.getMaxHeight(); y++) {

                    Block block = chunk.getBlock(x, y, z);
                    BlockData blockData = chunkSnapshot.getBlockData(x, y, z);

                    if (air.stream().noneMatch(blockType -> blockType == blockData.getMaterial())) { continue; }
                    if (blockData.getMaterial() != Material.WATER) { continue; }

                    if(greenBiomes.stream().noneMatch(biomeType -> biomeType == block.getBiome())) { continue; }

                    Material underBlock = chunkSnapshot.getBlockData(x, y - 1, z).getMaterial();
                    if (greenBlocks.stream().noneMatch(blockType -> blockType == underBlock)) { continue; }

                    block.setType(Material.KELP_PLANT, true);

                    int count = 1;
                    for(int age = random.nextInt(2, 25); age < 25; age++) {
                        Material overBlock = chunkSnapshot.getBlockData(x, y + count, z).getMaterial();
                        if(overBlock != Material.WATER) { break; }
                        chunk.getBlock(x, y + count, z).setType(Material.KELP_PLANT, true);
                        count++;
                    }
                }
            }
        }
    }
}
