package com.iridium.iridiumskyblock.biomes;

import com.iridium.iridiumcore.Item;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Represents a category of items in the shop.
 */
@AllArgsConstructor
public class BiomeCategory {

    public String name;
    public Item item;
    public List<BiomeItem> items;
    public int size;

}