package com.iridium.iridiumskyblock.generators;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Generators;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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

        Map<String, Generators.GeneratorConfig> generatorConfig = IridiumSkyblock.getInstance().getGenerators().generators;

        switch(world.getEnvironment()) {
            case NETHER: {
                return generatorConfig.get("canSpawnEntities").skyblock.nether.canSpawnEntities;
            }
            case THE_END: {
                return generatorConfig.get("canSpawnEntities").skyblock.end.canSpawnEntities;
            }
            default: {
                return generatorConfig.get("canSpawnEntities").skyblock.overworld.canSpawnEntities;
            }
        }
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Collections.emptyList();
    }

}
