package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumcore.Background;
import com.iridium.iridiumcore.Item;

import java.util.Map;

public class InventoryConfig extends NoItemGUI{
    /**
     * A HashMap of the items in the GUI with the string representing the command to be executed
     */
    public Map<String, Item> items;

    public InventoryConfig(int size, String title, Background background, Map<String, Item> items) {
        this.size = size;
        this.title = title;
        this.background = background;
        this.items = items;
    }
}
