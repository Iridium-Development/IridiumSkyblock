package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XBiome;
import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Item;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Schematics {

    public List<SchematicConfig> schematics = Arrays.asList(
            new SchematicConfig("island",
                    new Item(XMaterial.PLAYER_HEAD, 11, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGY0OTNkZDgwNjUzM2Q5ZDIwZTg0OTUzOTU0MzY1ZjRkMzY5NzA5Y2ViYzlkZGVmMDIyZDFmZDQwZDg2YTY4ZiJ9fX0=", 1, "&b&lDesert Island", Collections.singletonList("&7A starter desert island.")),
                    -3.5, 95, -5.5, new SchematicWorld(XBiome.PLAINS, "island")),
            new SchematicConfig("island",
                    new Item(XMaterial.PLAYER_HEAD, 13, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgzYWRmNDU2MGRlNDc0MTQwNDA5M2FjNjFjMzNmYjU1NmIzZDllZTUxNDBmNjIwMzYyNTg5ZmRkZWRlZmEyZCJ9fX0=", 1, "&b&lJungle Island", Collections.singletonList("&7A starter jungle island.")),
                    -3.5, 95, -5.5, new SchematicWorld(XBiome.PLAINS, "island")),
            new SchematicConfig("island",
                    new Item(XMaterial.PLAYER_HEAD, 15, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE0NWQxYjQxN2NiZGRjMjE3NjdiMDYwNDRlODk5YjI2NmJmNzhhNjZlMjE4NzZiZTNjMDUxNWFiNTVkNzEifX19", 1, "&b&lMushroom Island", Collections.singletonList("&7A starter mushroom island.")),
                    -3.5, 95, -5.5, new SchematicWorld(XBiome.PLAINS, "island"))
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
