package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumskyblock.Item;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class SingleItemGUI {
    /**
     * The size of the GUI
     */
    public int size;
    /**
     * The title for the GUI
     */
    public String title;
    /**
     * The item for the GUI
     */
    public Item item;
}
