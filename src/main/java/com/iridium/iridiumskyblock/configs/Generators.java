package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Generators {

    public Map<String, Generators.GeneratorConfig> generators = ImmutableMap.<String, GeneratorConfig>builder()

            .put("skyblock", new com.iridium.iridiumskyblock.configs.Generators.SkyblockGeneratorConfig(
                    new com.iridium.iridiumskyblock.configs.Generators.SkyblockGeneratorWorld(true),
                    new com.iridium.iridiumskyblock.configs.Generators.SkyblockGeneratorWorld(true),
                    new com.iridium.iridiumskyblock.configs.Generators.SkyblockGeneratorWorld(true)))

            .put("ocean", new com.iridium.iridiumskyblock.configs.Generators.OceanGeneratorConfig(
                    new com.iridium.iridiumskyblock.configs.Generators.OceanGeneratorWorld(
                            XMaterial.SAND,
                            XMaterial.STONE,
                            XMaterial.WATER,
                            63, 48, 53, true
                    ),
                    new com.iridium.iridiumskyblock.configs.Generators.OceanGeneratorWorld(
                            XMaterial.SOUL_SAND,
                            XMaterial.NETHERRACK,
                            XMaterial.LAVA,
                            63, 48, 53, true
                    ),
                    new com.iridium.iridiumskyblock.configs.Generators.OceanGeneratorWorld(
                            XMaterial.END_STONE,
                            XMaterial.END_STONE,
                            XMaterial.VOID_AIR,
                            63, 48, 53, true
                    )))
            .build();

    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeneratorConfig {
        public Generators.SkyblockGeneratorConfig skyblock;
        public Generators.OceanGeneratorConfig ocean;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class SkyblockGeneratorConfig extends GeneratorConfig{
        public SkyblockGeneratorWorld overworld;
        public SkyblockGeneratorWorld nether;
        public SkyblockGeneratorWorld end;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class OceanGeneratorConfig extends GeneratorConfig{
        public OceanGeneratorWorld overworld;
        public OceanGeneratorWorld nether;
        public OceanGeneratorWorld end;
    }

    @NoArgsConstructor
    public static class SkyblockGeneratorWorld {
        public boolean canSpawnEntities;

        public SkyblockGeneratorWorld(boolean canSpawnEntities) {
            this.canSpawnEntities = canSpawnEntities;
        }
    }

    @NoArgsConstructor
    public static class OceanGeneratorWorld {

        public XMaterial floor;
        public XMaterial underFloor;
        public XMaterial liquidType;
        public int liquidHeight;
        public int minFloorHeight;
        public int maxFloorHeight;
        public boolean canSpawnEntities;

        public OceanGeneratorWorld( XMaterial floor, XMaterial underFloor, XMaterial liquidType, int liquidHeight, int minFloorHeight, int maxFloorHeight, boolean canSpawnEntities) {
            this.floor = floor;
            this.underFloor = underFloor;
            this.liquidType = liquidType;
            this.liquidHeight = liquidHeight;
            this.minFloorHeight = minFloorHeight;
            this. maxFloorHeight = maxFloorHeight;
            this.canSpawnEntities = canSpawnEntities;

        }
    }
}
