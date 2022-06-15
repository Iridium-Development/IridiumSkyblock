package com.iridium.iridiumskyblock;

import com.iridium.iridiumskyblock.support.material.Item;
import com.iridium.iridiumskyblock.upgrades.UpgradeData;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class Upgrade<T extends UpgradeData> {
    public boolean enabled;
    public String name;
    public Item item;
    public Map<Integer, T> upgrades;
}
