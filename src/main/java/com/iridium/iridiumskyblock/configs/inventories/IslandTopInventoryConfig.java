package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumskyblock.Item;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class IslandTopInventoryConfig {
    /**
     * The size of the GUI
     */
    public int size;
    /**
     * The title of the GUI
     */
    public String title;
    /**
     * The item for top islands
     */
    public Item item;
    /**
     * The filler item if there isnt a # top island
     */
    public Item filler;
}
