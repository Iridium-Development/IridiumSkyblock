package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumskyblock.BackButton;
import com.iridium.iridiumcore.Background;
import com.iridium.iridiumcore.Item;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SingleItemGUI extends NoItemGUI {
    /**
     * The item for the GUI
     */
    public Item item;

    public SingleItemGUI(int size, String title, Background background, BackButton backButton, Item item) {
        super(size, title, background, backButton);
        this.item = item;
    }
}
