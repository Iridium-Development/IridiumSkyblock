package com.iridium.iridiumskyblock.managers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GeneratorType {
    VOID(false, true),
    OCEAN(true, false),
    FLAT(true, true),
    VANILLA(true, false);

    private final boolean terrainGenerator;
    private final boolean lowerHorizon;
}