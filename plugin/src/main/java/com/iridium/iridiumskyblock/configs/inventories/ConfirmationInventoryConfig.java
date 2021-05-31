package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumcore.Background;
import com.iridium.iridiumcore.Item;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationInventoryConfig {
    /**
     * The size of the GUI
     */
    public int size;
    /**
     * The title of the GUI
     */
    public String title;
    /**
     * The background of the GUI
     */
    public Background background;
    /**
     * The yes item
     */
    public Item yes;
    /**
     * the no item
     */
    public Item no;
}
