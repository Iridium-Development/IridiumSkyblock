package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XBiome;
import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

public class Schematics {

    public List<SchematicConfig> schematics = Arrays.asList(
            new SchematicConfig("test", new Item(XMaterial.GRASS, 0, 1, "&b&lTest Schematic", Arrays.asList()), -7.5, 97, -8.5, new SchematicWorld(XBiome.PLAINS, "test")),
            new SchematicConfig("test2", new Item(XMaterial.GRASS, 0, 1, "&b&lTest2 Schematic", Arrays.asList()), -7.5, 97, -8.5, new SchematicWorld(XBiome.PLAINS, "test2"))
    );

    @Getter
    @NoArgsConstructor
    public static class SchematicConfig {
        private String name;
        private Item item;
        private double xHome;
        private double yHome;
        private double zHome;
        private SchematicWorld overworld;

        public SchematicConfig(String name, Item item, double xHome, double yHome, double zHome, SchematicWorld overworld) {
            this.name = name;
            this.item = item;
            this.xHome = xHome;
            this.yHome = yHome;
            this.zHome = zHome;
            this.overworld = overworld;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class SchematicWorld {
        private XBiome biome;
        private String schematicID;

        public SchematicWorld(XBiome biome, String schematicID) {
            this.biome = biome;
            this.schematicID = schematicID;
        }
    }
}
