package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumskyblock.BackButton;
import com.iridium.iridiumcore.Background;
import com.iridium.iridiumcore.Item;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IslandTopInventoryConfig extends NoItemGUI {
    /**
     * The item for top islands
     */
    public Item item;
    /**
     * The filler item if there isnt a # top island
     */
    public Item filler;

    public IslandTopInventoryConfig(int size, String title, Background background, BackButton backButton, Item item, Item filler) {
        super(size, title, background, backButton);
        this.item = item;
        this.filler = filler;
    }
}
