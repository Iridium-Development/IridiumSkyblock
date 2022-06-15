package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumskyblock.support.material.Background;
import com.iridium.iridiumskyblock.support.material.Item;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;
import com.iridium.iridiumskyblock.support.material.IridiumMaterial;
import com.iridium.iridiumcore.dependencies.xseries.XSound;
import com.iridium.iridiumskyblock.shop.ShopItem;
import com.iridium.iridiumskyblock.shop.ShopItem.BuyCost;
import com.iridium.iridiumskyblock.shop.ShopItem.SellReward;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The shop configuration used by IridiumSkyblock (shop.yml).
 * Is deserialized automatically on plugin startup and reload.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Shop {

    public Map<String, ShopCategoryConfig> categories = ImmutableMap.<String, ShopCategoryConfig>builder()
            .put("Blocks", new ShopCategoryConfig(new Item(IridiumMaterial.GRASS_BLOCK, 12, 1, "&9&lBlocks", Collections.emptyList()), 6))
            .put("Food", new ShopCategoryConfig(new Item(IridiumMaterial.COOKED_CHICKEN, 13, 1, "&9&lFood", Collections.emptyList()), 4))
            .put("Ores", new ShopCategoryConfig(new Item(IridiumMaterial.GOLD_INGOT, 14, 1, "&9&lOres", Collections.emptyList()), 4))
            .put("Farming", new ShopCategoryConfig(new Item(IridiumMaterial.WHEAT, 21, 1, "&9&lFarming", Collections.emptyList()), 5))
            .put("Mob Drops", new ShopCategoryConfig(new Item(IridiumMaterial.SPIDER_EYE, 22, 1, "&9&lMob Drops", Collections.emptyList()), 5))
            .put("Miscellaneous", new ShopCategoryConfig(new Item(IridiumMaterial.SADDLE, 23, 1, "&9&lMiscellaneous", Collections.emptyList()), 4))
            .build();

    public Map<String, List<ShopItem>> items = ImmutableMap.<String, List<ShopItem>>builder()
            .put("Blocks", Arrays.asList(
                    new ShopItem(
                            "&9&lGrass Block",
                            "",
                            IridiumMaterial.GRASS_BLOCK,
                            1,
                            10,
                            new BuyCost(100, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lDirt Block",
                            "",
                            IridiumMaterial.DIRT,
                            10,
                            11,
                            new BuyCost(50, 0),
                            new SellReward(5, 0)
                    ),
                    new ShopItem(
                            "&9&lGravel",
                            "",
                            IridiumMaterial.GRAVEL,
                            10,
                            12,
                            new BuyCost(100, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lGranite",
                            "",
                            IridiumMaterial.GRANITE,
                            10,
                            13,
                            new BuyCost(100, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lDiorite",
                            "",
                            IridiumMaterial.DIORITE,
                            10,
                            14,
                            new BuyCost(100, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lAndesite",
                            "",
                            IridiumMaterial.ANDESITE,
                            10,
                            15,
                            new BuyCost(100, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lOak Log",
                            "",
                            IridiumMaterial.OAK_LOG,
                            16,
                            16,
                            new BuyCost(100, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lSpruce Log",
                            "",
                            IridiumMaterial.SPRUCE_LOG,
                            16,
                            19,
                            new BuyCost(100, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lBirch Log",
                            "",
                            IridiumMaterial.BIRCH_LOG,
                            16,
                            20,
                            new BuyCost(100, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lJungle Log",
                            "",
                            IridiumMaterial.JUNGLE_LOG,
                            16,
                            21,
                            new BuyCost(100, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lAcacia Log",
                            "",
                            IridiumMaterial.ACACIA_LOG,
                            16,
                            22,
                            new BuyCost(100, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lDark Oak Log",
                            "",
                            IridiumMaterial.DARK_OAK_LOG,
                            16,
                            23,
                            new BuyCost(100, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lSnow Block",
                            "",
                            IridiumMaterial.SNOW_BLOCK,
                            16,
                            24,
                            new BuyCost(200, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lIce",
                            "",
                            IridiumMaterial.ICE,
                            8,
                            25,
                            new BuyCost(300, 0),
                            new SellReward(20, 0)
                    ),
                    new ShopItem(
                            "&9&lPacked Ice",
                            "",
                            IridiumMaterial.PACKED_ICE,
                            8,
                            28,
                            new BuyCost(300, 0),
                            new SellReward(20, 0)
                    ),
                    new ShopItem(
                            "&9&lSponge",
                            "",
                            IridiumMaterial.SPONGE,
                            4,
                            29,
                            new BuyCost(1000, 0),
                            new SellReward(200, 0)
                    ),
                    new ShopItem(
                            "&9&lSand",
                            "",
                            IridiumMaterial.SAND,
                            8,
                            30,
                            new BuyCost(100, 0),
                            new SellReward(20, 0)
                    ),
                    new ShopItem(
                            "&9&lSandstone",
                            "",
                            IridiumMaterial.SANDSTONE,
                            16,
                            31,
                            new BuyCost(80, 0),
                            new SellReward(5, 0)
                    ),
                    new ShopItem(
                            "&9&lClay Ball",
                            "",
                            IridiumMaterial.CLAY_BALL,
                            32,
                            32,
                            new BuyCost(70, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lObsidian",
                            "",
                            IridiumMaterial.OBSIDIAN,
                            4,
                            33,
                            new BuyCost(250, 0),
                            new SellReward(25, 0)
                    ),
                    new ShopItem(
                            "&9&lGlowstone",
                            "",
                            IridiumMaterial.GLOWSTONE,
                            8,
                            34,
                            new BuyCost(125, 0),
                            new SellReward(15, 0)
                    ),
                    new ShopItem(
                            "&9&lEnd Stone",
                            "",
                            IridiumMaterial.END_STONE,
                            4,
                            39,
                            new BuyCost(250, 0),
                            new SellReward(25, 0)
                    ),
                    new ShopItem(
                            "&9&lPrismarine",
                            "",
                            IridiumMaterial.PRISMARINE,
                            16,
                            40,
                            new BuyCost(200, 0),
                            new SellReward(20, 0)
                    ),
                    new ShopItem(
                            "&9&lWool",
                            "",
                            IridiumMaterial.WHITE_WOOL,
                            8,
                            41,
                            new BuyCost(50, 0),
                            new SellReward(5, 0)
                    )
            ))
            .put("Food", Arrays.asList(
                    new ShopItem(
                            "&9&lApple",
                            "",
                            IridiumMaterial.APPLE,
                            10,
                            11,
                            new BuyCost(50, 0),
                            new SellReward(15, 0)
                    ),
                    new ShopItem(
                            "&9&lEnchanted Golden Apple",
                            "",
                            IridiumMaterial.ENCHANTED_GOLDEN_APPLE,
                            3,
                            12,
                            new BuyCost(1000, 0),
                            new SellReward(100, 0)
                    ),
                    new ShopItem(
                            "&9&lCarrot",
                            "",
                            IridiumMaterial.CARROT,
                            10,
                            13,
                            new BuyCost(100, 0),
                            new SellReward(25, 0)
                    ),
                    new ShopItem(
                            "&9&lBaked Potato",
                            "",
                            IridiumMaterial.BAKED_POTATO,
                            10,
                            14,
                            new BuyCost(150, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lBread",
                            "",
                            IridiumMaterial.BREAD,
                            10,
                            15,
                            new BuyCost(50, 0),
                            new SellReward(30, 0)
                    ),
                    new ShopItem(
                            "&9&lCookie",
                            "",
                            IridiumMaterial.COOKIE,
                            5,
                            20,
                            new BuyCost(130, 0),
                            new SellReward(5, 0)
                    ),
                    new ShopItem(
                            "&9&lCooked Porkchop",
                            "",
                            IridiumMaterial.COOKED_PORKCHOP,
                            10,
                            21,
                            new BuyCost(100, 0),
                            new SellReward(15, 0)
                    ),
                    new ShopItem(
                            "&9&lCooked Beef",
                            "",
                            IridiumMaterial.COOKED_BEEF,
                            10,
                            22,
                            new BuyCost(100, 0),
                            new SellReward(15, 0)
                    ),
                    new ShopItem(
                            "&9&lCooked Mutton",
                            "",
                            IridiumMaterial.COOKED_MUTTON,
                            10,
                            23,
                            new BuyCost(100, 0),
                            new SellReward(20, 0)
                    ),
                    new ShopItem(
                            "&9&lCooked Rabbit",
                            "",
                            IridiumMaterial.COOKED_RABBIT,
                            10,
                            24,
                            new BuyCost(100, 0),
                            new SellReward(25, 0)
                    )
                    )
            )
            .put("Ores", Arrays.asList(
                    new ShopItem(
                            "&9&lCoal",
                            "",
                            IridiumMaterial.COAL,
                            16,
                            11,
                            new BuyCost(100, 0),
                            new SellReward(15, 0)
                    ),
                    new ShopItem(
                            "&9&lRedstone",
                            "",
                            IridiumMaterial.REDSTONE,
                            16,
                            12,
                            new BuyCost(150, 0),
                            new SellReward(7, 0)
                    ),
                    new ShopItem(
                            "&9&lLapis Lazuli",
                            "",
                            IridiumMaterial.LAPIS_LAZULI,
                            16,
                            13,
                            new BuyCost(150, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lIron Ingot",
                            "",
                            IridiumMaterial.IRON_INGOT,
                            8,
                            14,
                            new BuyCost(200, 0),
                            new SellReward(20, 0)
                    ),
                    new ShopItem(
                            "&9&lGold Ingot",
                            "",
                            IridiumMaterial.GOLD_INGOT,
                            8,
                            15,
                            new BuyCost(200, 0),
                            new SellReward(20, 0)
                    ),
                    new ShopItem(
                            "&9&lDiamond",
                            "",
                            IridiumMaterial.DIAMOND,
                            8,
                            21,
                            new BuyCost(1000, 0),
                            new SellReward(100, 0)
                    ),
                    new ShopItem(
                            "&9&lEmerald",
                            "",
                            IridiumMaterial.EMERALD,
                            8,
                            22,
                            new BuyCost(200, 0),
                            new SellReward(15, 0)
                    ),
                    new ShopItem(
                            "&9&lQuartz",
                            "",
                            IridiumMaterial.QUARTZ,
                            64,
                            23,
                            new BuyCost(100, 0),
                            new SellReward(10, 0)
                    )
                    )
            )
            .put("Farming", Arrays.asList(
                    new ShopItem(
                            "&9&lWheat Seeds",
                            "",
                            IridiumMaterial.WHEAT_SEEDS,
                            16,
                            10,
                            new BuyCost(130, 0),
                            new SellReward(2, 0)
                    ),
                    new ShopItem(
                            "&9&lPumpkin Seeds",
                            "",
                            IridiumMaterial.PUMPKIN_SEEDS,
                            16,
                            11,
                            new BuyCost(150, 0),
                            new SellReward(5, 0)
                    ),
                    new ShopItem(
                            "&9&lMelon Seeds",
                            "",
                            IridiumMaterial.MELON_SEEDS,
                            16,
                            12,
                            new BuyCost(250, 0),
                            new SellReward(5, 0)
                    ),
                    new ShopItem(
                            "&9&lNether Wart",
                            "",
                            IridiumMaterial.NETHER_WART,
                            4,
                            13,
                            new BuyCost(100, 0),
                            new SellReward(5, 0)
                    ),
                    new ShopItem(
                            "&9&lSugar Cane",
                            "",
                            IridiumMaterial.SUGAR_CANE,
                            16,
                            14,
                            new BuyCost(150, 0),
                            new SellReward(5, 0)
                    ),
                    new ShopItem(
                            "&9&lWheat",
                            "",
                            IridiumMaterial.WHEAT,
                            16,
                            15,
                            new BuyCost(50, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lPumpkin",
                            "",
                            IridiumMaterial.PUMPKIN,
                            16,
                            16,
                            new BuyCost(150, 0),
                            new SellReward(15, 0)
                    ),
                    new ShopItem(
                            "&9&lMelon Slice",
                            "",
                            IridiumMaterial.MELON_SLICE,
                            16,
                            19,
                            new BuyCost(150, 0),
                            new SellReward(5, 0)
                    ),
                    new ShopItem(
                            "&9&lCactus",
                            "",
                            IridiumMaterial.CACTUS,
                            8,
                            20,
                            new BuyCost(80, 0),
                            new SellReward(2, 0)
                    ),
                    new ShopItem(
                            "&9&lOak Sapling",
                            "",
                            IridiumMaterial.OAK_SAPLING,
                            4,
                            21,
                            new BuyCost(20, 0),
                            new SellReward(2, 0)
                    ),
                    new ShopItem(
                            "&9&lSpruce Sapling",
                            "",
                            IridiumMaterial.SPRUCE_SAPLING,
                            4,
                            22,
                            new BuyCost(20, 0),
                            new SellReward(2, 0)
                    ),
                    new ShopItem(
                            "&9&lBirch Sapling",
                            "",
                            IridiumMaterial.BIRCH_SAPLING,
                            4,
                            23,
                            new BuyCost(20, 0),
                            new SellReward(2, 0)
                    ),
                    new ShopItem(
                            "&9&lJungle Sapling",
                            "",
                            IridiumMaterial.JUNGLE_SAPLING,
                            4,
                            24,
                            new BuyCost(150, 0),
                            new SellReward(4, 0)
                    ),
                    new ShopItem(
                            "&9&lAcacia Sapling",
                            "",
                            IridiumMaterial.ACACIA_SAPLING,
                            4,
                            25,
                            new BuyCost(20, 0),
                            new SellReward(2, 0)
                    ),
                    new ShopItem(
                            "&9&lDark Oak Sapling",
                            "",
                            IridiumMaterial.DARK_OAK_SAPLING,
                            4,
                            30,
                            new BuyCost(150, 0),
                            new SellReward(4, 0)
                    ),
                    new ShopItem(
                            "&9&lBrown Mushroom",
                            "",
                            IridiumMaterial.BROWN_MUSHROOM,
                            8,
                            31,
                            new BuyCost(60, 0),
                            new SellReward(6, 0)
                    ),
                    new ShopItem(
                            "&9&lRed Mushroom",
                            "",
                            IridiumMaterial.RED_MUSHROOM,
                            8,
                            32,
                            new BuyCost(60, 0),
                            new SellReward(6, 0)
                    )
                    )
            )
            .put("Mob Drops", Arrays.asList(
                    new ShopItem(
                            "&9&lRotten Flesh",
                            "",
                            IridiumMaterial.ROTTEN_FLESH,
                            16,
                            10,
                            new BuyCost(20, 0),
                            new SellReward(2, 0)
                    ),
                    new ShopItem(
                            "&9&lBone",
                            "",
                            IridiumMaterial.BONE,
                            16,
                            11,
                            new BuyCost(100, 0),
                            new SellReward(3, 0)
                    ),
                    new ShopItem(
                            "&9&lGunpowder",
                            "",
                            IridiumMaterial.GUNPOWDER,
                            16,
                            12,
                            new BuyCost(30, 0),
                            new SellReward(3, 0)
                    ),
                    new ShopItem(
                            "&9&lString",
                            "",
                            IridiumMaterial.STRING,
                            16,
                            13,
                            new BuyCost(80, 0),
                            new SellReward(3, 0)
                    ),
                    new ShopItem(
                            "&9&lArrow",
                            "",
                            IridiumMaterial.ARROW,
                            16,
                            14,
                            new BuyCost(75, 0),
                            new SellReward(4, 0)
                    ),
                    new ShopItem(
                            "&9&lSpider Eye",
                            "",
                            IridiumMaterial.SPIDER_EYE,
                            16,
                            15,
                            new BuyCost(50, 0),
                            new SellReward(5, 0)
                    ),
                    new ShopItem(
                            "&9&lEnder Pearl",
                            "",
                            IridiumMaterial.ENDER_PEARL,
                            3,
                            16,
                            new BuyCost(75, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lSlime Ball",
                            "",
                            IridiumMaterial.SLIME_BALL,
                            16,
                            19,
                            new BuyCost(200, 0),
                            new SellReward(5, 0)
                    ),
                    new ShopItem(
                            "&9&lPrismarine Crystals",
                            "",
                            IridiumMaterial.PRISMARINE_CRYSTALS,
                            16,
                            20,
                            new BuyCost(50, 0),
                            new SellReward(5, 0)
                    ),
                    new ShopItem(
                            "&9&lPrismarine Shard",
                            "",
                            IridiumMaterial.PRISMARINE_SHARD,
                            16,
                            21,
                            new BuyCost(50, 0),
                            new SellReward(5, 0)
                    ),
                    new ShopItem(
                            "&9&lBlaze Rod",
                            "",
                            IridiumMaterial.BLAZE_ROD,
                            4,
                            22,
                            new BuyCost(250, 0),
                            new SellReward(20, 0)
                    ),
                    new ShopItem(
                            "&9&lMagma Cream",
                            "",
                            IridiumMaterial.MAGMA_CREAM,
                            4,
                            23,
                            new BuyCost(150, 0),
                            new SellReward(15, 0)
                    ),
                    new ShopItem(
                            "&9&lGhast Tear",
                            "",
                            IridiumMaterial.GHAST_TEAR,
                            4,
                            24,
                            new BuyCost(200, 0),
                            new SellReward(30, 0)
                    ),
                    new ShopItem(
                            "&9&lLeather",
                            "",
                            IridiumMaterial.LEATHER,
                            8,
                            25,
                            new BuyCost(50, 0),
                            new SellReward(5, 0)
                    ),
                    new ShopItem(
                            "&9&lRabbit Foot",
                            "",
                            IridiumMaterial.RABBIT_FOOT,
                            4,
                            30,
                            new BuyCost(250, 0),
                            new SellReward(30, 0)
                    ),
                    new ShopItem(
                            "&9&lInk Sack",
                            "",
                            IridiumMaterial.INK_SAC,
                            8,
                            31,
                            new BuyCost(50, 0),
                            new SellReward(5, 0)
                    ),
                    new ShopItem(
                            "&9&lFeather",
                            "",
                            IridiumMaterial.FEATHER,
                            16,
                            32,
                            new BuyCost(30, 0),
                            new SellReward(3, 0)
                    )
                    )
            )
            .put("Miscellaneous", Arrays.asList(
                    new ShopItem(
                            "&9&lBucket",
                            "",
                            IridiumMaterial.BUCKET,
                            1,
                            12,
                            new BuyCost(100, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lWater Bucket",
                            "",
                            IridiumMaterial.WATER_BUCKET,
                            1,
                            13,
                            new BuyCost(200, 0),
                            new SellReward(10, 0)
                    ),
                    new ShopItem(
                            "&9&lLava Bucket",
                            "",
                            IridiumMaterial.LAVA_BUCKET,
                            1,
                            14,
                            new BuyCost(200, 0),
                            new SellReward(20, 0)
                    ),
                    new ShopItem(
                            "&9&lName Tag",
                            "",
                            IridiumMaterial.NAME_TAG,
                            1,
                            21,
                            new BuyCost(200, 0),
                            new SellReward(30, 0)
                    ),
                    new ShopItem(
                            "&9&lSaddle",
                            "",
                            IridiumMaterial.SADDLE,
                            1,
                            22,
                            new BuyCost(300, 0),
                            new SellReward(30, 0)
                    ),
                    new ShopItem(
                            "&9&lEnd Portal Frame",
                            "",
                            IridiumMaterial.END_PORTAL_FRAME,
                            Arrays.asList("&5&lVisit the end!", " "),
                            null,
                            1,
                            23,
                            new BuyCost(5000, 50),
                            new SellReward(0, 0)
                    )
                    )
            )
            .build();

    public String overviewTitle = "&7Island Shop";
    public String categoryTitle = "&7Island Shop | %category_name%";
    public String buyPriceLore = "&aBuy Price: $%buy_price_vault%, %buy_price_crystals% Crystals";
    public String sellRewardLore = "&cSelling Reward: $%sell_reward_vault%, %sell_reward_crystals% Crystals";
    public String notPurchasableLore = "&cThis item cannot be purchased!";
    public String notSellableLore = "&cThis item cannot be sold!";

    public boolean abbreviatePrices = true;
    public boolean dropItemWhenFull = false;

    public int overviewSize = 4 * 9;

    public XSound failSound = XSound.BLOCK_ANVIL_LAND;
    public XSound successSound = XSound.ENTITY_PLAYER_LEVELUP;

    public Background overviewBackground = new Background(ImmutableMap.<Integer, Item>builder().build());
    public Background categoryBackground = new Background(ImmutableMap.<Integer, Item>builder().build());

    public List<String> shopItemLore = Arrays.asList(" ", "&b&l[!] &bLeft-Click to Purchase %amount%, Shift for 64", "&b&l[!] &bRight Click to Sell %amount%, Shift for 64");

    /**
     * Represents configurable options of a {@link com.iridium.iridiumskyblock.shop.ShopCategory}.
     */
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShopCategoryConfig {
        public Item item;
        public int inventoryRows;

    }

}
