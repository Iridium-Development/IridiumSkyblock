package com.iridium.iridiumskyblock.generators;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Generators;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class VoidGenerator extends ChunkGenerator {

    public byte[][] blockSections;

    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biomeGrid) {
        final ChunkData chunkData = createChunkData(world);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                biomeGrid.setBiome(x, z, Objects.requireNonNull(getSkyblockGenerator(world.getEnvironment()).biome.getBiome()));
            }
        }
        return chunkData;
    }

    public byte[][] generateBlockSections(World world, Random random, int x, int z, BiomeGrid biomeGrid) {
        if (blockSections == null) {
            blockSections = new byte[world.getMaxHeight() / 16][];
        }
        return blockSections;
    }

    @Override
    public boolean canSpawn(@NotNull World world, int x, int z) {
        return getSkyblockGenerator(world.getEnvironment()).canSpawnEntities;
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Collections.emptyList();
    }

    private Generators.SkyblockGeneratorWorld getSkyblockGenerator(World.Environment environment) {
        switch (environment) {
            case NETHER: {
                return IridiumSkyblock.getInstance().getGenerators().skyblockGenerator.nether;
            }
            case THE_END: {
                return IridiumSkyblock.getInstance().getGenerators().skyblockGenerator.end;
            }
            default: {
                return IridiumSkyblock.getInstance().getGenerators().skyblockGenerator.overworld;
            }
        }
    }
}
