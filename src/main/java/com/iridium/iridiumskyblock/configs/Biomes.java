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

    public Map<String, BiomeCategory> categories = ImmutableMap.<String, BiomeCategory>builder()
            .put("Overworld", new BiomeCategory(new Item(XMaterial.GRASS_BLOCK, 11, 1, "&9&lOverworld", Collections.emptyList()), 54))
            .put("Nether", new BiomeCategory(new Item(XMaterial.CRIMSON_NYLIUM, 13, 1, "&9&lNether", Collections.emptyList()), 36))
            .put("End", new BiomeCategory(new Item(XMaterial.END_STONE, 15, 1, "&9&lEnd", Collections.emptyList()), 36))
            .build();
    public Map<String, List<BiomeItem>> items = ImmutableMap.<String, List<BiomeItem>>builder()
            .put("Overworld", Arrays.asList(
                    new BiomeItem(
                            "&9&lPlains",
                            XMaterial.GRASS_BLOCK,
                            XBiome.PLAINS,
                            1,
                            20,
                            new Cost(100, new HashMap<>())
                    ),
                    new BiomeItem(
                            "&9&lSnowy Plains",
                            XMaterial.SNOW_BLOCK,
                            XBiome.SNOWY_PLAINS,
                            1,
                            22,
                            new Cost(50, new HashMap<>())
                    ),
                    new BiomeItem(
                            "&9&lSavanna",
                            XMaterial.TALL_GRASS,
                            XBiome.SAVANNA,
                            1,
                            24,
                            new Cost(100, new HashMap<>())
                    )
            ))
            .put("Nether", Arrays.asList(
                            new BiomeItem(
                                    "&9&lNether Wastes",
                                    XMaterial.NETHERRACK,
                                    XBiome.NETHER_WASTES,
                                    1,
                                    20,
                                    new Cost(50, new HashMap<>())
                            ),
                            new BiomeItem(
                                    "&9&lCrimson Forest",
                                    XMaterial.CRIMSON_NYLIUM,
                                    XBiome.CRIMSON_FOREST,
                                    1,
                                    22,
                                    new Cost(1000, new HashMap<>())
                            ),
                            new BiomeItem(
                                    "&9&lWarped Forest",
                                    XMaterial.WARPED_NYLIUM,
                                    XBiome.WARPED_FOREST,
                                    1,
                                    24,
                                    new Cost(100, new HashMap<>())
                            )
                    )
            )
            .put("End", Arrays.asList(
                            new BiomeItem(
                                    "&9&lTHE END",
                                    XMaterial.END_STONE,
                                    XBiome.THE_END,
                                    1,
                                    20,
                                    new Cost(100, new HashMap<>())
                            ),
                            new BiomeItem(
                                    "&9&lEnd Highlands",
                                    XMaterial.PURPUR_BLOCK,
                                    XBiome.END_HIGHLANDS,
                                    1,
                                    22,
                                    new Cost(150, new HashMap<>())
                            ),
                            new BiomeItem(
                                    "&9&lEnd Barrens",
                                    XMaterial.ENDER_EYE,
                                    XBiome.END_BARRENS,
                                    1,
                                    24,
                                    new Cost(150, new HashMap<>())
                            )
                    )
            )
            .build();
    public String buyPriceLore = "&aBuy Price: $%vault_cost%";
    public String notPurchasableLore = "&cThis item cannot be purchased!";
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

        public BiomeItem(String name, XMaterial type, XBiome biome, int defaultAmount, int slot, Cost buyCost) {
            this.name = name;
            this.type = type;
            this.biome = biome;
            this.lore = Collections.emptyList();
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
