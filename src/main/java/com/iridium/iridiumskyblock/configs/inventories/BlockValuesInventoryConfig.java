package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumcore.Background;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class BlockValuesInventoryConfig extends NoItemGUI {
    /**
     * The lore of the Items
     */
    public List<String> lore;

    public BlockValuesInventoryConfig(int size, String title, Background background, List<String> lore) {
        this.size = size;
        this.title = title;
        this.background = background;
        this.lore = lore;
    }
}
