package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XBiome;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Item;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.*;

public class Biomes {

    public Map<String, BiomeCategory> categories = ImmutableMap.<String, BiomeCategory>builder()
            .put("Overworld", new BiomeCategory(new Item(XMaterial.GRASS_BLOCK, 11, 1, "&9&lOverworld", Collections.emptyList()), 27))
            .put("Nether", new BiomeCategory(new Item(XMaterial.CRIMSON_NYLIUM, 13, 1, "&9&lNether", Collections.emptyList()), 27))
            .put("End", new BiomeCategory(new Item(XMaterial.END_STONE, 15, 1, "&9&lEnd", Collections.emptyList()), 27))
            .build();
    public Map<String, List<BiomeItem>> items = ImmutableMap.<String, List<BiomeItem>>builder()
            .put("Overworld", Arrays.asList(
                    new BiomeItem(
                            "&9&lPlains",
                            XMaterial.GRASS_BLOCK,
                            XBiome.PLAINS,
                            1,
                            11,
                            new Cost(100, new HashMap<>()),
                            1
                    ),
                    new BiomeItem(
                            "&9&lSnowy Plains",
                            XMaterial.SNOW_BLOCK,
                            XBiome.SNOWY_PLAINS,
                            1,
                            13,
                            new Cost(50, new HashMap<>()),
                            1
                    ),
                    new BiomeItem(
                            "&9&lSavanna",
                            XMaterial.TALL_GRASS,
                            XBiome.SAVANNA,
                            1,
                            15,
                            new Cost(100, new HashMap<>()),
                            1
                    )
            ))
            .put("Nether", Arrays.asList(
                            new BiomeItem(
                                    "&9&lNether Wastes",
                                    XMaterial.NETHERRACK,
                                    XBiome.NETHER_WASTES,
                                    1,
                                    11,
                                    new Cost(50, new HashMap<>()),
                                    1
                            ),
                            new BiomeItem(
                                    "&9&lCrimson Forest",
                                    XMaterial.CRIMSON_NYLIUM,
                                    XBiome.CRIMSON_FOREST,
                                    1,
                                    13,
                                    new Cost(1000, new HashMap<>()),
                                    1
                            ),
                            new BiomeItem(
                                    "&9&lWarped Forest",
                                    XMaterial.WARPED_NYLIUM,
                                    XBiome.WARPED_FOREST,
                                    1,
                                    15,
                                    new Cost(100, new HashMap<>()),
                                    1
                            )
                    )
            )
            .put("End", Arrays.asList(
                            new BiomeItem(
                                    "&9&lTHE END",
                                    XMaterial.END_STONE,
                                    XBiome.THE_END,
                                    1,
                                    11,
                                    new Cost(100, new HashMap<>()),
                                    1
                            ),
                            new BiomeItem(
                                    "&9&lEnd Highlands",
                                    XMaterial.PURPUR_BLOCK,
                                    XBiome.END_HIGHLANDS,
                                    1,
                                    13,
                                    new Cost(150, new HashMap<>()),
                                    1
                            ),
                            new BiomeItem(
                                    "&9&lEnd Barrens",
                                    XMaterial.ENDER_EYE,
                                    XBiome.END_BARRENS,
                                    1,
                                    15,
                                    new Cost(150, new HashMap<>()),
                                    1
                                    )
                    )
            )
            .build();
    public String buyPriceLore = "&aBuy Price: $%vault_cost%";
    public String notPurchasableLore = "&cThis item cannot be purchased!";
    public String levelRequirementLore = "&9[!] &7Must be level %level% to purchase";
    public boolean abbreviatePrices = true;
    public XSound failSound = XSound.BLOCK_ANVIL_LAND;
    public XSound successSound = XSound.ENTITY_PLAYER_LEVELUP;
    public List<String> biomeItemLore = Arrays.asList(
            "&9&l[!] " + "&9Left-Click to Purchase"
    );

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
        public int defaultAmount;
        public int slot;
        public int page;
        public Cost buyCost;
        public int minLevel;

        public BiomeItem(String name, XMaterial type, XBiome biome, int defaultAmount, int slot, Cost buyCost, int minLevel) {
            this.name = name;
            this.type = type;
            this.biome = biome;
            this.lore = Collections.emptyList();
            this.defaultAmount = defaultAmount;
            this.slot = slot;
            this.page = 1;
            this.buyCost = buyCost;
            this.minLevel = minLevel;
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
