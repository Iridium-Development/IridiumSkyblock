package com.iridium.iridiumskyblock.generators.blockPopulators;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MagmaBlockPopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        SimplexNoiseGenerator generator = new SimplexNoiseGenerator(random);

        // Range is from -1 to 1.
        double noise = generator.getNoise(chunk.getX(), chunk.getZ());
        int minHeight = 0;

        List<Material> greenBlocks = Arrays.asList(
                Material.GRAVEL,
                Material.DIRT,
                Material.STONE
        );

        List<Biome> greenBiomes = Arrays.asList(
                Biome.OCEAN,
                Biome.DEEP_OCEAN,
                Biome.LUKEWARM_OCEAN,
                Biome.DEEP_LUKEWARM_OCEAN,
                Biome.WARM_OCEAN,
                Biome.COLD_OCEAN,
                Biome.DEEP_COLD_OCEAN,
                Biome.FROZEN_OCEAN,
                Biome.DEEP_FROZEN_OCEAN
        );

        try {
            minHeight = world.getMinHeight();
        } catch(NoSuchMethodError ignored) {}

        for(int x=0; x<16; x++) {
            for(int z = 0; z < 16; z++) {
                for(int y = minHeight; y < world.getMaxHeight(); y++) {

                    Block block = chunk.getBlock(x, y, z);
                    Biome biome = block.getBiome();
                    if(greenBiomes.stream().noneMatch(biomeType -> biomeType == biome)) { continue; }

                    if (block.getType() != Material.WATER) { continue; }

                    Material underBlock = chunk.getBlock(x, y - 1, z).getType();
                    if (greenBlocks.stream().noneMatch(blockType -> blockType == underBlock)) { continue; }

                    if (noise > 0.82) { continue; }

                    chunk.getBlock(x, y - 1, z).setType(Material.MAGMA_BLOCK, true);
                }
            }
        }
    }
}
