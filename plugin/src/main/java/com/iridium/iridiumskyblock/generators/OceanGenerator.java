package com.iridium.iridiumskyblock.generators;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

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
                XMaterial oceanMaterial = world.getEnvironment() == Environment.NETHER ? XMaterial.LAVA : XMaterial.WATER;
                for (int y = currentFloorHeight + 1; y <= waterHeight; y++) {
                    chunkData.setBlock(x, y, z, Objects.requireNonNull(oceanMaterial.parseMaterial()));
                }
            }
        }

        return chunkData;
    }

    public void generateWater(World world, int x, int z) {
        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
        generator.setScale(0.005D);

        XMaterial bottomMaterial = IridiumSkyblock.getInstance().getConfiguration().generatorSettings.oceanFloorBottomMaterial;
        XMaterial topMaterial = IridiumSkyblock.getInstance().getConfiguration().generatorSettings.oceanFloorTopMaterial;
        int waterHeight = IridiumSkyblock.getInstance().getConfiguration().generatorSettings.waterHeight;
        int maxOceanFloorLevel = IridiumSkyblock.getInstance().getConfiguration().generatorSettings.maxOceanFloorLevel;
        int minOceanFloorLevel = IridiumSkyblock.getInstance().getConfiguration().generatorSettings.minOceanFloorLevel;

        int currentFloorHeight = (int) ((generator.noise(x, z, 1.5D, 0.5D, true) + 1) * (maxOceanFloorLevel - minOceanFloorLevel) + minOceanFloorLevel);

        // Generate layer of bedrock
        if (world.getBlockAt(x, 0, z).getType() != XMaterial.BEDROCK.parseMaterial()) {
            IridiumSkyblock.getInstance().getNms().setBlockFast(
                world,
                x,
                0,
                z,
                XMaterial.BEDROCK.getId(),
                XMaterial.BEDROCK.getData(),
                false
            );
        }

        // Generate gravel layer
        for (int y = 1; y < currentFloorHeight; y++) {
            if (world.getBlockAt(x, y, z).getType() != bottomMaterial.parseMaterial()) {
                IridiumSkyblock.getInstance().getNms().setBlockFast(
                    world,
                    x,
                    y,
                    z,
                    bottomMaterial.getId(),
                    bottomMaterial.getData(),
                    false
                );
            }
        }

        // Generate sand on top of gravel
        if (world.getBlockAt(x, currentFloorHeight, z).getType() != topMaterial.parseMaterial()) {
            IridiumSkyblock.getInstance().getNms().setBlockFast(
                world,
                x,
                currentFloorHeight,
                z,
                topMaterial.getId(),
                topMaterial.getData(),
                false
            );
        }

        // Generate water or lava on top of the floor
        XMaterial oceanMaterial = world.getEnvironment() == Environment.NETHER ? XMaterial.LAVA : XMaterial.WATER;
        for (int y = currentFloorHeight + 1; y <= waterHeight; y++) {
            if (world.getBlockAt(x, y, z).getType() != oceanMaterial.parseMaterial()) {
                IridiumSkyblock.getInstance().getNms().setBlockFast(
                    world,
                    x,
                    y,
                    z,
                    oceanMaterial.getId(),
                    oceanMaterial.getData(),
                    false
                );
            }
        }

        // Replace everything else with air
        for (int y = waterHeight + 1; y < world.getMaxHeight(); y++) {
            if (world.getBlockAt(x, y, z).getType() != XMaterial.AIR.parseMaterial()) {
                IridiumSkyblock.getInstance().getNms().setBlockFast(
                    world,
                    x,
                    y,
                    z,
                    XMaterial.AIR.getId(),
                    XMaterial.AIR.getData(),
                    true
                );
            }
        }
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
