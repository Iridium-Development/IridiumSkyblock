package com.iridium.iridiumskyblock.shop;

import com.cryptomorin.xseries.XMaterial;
import java.util.List;
import lombok.AllArgsConstructor;

/**
 * Represents a category of items in the shop.
 */
@AllArgsConstructor
public class ShopCategory {

    public String name;
    public String formattedName;
    public XMaterial overviewItem;
    public List<String> itemLore;
    public List<ShopItem> items;
    public int slot;
    public int size;

}
