package com.iridium.iridiumskyblock;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class InventoryConfig {
    /**
     * The size of the GUI
     */
    public int size;
    /**
     * The title of the GUI
     */
    public String title;
    /**
     * A HashMap of the items in the GUI with the string representing the command to be executed
     */
    public Map<String, Item> items;
}
