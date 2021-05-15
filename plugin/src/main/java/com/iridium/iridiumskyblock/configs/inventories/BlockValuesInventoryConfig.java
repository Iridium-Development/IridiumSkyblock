package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumskyblock.Background;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class BlockValuesInventoryConfig {
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
     * The lore of the Items
     */
    public List<String> lore;
}
