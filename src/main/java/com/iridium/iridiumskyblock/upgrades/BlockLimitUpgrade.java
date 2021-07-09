package com.iridium.iridiumskyblock.upgrades;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class BlockLimitUpgrade extends UpgradeData {
    public Map<XMaterial, Integer> limits;

    public BlockLimitUpgrade(int money, int crystals, Map<XMaterial, Integer> limits) {
        super(money, crystals);
        this.limits = limits;
    }
}
