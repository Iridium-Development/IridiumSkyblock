package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumskyblock.BackButton;
import com.iridium.iridiumcore.Background;
import com.iridium.iridiumcore.Item;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class InventoryConfig extends NoItemGUI {
    /**
     * A HashMap of the items in the GUI with the string representing the command to be executed
     */
    public Map<String, Item> items;

    public InventoryConfig(int size, String title, Background background, BackButton backButton, Map<String, Item> items) {
        super(size, title, background, backButton);
        this.items = items;
    }
}
