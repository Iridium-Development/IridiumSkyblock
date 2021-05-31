package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.xseries.XBiome;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Map;

/**
 * The schematic configuration used by IridiumSkyblock (schematics.yml).
 * Is deserialized automatically on plugin startup and reload.
 */
public class Schematics {

    public Map<String, SchematicConfig> schematics = ImmutableMap.<String, SchematicConfig>builder()
            .put("desert", new SchematicConfig(new Item(XMaterial.PLAYER_HEAD, 11, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGY0OTNkZDgwNjUzM2Q5ZDIwZTg0OTUzOTU0MzY1ZjRkMzY5NzA5Y2ViYzlkZGVmMDIyZDFmZDQwZDg2YTY4ZiJ9fX0=", 1, "&b&lDesert Island", Collections.singletonList("&7A starter desert island.")),
                    -1.5, 95, -0.5, 90, new SchematicWorld(XBiome.PLAINS, "desert.iridiumschem"), new SchematicWorld(XBiome.NETHER_WASTES, "desert_nether.iridiumschem"), new SchematicWorld(XBiome.THE_END, "desert_end.iridiumschem")))
            .put("jungle", new SchematicConfig(new Item(XMaterial.PLAYER_HEAD, 13, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgzYWRmNDU2MGRlNDc0MTQwNDA5M2FjNjFjMzNmYjU1NmIzZDllZTUxNDBmNjIwMzYyNTg5ZmRkZWRlZmEyZCJ9fX0=", 1, "&b&lJungle Island", Collections.singletonList("&7A starter jungle island.")),
                    1.5, 95, 0.5, 90, new SchematicWorld(XBiome.PLAINS, "jungle.iridiumschem"), new SchematicWorld(XBiome.NETHER_WASTES, "jungle_nether.iridiumschem"), new SchematicWorld(XBiome.THE_END, "jungle_end.iridiumschem")))
            .put("mushroom", new SchematicConfig(new Item(XMaterial.PLAYER_HEAD, 15, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE0NWQxYjQxN2NiZGRjMjE3NjdiMDYwNDRlODk5YjI2NmJmNzhhNjZlMjE4NzZiZTNjMDUxNWFiNTVkNzEifX19", 1, "&b&lMushroom Island", Collections.singletonList("&7A starter mushroom island.")),
                    -1.5, 95, -0.5, 90, new SchematicWorld(XBiome.PLAINS, "mushroom.iridiumschem"), new SchematicWorld(XBiome.NETHER_WASTES, "mushroom_nether.iridiumschem"), new SchematicWorld(XBiome.THE_END, "mushroom_end.iridiumschem")))
            .build();

    @NoArgsConstructor
    @AllArgsConstructor
    public static class SchematicConfig {
        public Item item;
        public double xHome;
        public double yHome;
        public double zHome;
        public float yawHome;
        public SchematicWorld overworld;
        public SchematicWorld nether;
        public SchematicWorld end;
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
