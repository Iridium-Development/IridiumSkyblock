package com.iridium.iridiumskyblock;

import com.iridium.iridiumcore.Item;
import com.iridium.iridiumskyblock.upgrades.UpgradeData;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class Upgrade<T extends UpgradeData> {
    public boolean enabled;
    public Item item;
    public Map<Integer, T> upgrades;
}
