package com.iridium.iridiumskyblock.biomes;

import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnore;
import com.iridium.iridiumcore.dependencies.xseries.XBiome;

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
public class BiomeItem {

    public String name;
    public String displayName;
    public XBiome biome;
    public XMaterial item;
    public List<String> lore;
    public String command;
    public int defaultAmount;
    public int slot;
    public BuyCost buyCost;

    /**
     * A short version of the default constructor.
     *
     * @param name The name of the item in the shop
     * @param displayName The name of the item when bought
     * @param biome The biome being purchased
     * @param defaultAmount The amount of the item
     * @param slot The slot this item should be in
     * @param buyCost The cost for buying this item
     */
    public BiomeItem(String name, String displayName, XBiome biome, XMaterial item, int defaultAmount, int slot, BuyCost buyCost) {
        this.name = name;
        this.displayName = displayName;
        this.biome = biome;
        this.item = item;
        this.lore = Collections.emptyList();
        this.defaultAmount = defaultAmount;
        this.slot = slot;
        this.buyCost = buyCost;
    }

    /**
     * Returns whether this item can be purchased.
     *
     * @return True if the cost for this item is > 0
     */
    @JsonIgnore
    public boolean isPurchasable() {
        return buyCost.vault > 0 || buyCost.crystals > 0;
    }

    public XBiome getBiome() {
        return biome;
    }

    /**
     * Represents the cost of a biome.
     */
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BuyCost {

        public double vault;
        public int crystals;

    }
}
