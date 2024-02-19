package com.iridium.iridiumskyblock.managers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GeneratorType {
    VOID(false), OCEAN(true), VANILLA(true);
    private final boolean terrainGenerator;
}