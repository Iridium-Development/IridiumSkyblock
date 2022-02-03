package com.iridium.iridiumskyblock.upgrades;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class OresUpgrade extends UpgradeData {
    public Map<XMaterial, Integer> ores;
    public Map<XMaterial, Integer> netherOres;
    public Map<XMaterial, Integer> endOres;

    public OresUpgrade(int money, int crystals, Map<XMaterial, Integer> ores, Map<XMaterial, Integer> netherOres, Map<XMaterial, Integer> endOres) {
        super(money, crystals);
        this.ores = ores;
        this.netherOres = netherOres;
        this.endOres = endOres;
    }
}
