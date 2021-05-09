package com.iridium.iridiumskyblock.upgrades;

import com.cryptomorin.xseries.XMaterial;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class OresUpgrade extends UpgradeData {
    public Map<XMaterial, Integer> ores;
    public Map<XMaterial, Integer> netherOres;

    public OresUpgrade(int money, int crystals, Map<XMaterial, Integer> ores, Map<XMaterial, Integer> netherOres) {
        super(money, crystals);
        this.ores = ores;
        this.netherOres = netherOres;
    }
}
