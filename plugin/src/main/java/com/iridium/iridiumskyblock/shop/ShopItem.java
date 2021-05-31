package com.iridium.iridiumskyblock.shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * Represents an item in the shop.
 */
@AllArgsConstructor
@NoArgsConstructor
public class ShopItem {

    public String name;
    public XMaterial type;
    public List<String> lore;
    public String command;
    public int defaultAmount;
    public int slot;
    public BuyCost buyCost;
    public SellReward sellReward;

    /**
     * A short version of the default constructor.
     *
     * @param name The name of the item
     * @param type The material of the item
     * @param defaultAmount The amount of the item
     * @param slot The slot this item should be in
     * @param buyCost The cost for buying this item
     * @param sellReward The reward for selling this item
     */
    public ShopItem(String name, XMaterial type, int defaultAmount, int slot, BuyCost buyCost, SellReward sellReward) {
        this.name = name;
        this.type = type;
        this.lore = Collections.emptyList();
        this.defaultAmount = defaultAmount;
        this.slot = slot;
        this.buyCost = buyCost;
        this.sellReward = sellReward;
    }

    /**
     * Returns whether or not this item can be purchased.
     *
     * @return True if the cost for this item is > 0
     */
    @JsonIgnore
    public boolean isPurchasable() {
        return buyCost.vault > 0 || buyCost.crystals > 0;
    }

    /**
     * Returns whether or not this item can be sold.
     *
     * @return True if the reward for this item is > 0
     */
    @JsonIgnore
    public boolean isSellable() {
        return sellReward.vault > 0 || sellReward.crystals > 0;
    }

    /**
     * Represents the cost of a shop item.
     */
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BuyCost {

        public double vault;
        public int crystals;

    }

    /**
     * Represents the reward for selling an item in the shop.
     */
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SellReward {

        public double vault;
        public int crystals;

    }

}
