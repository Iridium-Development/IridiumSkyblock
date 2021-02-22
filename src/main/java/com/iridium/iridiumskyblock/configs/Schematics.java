package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XBiome;
import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Item;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

public class Schematics {

    public List<SchematicConfig> schematics = Arrays.asList(
            new SchematicConfig("test", new Item(XMaterial.GRASS, 0, 1, "&b&lTest Schematic", Arrays.asList()), -7.5, 97, -8.5, new SchematicWorld(XBiome.PLAINS, "test")),
            new SchematicConfig("test2", new Item(XMaterial.GRASS, 0, 1, "&b&lTest2 Schematic", Arrays.asList()), -7.5, 97, -8.5, new SchematicWorld(XBiome.PLAINS, "test2"))
    );

    @NoArgsConstructor
    public static class SchematicConfig {
        public String name;
        public Item item;
        public double xHome;
        public double yHome;
        public double zHome;
        public SchematicWorld overworld;

        public SchematicConfig(String name, Item item, double xHome, double yHome, double zHome, SchematicWorld overworld) {
            this.name = name;
            this.item = item;
            this.xHome = xHome;
            this.yHome = yHome;
            this.zHome = zHome;
            this.overworld = overworld;
        }
    }

    @NoArgsConstructor
    public static class SchematicWorld {
        public XBiome biome;
        public String schematicID;

        public SchematicWorld(XBiome biome, String schematicID) {
            this.biome = biome;
            this.schematicID = schematicID;
        }
    }
}
