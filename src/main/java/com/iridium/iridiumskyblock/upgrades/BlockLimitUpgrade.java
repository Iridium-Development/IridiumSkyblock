package com.iridium.iridiumskyblock.upgrades;

import com.iridium.iridiumskyblock.support.material.IridiumMaterial;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class BlockLimitUpgrade extends UpgradeData {
    public Map<IridiumMaterial, Integer> limits;

    public BlockLimitUpgrade(int money, int crystals, Map<IridiumMaterial, Integer> limits) {
        super(money, crystals);
        this.limits = limits;
    }
}
