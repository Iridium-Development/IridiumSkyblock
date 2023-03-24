package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Background;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.xseries.XBiome;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.dependencies.xseries.XSound;
import com.iridium.iridiumskyblock.biomes.BiomeCategory;
import com.iridium.iridiumskyblock.biomes.BiomeItem;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Biomes {

    public Map<String, BiomeCategoryConfig> categories = ImmutableMap.<String, BiomeCategoryConfig>builder()
            .put("Overworld", new BiomeCategoryConfig(new Item(XMaterial.GRASS_BLOCK, 11, 1, "&9&lOverworld", Collections.emptyList()), 6))
            .put("Nether", new BiomeCategoryConfig(new Item(XMaterial.NETHERRACK, 13, 1, "&9&lNether", Collections.emptyList()), 6))
            .put("End", new BiomeCategoryConfig(new Item(XMaterial.END_STONE, 15, 1, "&9&lEnd", Collections.emptyList()), 6))
            .build();

    public Map<String, List<BiomeItem>> biomes = ImmutableMap.<String, List<BiomeItem>>builder()
            .put("Overworld", Arrays.asList(
                    new BiomeItem(
                            "&9&lPlains",
                            "",
                            XBiome.PLAINS,
                            XMaterial.GRASS_BLOCK,
                            1,
                            1,
                            new BiomeItem.BuyCost(1000, 15)),
                    new BiomeItem(
                            "&9&lForest",
                            "",
                            XBiome.FOREST,
                            XMaterial.OAK_SAPLING,
                            1,
                            4,
                            new BiomeItem.BuyCost(1000, 15)),
                    new BiomeItem(
                            "&9&lSnowy Taiga",
                            "",
                            XBiome.SNOWY_TAIGA,
                            XMaterial.SPRUCE_WOOD,
                            1,
                            7,
                            new BiomeItem.BuyCost(1000, 15))))
            .put("Nether", Arrays.asList(
                    new BiomeItem(
                            "&9&lNether Wastes",
                            "",
                            XBiome.NETHER_WASTES,
                            XMaterial.NETHERRACK,
                            1,
                            1,
                            new BiomeItem.BuyCost(1000, 15)),
                    new BiomeItem(
                            "&9&lCrimson Forest",
                            "",
                            XBiome.CRIMSON_FOREST,
                            XMaterial.CRIMSON_NYLIUM,
                            1,
                            4,
                            new BiomeItem.BuyCost(1000, 15)),
                    new BiomeItem(
                            "&9&lWarped Forest",
                            "",
                            XBiome.WARPED_FOREST,
                            XMaterial.WARPED_NYLIUM,
                            1,
                            7,
                            new BiomeItem.BuyCost(1000, 15))))
            .put("End", Arrays.asList(
                    new BiomeItem(
                            "&9&lThe End",
                            "",
                            XBiome.THE_END,
                            XMaterial.END_CRYSTAL,
                            1,
                            1,
                            new BiomeItem.BuyCost(1000, 15)),
                    new BiomeItem(
                            "&9&lEnd Barrens",
                            "",
                            XBiome.END_BARRENS,
                            XMaterial.END_STONE,
                            1,
                            4,
                            new BiomeItem.BuyCost(1000, 15)),
                    new BiomeItem(
                            "&9&lEnd Highlands",
                            "",
                            XBiome.END_HIGHLANDS,
                            XMaterial.CHORUS_FRUIT,
                            1,
                            7,
                            new BiomeItem.BuyCost(1000, 15))))
            .build();

    public XSound failSound = XSound.BLOCK_ANVIL_LAND;
    public XSound successSound = XSound.ENTITY_PLAYER_LEVELUP;

    public Background overviewBackground = new Background(ImmutableMap.<Integer, Item>builder().build());
    public Background categoryBackground = new Background(ImmutableMap.<Integer, Item>builder().build());

    public List<String> biomeItemLore = Arrays.asList(" ", "&b&l[!] &bLeft-Click to Purchase");

    public String overviewTitle = "&7Island Biome Shop";
    public String categoryTitle = "&7Island Biome Shop | %biomecategory_name%";
    public String buyPriceLore = "&aBuy Price: $%buy_price_vault%, %buy_price_crystals% Crystals";
    public String notPurchasableLore = "&cThis biome cannot be purchased!";

    public boolean abbreviatePrices = true;

    public int overviewSize = 3 * 9;

    /**
     * Represents configurable options of a {@link BiomeCategory}.
     */
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BiomeCategoryConfig {
        public Item item;
        public int inventoryRows;

    }

}
