package com.iridium.iridiumskyblock;

import com.cryptomorin.xseries.XMaterial;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Map;

@NoArgsConstructor
public class Background {

    public Item filler = new Item(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList());
    public Map<Integer, Item> items;

    public Background(Map<Integer, Item> items) {
        this.items = items;
    }

}
