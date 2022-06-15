package com.iridium.iridiumskyblock.support.material;

import java.util.Collections;
import java.util.Map;

public class Background {
    public Item filler;
    public Map<Integer, Item> items;

    public Background(Map<Integer, Item> items) {
        this.filler = new Item(IridiumMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList());
        this.items = items;
    }

    public Background(Map<Integer, Item> items, Item filler) {
        this.filler = new Item(IridiumMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList());
        this.items = items;
        this.filler = filler;
    }

    public Background() {
        this.filler = new Item(IridiumMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList());
    }
}
