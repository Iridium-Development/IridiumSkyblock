package com.iridium.iridiumskyblock.shop;

import com.cryptomorin.xseries.XMaterial;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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

    public ShopItem(String name, XMaterial type, int defaultAmount, int slot, BuyCost buyCost, SellReward sellReward) {
        this.name = name;
        this.type = type;
        this.lore = Collections.emptyList();
        this.defaultAmount = defaultAmount;
        this.slot = slot;
        this.buyCost = buyCost;
        this.sellReward = sellReward;
    }

    @JsonIgnore
    public boolean isPurchasable() {
        return buyCost.vault != 0 || buyCost.crystals != 0;
    }

    @JsonIgnore
    public boolean isSellable() {
        return sellReward.vault != 0 || sellReward.crystals != 0;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class BuyCost {

        public double vault;
        public int crystals;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class SellReward {

        public double vault;
        public int crystals;

    }

}
