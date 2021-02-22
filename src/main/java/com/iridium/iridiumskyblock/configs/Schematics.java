package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XBiome;
import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Item;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class Schematics {

    public List<SchematicConfig> schematics = Arrays.asList(
            new SchematicConfig("test", new Item(XMaterial.GRASS, 0, 1, "&b&lTest Schematic", Arrays.asList()), -7, 97, -8, new SchematicWorld(XBiome.PLAINS, "test")),
            new SchematicConfig("test2", new Item(XMaterial.GRASS, 0, 1, "&b&lTest2 Schematic", Arrays.asList()), -7, 97, -8, new SchematicWorld(XBiome.PLAINS, "test2"))
    );

    @Getter
    public static class SchematicConfig {
        private final String name;
        private final Item item;
        private final int xHome;
        private final int yHome;
        private final int zHome;
        private final SchematicWorld overworld;

        public SchematicConfig(String name, Item item, int xHome, int yHome, int zHome, SchematicWorld overworld) {
            this.name = name;
            this.item = item;
            this.xHome = xHome;
            this.yHome = yHome;
            this.zHome = zHome;
            this.overworld = overworld;
        }
    }

    @Getter
    public static class SchematicWorld {
        private final XBiome biome;
        private final String schematicID;

        public SchematicWorld(XBiome biome, String schematicID) {
            this.biome = biome;
            this.schematicID = schematicID;
        }
    }
}
