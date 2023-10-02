package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnore;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.dependencies.xseries.XSound;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.block.Biome;

import java.util.Collections;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Schematics {

    public Map<String, SchematicConfig> schematics;
    public String buyPriceLore;
    public String notPurchasableLore;
    public boolean abbreviatePrices;
    public XSound failSound;
    public XSound successSound;
    public List<String> schematicPurchaseLore;

    public Schematics() {
        this("&c");
    }

    public Schematics(String color) {
        schematics = ImmutableMap.<String, SchematicConfig>builder()
                .put("desert", new SchematicConfig(new Item(XMaterial.PLAYER_HEAD, 11, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGY0OTNkZDgwNjUzM2Q5ZDIwZTg0OTUzOTU0MzY1ZjRkMzY5NzA5Y2ViYzlkZGVmMDIyZDFmZDQwZDg2YTY4ZiJ9fX0=", 1, "&9&lDesert Island", Collections.singletonList("&7A starter desert island.")),
                        new Schematics.Cost(100, new HashMap<>()), -0.5, 89, -0.5, 90, new SchematicWorld(Biome.DESERT,
                        "desert.schem", 90.0, true
                ), new SchematicWorld(Biome.NETHER_WASTES,
                        "desert_nether.schem", 90.0, true
                ), new SchematicWorld(Biome.THE_END,
                        "desert_end.schem", 90.0, true
                )))
                .put("jungle", new SchematicConfig(new Item(XMaterial.PLAYER_HEAD, 13, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgzYWRmNDU2MGRlNDc0MTQwNDA5M2FjNjFjMzNmYjU1NmIzZDllZTUxNDBmNjIwMzYyNTg5ZmRkZWRlZmEyZCJ9fX0=", 1, "&9&lJungle Island", Collections.singletonList("&7A starter jungle island.")),
                        new Schematics.Cost(100, new HashMap<>()), 1.5, 83, 1.5, 90, new SchematicWorld(Biome.JUNGLE,
                        "jungle.schem", 90.0, true
                ), new SchematicWorld(Biome.NETHER_WASTES,
                        "jungle_nether.schem", 90.0, true
                ), new SchematicWorld(Biome.THE_END,
                        "jungle_end.schem", 90.0, true
                )))
                .put("mushroom", new SchematicConfig(new Item(XMaterial.PLAYER_HEAD, 15, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE0NWQxYjQxN2NiZGRjMjE3NjdiMDYwNDRlODk5YjI2NmJmNzhhNjZlMjE4NzZiZTNjMDUxNWFiNTVkNzEifX19", 1, "&9&lMushroom Island", Collections.singletonList("&7A starter mushroom island.")),
                        new Schematics.Cost(100, new HashMap<>()), 0.5, 89, -0.5, 90, new SchematicWorld(Biome.MUSHROOM_FIELDS,
                        "mushroom.schem", 90.0, true
                ), new SchematicWorld(Biome.NETHER_WASTES,
                        "mushroom_nether.schem", 90.0, true
                ), new SchematicWorld(Biome.THE_END,
                        "mushroom_end.schem", 90.0, true
                )))
                .build();

        buyPriceLore = "&aBuy Price: $%vault_cost%";
        notPurchasableLore = "&cThis item cannot be purchased!";

        abbreviatePrices = true;

        failSound = XSound.BLOCK_ANVIL_LAND;
        successSound = XSound.ENTITY_PLAYER_LEVELUP;

        schematicPurchaseLore = Arrays.asList(
                " ",
                color + "&l[!] " + color + "Left-Click to Purchase %amount%, Shift for 64",
                color + "&l[!] " + color + "Right Click to Sell %amount%, Shift for 64"
        );
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class SchematicConfig {
        public Item item;
        public Cost regenCost;
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
        public Biome biome;
        public String schematicID;
        public Double islandHeight;
        public Boolean ignoreAirBlocks;

        public SchematicWorld(Biome biome, String schematicID, Double islandHeight, Boolean ignoreAirBlocks) {
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
