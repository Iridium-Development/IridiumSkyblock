package com.iridium.iridiumskyblock.generators;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.generator.ChunkGenerator;

public abstract class IridiumChunkGenerator extends ChunkGenerator {

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private boolean generatesTerrain;
    @Getter
    @Setter
    private boolean lowerHorizon;

    public IridiumChunkGenerator(String name, boolean generatesTerrain, boolean lowerHorizon) {
        this.name = name;
        this.generatesTerrain = generatesTerrain;
        this.lowerHorizon = lowerHorizon;
    }
}
