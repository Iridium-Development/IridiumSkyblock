package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;
import com.iridium.iridiumcore.dependencies.xseries.XBiome;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Generators {

    public Generators.SkyblockGeneratorConfig skyblockGenerator = new SkyblockGeneratorConfig(
            new com.iridium.iridiumskyblock.configs.Generators.SkyblockGeneratorWorld(
                    XBiome.PLAINS,
                    true
            ),
            new com.iridium.iridiumskyblock.configs.Generators.SkyblockGeneratorWorld(
                    XBiome.NETHER_WASTES,
                    true
            ),
            new com.iridium.iridiumskyblock.configs.Generators.SkyblockGeneratorWorld(
                    XBiome.THE_END,
                    true
            )
    );
    public Generators.OceanGeneratorConfig oceanGenerator = new OceanGeneratorConfig(
            new com.iridium.iridiumskyblock.configs.Generators.OceanGeneratorWorld(
                    XBiome.OCEAN,
                    XMaterial.SAND,
                    XMaterial.STONE,
                    XMaterial.WATER,
                    63, 48, 53,
                    true,
                    true
            ),
            new com.iridium.iridiumskyblock.configs.Generators.OceanGeneratorWorld(
                    XBiome.NETHER_WASTES,
                    XMaterial.SOUL_SAND,
                    XMaterial.NETHERRACK,
                    XMaterial.LAVA,
                    63, 48, 53,
                    true,
                    true
            ),
            new com.iridium.iridiumskyblock.configs.Generators.OceanGeneratorWorld(
                    XBiome.END_BARRENS,
                    XMaterial.END_STONE,
                    XMaterial.END_STONE,
                    XMaterial.VOID_AIR,
                    63, 48, 53,
                    true,
                    true
            ));

    @NoArgsConstructor
    @AllArgsConstructor
    public static class SkyblockGeneratorConfig {
        public SkyblockGeneratorWorld overworld;
        public SkyblockGeneratorWorld nether;
        public SkyblockGeneratorWorld end;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class OceanGeneratorConfig {
        public OceanGeneratorWorld overworld;
        public OceanGeneratorWorld nether;
        public OceanGeneratorWorld end;
    }

    @NoArgsConstructor
    public static class SkyblockGeneratorWorld {
        public XBiome biome;
        public boolean canSpawnEntities;

        public SkyblockGeneratorWorld(XBiome biome, boolean canSpawnEntities) {
            this.biome = biome;
            this.canSpawnEntities = canSpawnEntities;
        }
    }

    @NoArgsConstructor
    public static class OceanGeneratorWorld {
        public XBiome biome;
        public XMaterial floor;
        public XMaterial underFloor;
        public XMaterial liquidType;
        public int liquidHeight;
        public int minFloorHeight;
        public int maxFloorHeight;
        public boolean decorate;
        public boolean canSpawnEntities;

        public OceanGeneratorWorld(XBiome biome, XMaterial floor, XMaterial underFloor, XMaterial liquidType, int liquidHeight, int minFloorHeight, int maxFloorHeight, boolean decorate, boolean canSpawnEntities) {
            this.biome = biome;
            this.floor = floor;
            this.underFloor = underFloor;
            this.liquidType = liquidType;
            this.liquidHeight = liquidHeight;
            this.minFloorHeight = minFloorHeight;
            this.maxFloorHeight = maxFloorHeight;
            this.decorate = decorate;
            this.canSpawnEntities = canSpawnEntities;

        }
    }
}
