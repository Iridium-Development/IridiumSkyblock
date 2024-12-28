package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XBiome;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.reflection.XReflection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Item;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.block.Biome;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Schematics {

    public Map<String, SchematicConfig> schematics = ImmutableMap.<String, SchematicConfig>builder()
            .put("desert", new SchematicConfig(new Item(XMaterial.PLAYER_HEAD, 11, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGY0OTNkZDgwNjUzM2Q5ZDIwZTg0OTUzOTU0MzY1ZjRkMzY5NzA5Y2ViYzlkZGVmMDIyZDFmZDQwZDg2YTY4ZiJ9fX0=", 1, "&9&lDesert Island", Arrays.asList("&7A starter desert island.", "", "&9&l[!] &7Costs $1000")),
                    new Schematics.Cost(0, new HashMap<>()), 1, -0.5, 89, -0.5, 90, new SchematicWorld(XBiome.DESERT,
                    "desert.schem", 90.0, true
            ), new SchematicWorld(XBiome.NETHER_WASTES,
                    "desert_nether.schem", 90.0, true
            ), new SchematicWorld(XBiome.THE_END,
                    "desert_end.schem", 90.0, true
            )))
            .put("jungle", new SchematicConfig(new Item(XMaterial.PLAYER_HEAD, 13, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgzYWRmNDU2MGRlNDc0MTQwNDA5M2FjNjFjMzNmYjU1NmIzZDllZTUxNDBmNjIwMzYyNTg5ZmRkZWRlZmEyZCJ9fX0=", 1, "&9&lJungle Island", Arrays.asList("&7A starter jungle island.", "", "&9&l[!] &7Costs $1000")),
                    new Schematics.Cost(0, new HashMap<>()), 1, 1.5, 83, 1.5, 90, new SchematicWorld(XBiome.JUNGLE,
                    "jungle.schem", 90.0, true
            ), new SchematicWorld(XBiome.NETHER_WASTES,
                    "jungle_nether.schem", 90.0, true
            ), new SchematicWorld(XBiome.THE_END,
                    "jungle_end.schem", 90.0, true
            )))
            .put("mushroom", new SchematicConfig(new Item(XMaterial.PLAYER_HEAD, 15, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE0NWQxYjQxN2NiZGRjMjE3NjdiMDYwNDRlODk5YjI2NmJmNzhhNjZlMjE4NzZiZTNjMDUxNWFiNTVkNzEifX19", 1, "&9&lMushroom Island", Arrays.asList("&7A starter mushroom island.", "", "&9&l[!] &7Costs $1000")),
                    new Schematics.Cost(0, new HashMap<>()), 1, 0.5, 89, -0.5, 90, new SchematicWorld(XBiome.MUSHROOM_FIELDS,
                    "mushroom.schem", 90.0, true
            ), new SchematicWorld(XBiome.NETHER_WASTES,
                    "mushroom_nether.schem", 90.0, true
            ), new SchematicWorld(XBiome.THE_END,
                    "mushroom_end.schem", 90.0, true
            )))
            .build();
    public boolean abbreviatePrices = true;
    public String buyPriceLore = "&9Buy Price: &7%vault_cost%";
    public String levelRequirementLore = "&9[!] &7Must be level %level% to purchase";
    public String regenDisclaimer = "&9[!] &7Applies to regeneration only";
    public XSound failSound = XSound.BLOCK_ANVIL_LAND;
    public XSound successSound = XSound.ENTITY_PLAYER_LEVELUP;

    @NoArgsConstructor
    @AllArgsConstructor
    public static class SchematicConfig {
        public Item item;
        public Cost regenCost;
        public int minLevel;
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
        public Double islandHeight;
        public Boolean ignoreAirBlocks;

        public SchematicWorld(XBiome biome, String schematicID, Double islandHeight, Boolean ignoreAirBlocks) {
            this.biome = biome;
            this.schematicID = schematicID;
            this.islandHeight = islandHeight;
            this.ignoreAirBlocks = ignoreAirBlocks;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class Cost {
        public double money;
        public Map<String, Double> bankItems;

        @JsonIgnore
        public boolean canPurchase() {
            return money > 0 || bankItems.values().stream().anyMatch(value -> value > 0);
        }
    }

}
