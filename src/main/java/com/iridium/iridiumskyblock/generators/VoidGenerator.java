package com.iridium.iridiumskyblock.generators;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class VoidGenerator extends ChunkGenerator {

    public byte[][] blockSections;

    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biomeGrid) {
        return createChunkData(world);
    }

    public byte[][] generateBlockSections(World world, Random random, int x, int z, BiomeGrid biomeGrid) {
        if (blockSections == null) {
            blockSections = new byte[world.getMaxHeight() / 16][];
        }
        return blockSections;
    }

    @Override
    public boolean canSpawn(@NotNull World world, int x, int z) {
        switch (world.getEnvironment()) {
            case NETHER: {
                return IridiumSkyblock.getInstance().getGenerators().skyblockGenerator.nether.canSpawnEntities;
            }
            case THE_END: {
                return IridiumSkyblock.getInstance().getGenerators().skyblockGenerator.end.canSpawnEntities;
            }
            default: {
                return IridiumSkyblock.getInstance().getGenerators().skyblockGenerator.overworld.canSpawnEntities;
            }
        }
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Collections.emptyList();
    }

}
