package com.iridium.iridiumskyblock.generators;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.BiomeUtils;
import java.util.Objects;
import java.util.Random;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * Class which handles the {@link World} generation of IridiumSkyblock.
 * Creates an ocean world.
 */
public class OceanGenerator extends ChunkGenerator {

    /**
     * Generates an ocean.
     *
     * @param world     The world that we try to generate
     * @param random    A random which should be used. We don't need this
     * @param chunkX    The x position of the chunk
     * @param chunkZ    The y position of the chunk
     * @param biomeGrid The biome grid of the chunk
     * @return The data of this chunk. None all the time to generate void
     */
    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biomeGrid) {
        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
        final ChunkData chunkData = createChunkData(world);
        generator.setScale(0.005D);

        BiomeUtils.setGeneratorBiome(world, biomeGrid);

        XMaterial bottomMaterial = IridiumSkyblock.getInstance().getConfiguration().generatorSettings.oceanFloorBottomMaterial;
        XMaterial topMaterial = IridiumSkyblock.getInstance().getConfiguration().generatorSettings.oceanFloorTopMaterial;
        int waterHeight = IridiumSkyblock.getInstance().getConfiguration().generatorSettings.waterHeight;
        int maxOceanFloorLevel = IridiumSkyblock.getInstance().getConfiguration().generatorSettings.maxOceanFloorLevel;
        int minOceanFloorLevel = IridiumSkyblock.getInstance().getConfiguration().generatorSettings.minOceanFloorLevel;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int currentFloorHeight = (int) ((generator.noise(chunkX * 16 + x, chunkZ * 16 + z, 1.5D, 0.5D, true) + 1) * (maxOceanFloorLevel - minOceanFloorLevel) + minOceanFloorLevel);

                // Generate layer of bedrock
                chunkData.setBlock(x, 0, z,
                    Objects.requireNonNull(XMaterial.BEDROCK.parseMaterial())
                );

                // Generate gravel layer
                for (int y = 1; y < currentFloorHeight; y++) {
                    chunkData.setBlock(x, y, z,
                        Objects.requireNonNull(bottomMaterial.parseMaterial())
                    );
                }

                // Generate sand on top of gravel
                chunkData.setBlock(x, currentFloorHeight, z,
                    Objects.requireNonNull(topMaterial.parseMaterial())
                );

                // Generate water or lava on top of the floor
                for (int y = currentFloorHeight + 1; y <= waterHeight; y++) {
                    XMaterial material = world.getEnvironment() == Environment.NETHER ? XMaterial.LAVA : XMaterial.WATER;
                    chunkData.setBlock(x, y, z, Objects.requireNonNull(material.parseMaterial()));
                }
            }
        }

        return chunkData;
    }

    /**
     * Tells Bukkit that we always want entities to spawn.
     *
     * @param world The world the entity wants to spawn in
     * @param x     The x location of the spawning attempt
     * @param z     The z location of the spawning attempt
     * @return true because we always want entities to spawn
     */
    @Override
    public boolean canSpawn(@NotNull World world, int x, int z) {
        return true;
    }

}
