package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumcore.BackButton;
import com.iridium.iridiumcore.Background;
import com.iridium.iridiumcore.Item;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConfirmationInventoryConfig extends NoItemGUI {
    /**
     * The yes item
     */
    public Item yes;
    /**
     * the no item
     */
    public Item no;

    public ConfirmationInventoryConfig(int size, String title, Background background, BackButton backButton, Item yes, Item no) {
        super(size, title, background, backButton);
        this.yes = yes;
        this.no = no;
    }
}
