package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumcore.Background;
import com.iridium.iridiumcore.Item;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConfirmationInventoryConfig extends NoItemGUI{
    /**
     * The yes item
     */
    public Item yes;
    /**
     * the no item
     */
    public Item no;

    public ConfirmationInventoryConfig(int size, String title, Background background, Item yes, Item no) {
        this.size = size;
        this.title = title;
        this.background = background;
        this.yes = yes;
        this.no = no;
    }
}
