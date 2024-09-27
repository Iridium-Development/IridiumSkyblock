package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XBiome;
import com.cryptomorin.xseries.XMaterial;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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

    public Generators.FlatGeneratorConfig flatGenerator = new FlatGeneratorConfig(
            new com.iridium.iridiumskyblock.configs.Generators.FlatGeneratorWorld(
                    XBiome.PLAINS,
                    XMaterial.GRASS_BLOCK,
                    XMaterial.DIRT,
                    -59,
                    true,
                    true
            ),
            new com.iridium.iridiumskyblock.configs.Generators.FlatGeneratorWorld(
                    XBiome.NETHER_WASTES,
                    XMaterial.NETHERRACK,
                    XMaterial.NETHERRACK,
                    5,
                    true,
                    true
            ),
            new com.iridium.iridiumskyblock.configs.Generators.FlatGeneratorWorld(
                    XBiome.END_BARRENS,
                    XMaterial.END_STONE,
                    XMaterial.END_STONE,
                    5,
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
    @AllArgsConstructor
    public static class FlatGeneratorConfig {
        public FlatGeneratorWorld overworld;
        public FlatGeneratorWorld nether;
        public FlatGeneratorWorld end;
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

    @NoArgsConstructor
    public static class FlatGeneratorWorld {
        public XBiome biome;
        public XMaterial floor;
        public XMaterial underFloor;
        public int floorHeight;
        public boolean decorate;
        public boolean canSpawnEntities;

        public FlatGeneratorWorld(XBiome biome, XMaterial floor, XMaterial underFloor, int floorHeight, boolean decorate, boolean canSpawnEntities) {
            this.biome = biome;
            this.floor = floor;
            this.underFloor = underFloor;
            this.floorHeight = floorHeight;
            this.decorate = decorate;
            this.canSpawnEntities = canSpawnEntities;
        }
    }
}
