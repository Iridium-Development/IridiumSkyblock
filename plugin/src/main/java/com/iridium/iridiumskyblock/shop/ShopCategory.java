package com.iridium.iridiumskyblock.shop;

import com.iridium.iridiumskyblock.Item;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Represents a category of items in the shop.
 */
@AllArgsConstructor
public class ShopCategory {

    public String name;
    public Item item;
    public List<ShopItem> items;
    public int size;

}
