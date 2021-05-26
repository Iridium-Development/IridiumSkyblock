package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumskyblock.Background;
import com.iridium.iridiumskyblock.Item;
import com.iridium.iridiumskyblock.shop.ShopItem;
import com.iridium.iridiumskyblock.shop.ShopItem.BuyCost;
import com.iridium.iridiumskyblock.shop.ShopItem.SellReward;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class Shop {

    public Map<String, ShopCategoryConfig> categories = ImmutableMap.<String, ShopCategoryConfig>builder()
        .put("Test", new ShopCategoryConfig("&cTest", XMaterial.COMMAND_BLOCK, null, 0))
        .build();

    public Map<String, List<ShopItem>> items = ImmutableMap.<String, List<ShopItem>>builder()
        .put(
            "Test",
            Arrays.asList(
                new ShopItem(
                    "Test-Item 1",
                    XMaterial.DIRT,
                    Collections.emptyList(),
                    "say Hello %player% x%amount%!",
                    1,
                    0,
                    new BuyCost(100, 10),
                    new SellReward(10, 3)
                ),
                new ShopItem(
                    "Test-Item 2",
                    XMaterial.COMMAND_BLOCK,
                    5,
                    1,
                    new BuyCost(10000, 1000000),
                    new SellReward(0, 0)
                )
            ))
        .build();

    public String overviewTitle = "&7Island Shop";
    public String categoryTitle = "&7Island Shop | %category_name%";
    public String buyPriceLore = "&aBuy Price: $%buy_price_vault%, %buy_price_crystals% Crystals";
    public String sellRewardLore = "&cSelling Reward: $%sell_reward_vault%, %sell_reward_crystals% Crystals";
    public String notPurchasableLore = "&cThis item cannot be purchased!";
    public String notSellableLore = "&cThis item cannot be sold!";

    public int overviewSize = 4 * 9;
    public int categorySize = 6 * 9;

    public Background overviewBackground = new Background(ImmutableMap.<Integer, Item>builder().build());
    public Background categoryBackground = new Background(ImmutableMap.<Integer, Item>builder().build());

    public List<String> shopItemLore = Arrays.asList(" ", "&b[!] Left-Click to Purchase %amount%, Shift for 64", "&b[!] Right Click to Sell %amount%, Shift for 64");

    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShopCategoryConfig {

        public String formattedName;
        public XMaterial representativeItem;
        public List<String> lore;
        public int slot;

    }

}
