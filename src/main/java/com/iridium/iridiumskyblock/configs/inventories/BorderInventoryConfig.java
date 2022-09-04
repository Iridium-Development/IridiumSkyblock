package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumcore.Background;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumteams.configs.inventories.NoItemGUI;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BorderInventoryConfig extends NoItemGUI {
    public Item blue;
    public Item red;
    public Item green;
    public Item off;

    public BorderInventoryConfig(int size, String title, Background background, Item blue, Item red, Item green, Item off) {
        super(size, title, background);
        this.blue = blue;
        this.red = red;
        this.green = green;
        this.off = off;
    }
}
