package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnore;
import com.iridium.iridiumcore.dependencies.xseries.XBiome;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.dependencies.xseries.XSound;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.*;

public class Biomes {

    public Map<String, BiomeCategory> categories;
    public Map<String, List<BiomeItem>> items;
    public String buyPriceLore;
    public String notPurchasableLore;
    public boolean abbreviatePrices;
    public XSound failSound;
    public XSound successSound;
    public List<String> biomeItemLore;

    public Biomes() {
        this("&c");
    }

    public Biomes(String color) {
        categories = ImmutableMap.<String, BiomeCategory>builder()
                .put("Overworld", new BiomeCategory(new Item(XMaterial.GRASS_BLOCK, 20, 1, color + "&lOverworld", Collections.emptyList()), 54))
                .put("Nether", new BiomeCategory(new Item(XMaterial.CRIMSON_NYLIUM, 22, 1, color + "&lNether", Collections.emptyList()), 36))
                .put("End", new BiomeCategory(new Item(XMaterial.END_STONE, 24, 1, color + "&lEnd", Collections.emptyList()), 36))
                .build();

        items = ImmutableMap.<String, List<BiomeItem>>builder()
                .put("Overworld", Arrays.asList(
                        new BiomeItem(
                                color + "&lPlains",
                                XMaterial.GRASS_BLOCK,
                                XBiome.PLAINS,
                                "",
                                1,
                                20,
                                new Cost(100, new HashMap<>())
                        ),
                        new BiomeItem(
                                color + "&lSnowy Plains",
                                XMaterial.SNOW_BLOCK,
                                XBiome.SNOWY_PLAINS,
                                "",
                                1,
                                22,
                                new Cost(50, new HashMap<>())
                        ),
                        new BiomeItem(
                                color + "&lSavanna",
                                XMaterial.TALL_GRASS,
                                XBiome.SAVANNA,
                                "",
                                1,
                                24,
                                new Cost(100, new HashMap<>())
                        )
                ))
                .put("Nether", Arrays.asList(
                                new BiomeItem(
                                        color + "&lNether Wastes",
                                        XMaterial.NETHERRACK,
                                        XBiome.NETHER_WASTES,
                                        "",
                                        1,
                                        20,
                                        new Cost(50, new HashMap<>())
                                ),
                                new BiomeItem(
                                        color + "&lCrimson Forest",
                                        XMaterial.CRIMSON_NYLIUM,
                                        XBiome.CRIMSON_FOREST,
                                        "",
                                        1,
                                        22,
                                        new Cost(1000, new HashMap<>())
                                ),
                                new BiomeItem(
                                        color + "&lWarped Forest",
                                        XMaterial.WARPED_NYLIUM,
                                        XBiome.WARPED_FOREST,
                                        "",
                                        1,
                                        24,
                                        new Cost(100, new HashMap<>())
                                )
                        )
                )
                .put("End", Arrays.asList(
                                new BiomeItem(
                                        color + "&lTHE END",
                                        XMaterial.END_STONE,
                                        XBiome.THE_END,
                                        "",
                                        1,
                                        20,
                                        new Cost(100, new HashMap<>())
                                ),
                                new BiomeItem(
                                        color + "&lEnd Highlands",
                                        XMaterial.PURPUR_BLOCK,
                                        XBiome.END_HIGHLANDS,
                                        "",
                                        1,
                                        22,
                                        new Cost(150, new HashMap<>())
                                ),
                                new BiomeItem(
                                        color + "&lEnd Barrens",
                                        XMaterial.ENDER_EYE,
                                        XBiome.END_BARRENS,
                                        "",
                                        1,
                                        24,
                                        new Cost(150, new HashMap<>())
                                )
                        )
                )
                .build();

        buyPriceLore = "&aBuy Price: $%vault_cost%";
        notPurchasableLore = "&cThis item cannot be purchased!";

        abbreviatePrices = true;

        failSound = XSound.BLOCK_ANVIL_LAND;
        successSound = XSound.ENTITY_PLAYER_LEVELUP;

        biomeItemLore = Arrays.asList(
                color + "&l[!] " + color + "Left-Click to Purchase"
        );
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class BiomeCategory {
        public Item item;
        public int inventorySize;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class BiomeItem {
        public String name;
        public XMaterial type;
        public XBiome biome;
        public List<String> lore;
        public String command;
        public int defaultAmount;
        public int slot;
        public int page;
        public Cost buyCost;

        public BiomeItem(String name, XMaterial type, XBiome biome, String command, int defaultAmount, int slot, Cost buyCost) {
            this.name = name;
            this.type = type;
            this.biome = biome;
            this.lore = Collections.emptyList();
            this.command = command;
            this.defaultAmount = defaultAmount;
            this.slot = slot;
            this.page = 1;
            this.buyCost = buyCost;
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
