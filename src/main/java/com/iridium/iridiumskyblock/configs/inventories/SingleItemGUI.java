package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumskyblock.support.material.Background;
import com.iridium.iridiumskyblock.support.material.Item;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SingleItemGUI extends NoItemGUI{
    /**
     * The item for the GUI
     */
    public Item item;

    public SingleItemGUI(int size, String title, Background background, Item item) {
        this.size = size;
        this.title = title;
        this.background = background;
        this.item = item;
    }
}
