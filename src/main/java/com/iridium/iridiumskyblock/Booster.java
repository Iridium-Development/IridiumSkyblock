package com.iridium.iridiumskyblock;

import com.iridium.iridiumcore.Item;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Booster {
    public Item item;
    public int crystalsCost;
    public int vaultCost;
    public int time;
    public boolean enabled;
}
