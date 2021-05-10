package com.iridium.iridiumskyblock.generators;

import com.iridium.iridiumskyblock.utils.BiomeUtils;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Class which handles the {@link World} generation of IridiumSkyblock.
 * Creates an empty void world.
 */
public class SkyblockGenerator extends ChunkGenerator {

    public byte[][] blockSections;

    /**
     * Generates an empty chunk.
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
        BiomeUtils.setGeneratorBiome(world, biomeGrid);
        return createChunkData(world);
    }

    /**
     * Provides the block sections of this generator to Bukkit.
     *
     * @param world     The world that we try to generate
     * @param random    A random which should be used. We don't need this
     * @param x         The x position of the chunk
     * @param z         The y position of the chunk
     * @param biomeGrid The biome grid of the chunk
     * @return The block sections of this world
     */
    public byte[][] generateBlockSections(World world, Random random, int x, int z, BiomeGrid biomeGrid) {
        if (blockSections == null) {
            blockSections = new byte[world.getMaxHeight() / 16][];
        }
        return blockSections;
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

    /**
     * Tells Bukkit that we never want any block populators.
     *
     * @param world The world where the {@link BlockPopulator}s should be in
     * @return Always an empty list because we don't want any
     */
    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Collections.emptyList();
    }

}