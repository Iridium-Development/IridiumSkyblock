package com.iridium.iridiumskyblock.utils;

import com.cryptomorin.xseries.XBiome;
import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;

/**
 * Utils for working with biomes.
 */
public class BiomeUtils {

    private static final boolean HORIZONTAL_SUPPORT = XMaterial.supports(16);

    /**
     * Sets the default biome in a {@link BiomeGrid} during chunk generation.
     *
     * @param world The world which is being generated
     * @param biomeGrid The biome grid of the chunk generator
     */
    public static void setGeneratorBiome(World world, BiomeGrid biomeGrid) {
        XBiome biome;
        switch (world.getEnvironment()) {
            case NORMAL:
                biome = IridiumSkyblock.getInstance().getConfiguration().generatorSettings.defaultOverworldBiome;
                break;
            case NETHER:
                biome = IridiumSkyblock.getInstance().getConfiguration().generatorSettings.defaultNetherBiome;
                break;
            default:
                biome = XBiome.PLAINS;
        }

        if (biome.getBiome() == null) {
            return;
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (HORIZONTAL_SUPPORT) {
                    // y loop for 1.16+ support (vertical biomes).
                    // As of now increasing it by 4 seems to work.
                    // This should be the minimal size of the vertical biomes.
                    for (int y = 0; y < 256; y += 4) {
                        biomeGrid.setBiome(x, y, z, biome.getBiome());
                    }
                } else {
                    biomeGrid.setBiome(x, z, biome.getBiome());
                }
            }
        }
    }

}
