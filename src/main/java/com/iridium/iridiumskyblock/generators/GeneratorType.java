package com.iridium.iridiumskyblock.generators;

import java.util.function.Supplier;
import org.bukkit.generator.ChunkGenerator;

/**
 * Represents a generator for the IridiumSkyblock world.
 */
public enum GeneratorType {

    SKYBLOCK(SkyblockGenerator::new),
    OCEAN(OceanGenerator::new);

    private IridiumChunkGenerator chunkGenerator;

    /**
     * The default constructor.
     *
     * @param chunkGenerator The ChunkGenerator for this generator
     */
    GeneratorType(Supplier<? extends IridiumChunkGenerator> chunkGenerator) {
        this.chunkGenerator = chunkGenerator.get();
    }

    /**
     * The {@link ChunkGenerator} for this generator.
     *
     * @return The ChunkGenerator
     */
    public IridiumChunkGenerator getChunkGenerator() {
        return chunkGenerator;
    }

}
