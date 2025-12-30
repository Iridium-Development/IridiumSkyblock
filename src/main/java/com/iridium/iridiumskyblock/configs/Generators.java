package com.iridium.iridiumskyblock.configs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.cryptomorin.xseries.XBiome;
import com.cryptomorin.xseries.XMaterial;
import static com.google.common.collect.Lists.newArrayList;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Generators {

    public double simplexTerrainScale = 0.005D;
    public int simplexTerrainOctave = 8;
    public boolean useLegacyPopulators = false;
    public boolean generateSurface = false;
    public long seed = 0;

    public double simplexBiomeScale = 0.01;
    public int simplexBiomeOctave = 4;
    public boolean biomeCurve = true;
    public boolean increaseRandom = true;
    public boolean biomeGradient = true;
    public boolean inverseBiomeGradient = false;

    public Generators.SkyblockGeneratorConfig skyblockGenerator = new SkyblockGeneratorConfig(
            new SkyblockGeneratorWorld(
                    newArrayList(
                        new BiomeDataConfig(XBiome.PLAINS, new BiomeData(0.8, 0.5, 1))),
                    true
            ),
            new com.iridium.iridiumskyblock.configs.Generators.SkyblockGeneratorWorld(
                    newArrayList(
                            new BiomeDataConfig(XBiome.NETHER_WASTES, new BiomeData(2, 0, 0))),
                    true
            ),
            new com.iridium.iridiumskyblock.configs.Generators.SkyblockGeneratorWorld(
                    newArrayList(
                            new BiomeDataConfig(XBiome.THE_END, new BiomeData(2, 0, 0))),
                    true
            )
    );

    public Generators.OceanGeneratorConfig oceanGenerator = new OceanGeneratorConfig(
            new com.iridium.iridiumskyblock.configs.Generators.OceanGeneratorWorld(
                    newArrayList(
                            new BiomeDataConfig(XBiome.OCEAN, new BiomeData(0.5, 1, 0)),
                            new BiomeDataConfig(XBiome.COLD_OCEAN, new BiomeData(0.2, 1, 0)),
                            new BiomeDataConfig(XBiome.FROZEN_OCEAN, new BiomeData(-1, 1, 0)),
                            new BiomeDataConfig(XBiome.LUKEWARM_OCEAN, new BiomeData(0.6, 1, 0)),
                            new BiomeDataConfig(XBiome.WARM_OCEAN, new BiomeData(0.9, 1, 0))
                    ),
                    XMaterial.SAND,
                    XMaterial.GRAVEL,
                    XMaterial.STONE,
                    XMaterial.WATER,
                    63, 48, 53,
                    false,
                    true,
                    true
            ),
            new com.iridium.iridiumskyblock.configs.Generators.OceanGeneratorWorld(
                    newArrayList(
                            new BiomeDataConfig(XBiome.NETHER_WASTES, new BiomeData(2, 0, 0)),
                            new BiomeDataConfig(XBiome.CRIMSON_FOREST, new BiomeData(2, 0, 0)),
                            new BiomeDataConfig(XBiome.WARPED_FOREST, new BiomeData(2, 0, 0))
                    ),
                    XMaterial.SOUL_SAND,
                    XMaterial.NETHERRACK,
                    XMaterial.NETHERRACK,
                    XMaterial.LAVA,
                    63, 48, 53,
                    false,
                    true,
                    true
            ),
            new com.iridium.iridiumskyblock.configs.Generators.OceanGeneratorWorld(
                    newArrayList(
                            new BiomeDataConfig(XBiome.END_BARRENS, new BiomeData(-10, 0, 0)),
                            new BiomeDataConfig(XBiome.END_MIDLANDS, new BiomeData(-10, 0, 1)),
                            new BiomeDataConfig(XBiome.END_HIGHLANDS, new BiomeData(-10, 0, 3))
                    ),
                    XMaterial.END_STONE,
                    XMaterial.END_STONE,
                    XMaterial.END_STONE,
                    XMaterial.VOID_AIR,
                    63, 48, 53,
                    false,
                    true,
                    true
            ));

    public Generators.FlatGeneratorConfig flatGenerator = new FlatGeneratorConfig(
            new com.iridium.iridiumskyblock.configs.Generators.FlatGeneratorWorld(
                    newArrayList(
                            new BiomeDataConfig(XBiome.PLAINS, new BiomeData(0.8, 0.5, 1))
                    ),
                    XMaterial.GRASS_BLOCK,
                    XMaterial.DIRT,
                    XMaterial.STONE,
                    -59,
                    false,
                    true,
                    true
            ),
            new com.iridium.iridiumskyblock.configs.Generators.FlatGeneratorWorld(
                    newArrayList(
                            new BiomeDataConfig(XBiome.NETHER_WASTES, new BiomeData(2.0, 0, 0))
                    ),
                    XMaterial.NETHERRACK,
                    XMaterial.NETHERRACK,
                    XMaterial.NETHERRACK,
                    5,
                    false,
                    true,
                    true
            ),
            new com.iridium.iridiumskyblock.configs.Generators.FlatGeneratorWorld(
                    newArrayList(
                            new BiomeDataConfig(XBiome.END_BARRENS, new BiomeData(-10, 0, 0))
                    ),
                    XMaterial.END_STONE,
                    XMaterial.END_STONE,
                    XMaterial.END_STONE,
                    5,
                    false,
                    true,
                    true
            ));

    public Generators.MonoGeneratorConfig monoGenerator = new MonoGeneratorConfig(
            new com.iridium.iridiumskyblock.configs.Generators.MonoGeneratorWorld(
                    newArrayList(
                            new BiomeDataConfig(XBiome.MEADOW, new BiomeData(0, 0, 3)),
                            new BiomeDataConfig(XBiome.FLOWER_FOREST, new BiomeData(1, 0, 0)),
                            new BiomeDataConfig(XBiome.CHERRY_GROVE, new BiomeData(2, 0, 1))
                    ),
                    false,
                    true,
                    true
            ),
            new com.iridium.iridiumskyblock.configs.Generators.MonoGeneratorWorld(
                    newArrayList(
                            new BiomeDataConfig(XBiome.NETHER_WASTES, new BiomeData(2.0, 0, 0))
                    ),
                    false,
                    true,
                    true
            ),
            new com.iridium.iridiumskyblock.configs.Generators.MonoGeneratorWorld(
                    newArrayList(
                            new BiomeDataConfig(XBiome.SMALL_END_ISLANDS, new BiomeData(-10, 0, 0))
                    ),
                    false,
                    true,
                    true
            ));

    public Generators.SkylandsGeneratorConfig skylandsGenerator = new SkylandsGeneratorConfig(
            new com.iridium.iridiumskyblock.configs.Generators.SkylandsGeneratorWorld(
                    newArrayList(
                            new BiomeDataConfig(XBiome.PLAINS, new BiomeData(0.5, 1, 0)),
                            new BiomeDataConfig(XBiome.SNOWY_PLAINS, new BiomeData(0.2, 1, 0)),
                            new BiomeDataConfig(XBiome.FROZEN_OCEAN, new BiomeData(-1, 1, 0)),
                            new BiomeDataConfig(XBiome.LUKEWARM_OCEAN, new BiomeData(0.6, 1, 0)),
                            new BiomeDataConfig(XBiome.WARM_OCEAN, new BiomeData(0.9, 1, 0))
                    ),
                    XMaterial.GRASS_BLOCK,
                    XMaterial.DIRT,
                    XMaterial.STONE,
                    148, 32, 0.008D, 5, 1.5D, 1.8D, 0.5D, 0.8D,
                    true,
                    true,
                    true
            ),
            new com.iridium.iridiumskyblock.configs.Generators.SkylandsGeneratorWorld(
                    newArrayList(
                            new BiomeDataConfig(XBiome.NETHER_WASTES, new BiomeData(2, 0, 0)),
                            new BiomeDataConfig(XBiome.SOUL_SAND_VALLEY, new BiomeData(2, 0, 0)),
                            new BiomeDataConfig(XBiome.BASALT_DELTAS, new BiomeData(2, 0, 0))
                    ),
                    XMaterial.SOUL_SAND,
                    XMaterial.NETHERRACK,
                    XMaterial.NETHERRACK,
                    148, 32, 0.008D, 5, 1.5D, 1.8D, 0.5D, 0.8D,
                    true,
                    true,
                    true
            ),
            new com.iridium.iridiumskyblock.configs.Generators.SkylandsGeneratorWorld(
                    newArrayList(
                            new BiomeDataConfig(XBiome.END_BARRENS, new BiomeData(-10, 0, 0)),
                            new BiomeDataConfig(XBiome.END_MIDLANDS, new BiomeData(-10, 0, 1)),
                            new BiomeDataConfig(XBiome.END_HIGHLANDS, new BiomeData(-10, 0, 3))
                    ),
                    XMaterial.END_STONE,
                    XMaterial.END_STONE,
                    XMaterial.END_STONE,
                    148, 32, 0.008D, 5, 1.5D, 1.8D, 0.5D, 0.8D,
                    true,
                    true,
                    true
            ));

    @AllArgsConstructor
    @NoArgsConstructor
    public static class BiomeDataConfig {
        public XBiome biome;
        public BiomeData biomeData;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class BiomeData {
        public double temperature;
        public double humidity;
        public double height;
    }


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
    @AllArgsConstructor
    public static class MonoGeneratorConfig {
        public MonoGeneratorWorld overworld;
        public MonoGeneratorWorld nether;
        public MonoGeneratorWorld end;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class SkylandsGeneratorConfig {
        public SkylandsGeneratorWorld overworld;
        public SkylandsGeneratorWorld nether;
        public SkylandsGeneratorWorld end;
    }

    @NoArgsConstructor
    public static class SkyblockGeneratorWorld {
        public List<BiomeDataConfig> biomeDataConfig;
        public boolean spawnEntities;

        public SkyblockGeneratorWorld(
                List<BiomeDataConfig> biomeDataConfig,
                boolean spawnEntities) {

            this.biomeDataConfig = biomeDataConfig;
            this.spawnEntities = spawnEntities;
        }
    }

    @NoArgsConstructor
    public static class OceanGeneratorWorld {
        public List<BiomeDataConfig> biomeDataConfig;
        public XMaterial floor;
        public XMaterial underFloor;
        public XMaterial mantle;
        public XMaterial liquidType;
        public int liquidHeight;
        public int minFloorHeight;
        public int maxFloorHeight;
        public boolean spawnCaves;
        public boolean decorate;
        public boolean spawnEntities;

        public OceanGeneratorWorld(
                List<BiomeDataConfig> biomeDataConfig,
                XMaterial floor,
                XMaterial underFloor,
                XMaterial mantle,
                XMaterial liquidType,
                int liquidHeight,
                int minFloorHeight,
                int maxFloorHeight,
                boolean spawnCaves,
                boolean decorate,
                boolean spawnEntities) {

            this.biomeDataConfig = biomeDataConfig;
            this.floor = floor;
            this.underFloor = underFloor;
            this.mantle = mantle;
            this.liquidType = liquidType;
            this.liquidHeight = liquidHeight;
            this.minFloorHeight = minFloorHeight;
            this.maxFloorHeight = maxFloorHeight;
            this.spawnCaves = spawnCaves;
            this.decorate = decorate;
            this.spawnEntities = spawnEntities;
        }
    }

    @NoArgsConstructor
    public static class FlatGeneratorWorld {
        public List<BiomeDataConfig> biomeDataConfig;
        public XMaterial floor;
        public XMaterial underFloor;
        public XMaterial mantle;
        public int floorHeight;
        public boolean spawnCaves;
        public boolean decorate;
        public boolean spawnEntities;

        public FlatGeneratorWorld(
                List<BiomeDataConfig> biomeDataConfig,
                XMaterial floor,
                XMaterial underFloor,
                XMaterial mantle,
                int floorHeight,
                boolean spawnCaves,
                boolean decorate,
                boolean spawnEntities) {

            this.biomeDataConfig = biomeDataConfig;
            this.floor = floor;
            this.underFloor = underFloor;
            this.mantle = mantle;
            this.floorHeight = floorHeight;
            this.spawnCaves = spawnCaves;
            this.decorate = decorate;
            this.spawnEntities = spawnEntities;

        }
    }

    @NoArgsConstructor
    public static class MonoGeneratorWorld {
        public List<BiomeDataConfig> biomeDataConfig;
        public boolean spawnCaves;
        public boolean decorate;
        public boolean spawnEntities;

        public MonoGeneratorWorld(
                List<BiomeDataConfig> biomeDataConfig,
                boolean spawnCaves,
                boolean decorate,
                boolean spawnEntities) {

            this.biomeDataConfig = biomeDataConfig;
            this.spawnCaves = spawnCaves;
            this.decorate = decorate;
            this.spawnEntities = spawnEntities;

        }
    }

    @NoArgsConstructor
    public static class SkylandsGeneratorWorld {
        public List<BiomeDataConfig> biomeDataConfig;
        public XMaterial floor;
        public XMaterial underFloor;
        public XMaterial mantle;
        public int minFloorHeight;
        public int maxFloorHeight;
        public double secondSimplexTerrainScale;
        public int secondSimplexTerrainOctave;
        public double frequency;
        public double frequency2;
        public double amplitude;
        public double amplitude2;
        public boolean spawnCaves;
        public boolean decorate;
        public boolean spawnEntities;

        public SkylandsGeneratorWorld(
                List<BiomeDataConfig> biomeDataConfig,
                XMaterial floor, XMaterial underFloor,
                XMaterial mantle,
                int minFloorHeight,
                int maxFloorHeight,
                double secondSimplexTerrainScale,
                int secondSimplexTerrainOctave,
                double frequency,
                double frequency2,
                double amplitude,
                double amplitude2,
                boolean spawnCaves,
                boolean decorate,
                boolean spawnEntities) {

            this.biomeDataConfig = biomeDataConfig;
            this.floor = floor;
            this.underFloor = underFloor;
            this.mantle = mantle;
            this.minFloorHeight = minFloorHeight;
            this.maxFloorHeight = maxFloorHeight;
            this.secondSimplexTerrainScale = secondSimplexTerrainScale;
            this.secondSimplexTerrainOctave = secondSimplexTerrainOctave;
            this.frequency = frequency;
            this.frequency2 = frequency2;
            this.amplitude = amplitude;
            this.amplitude2 = amplitude2;
            this.spawnCaves = spawnCaves;
            this.decorate = decorate;
            this.spawnEntities = spawnEntities;
        }

    }
}
