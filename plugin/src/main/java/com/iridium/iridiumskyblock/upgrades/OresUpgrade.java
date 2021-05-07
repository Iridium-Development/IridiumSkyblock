package com.iridium.iridiumskyblock.upgrades;

import com.cryptomorin.xseries.XMaterial;

import java.util.Map;

public class OresUpgrade extends UpgradeData {
    public Map<XMaterial, Integer> ores;

    public OresUpgrade(int money, int crystals, Map<XMaterial, Integer> ores) {
        super(money, crystals);
        this.ores = ores;
    }
}
