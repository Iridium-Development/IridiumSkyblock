package com.iridium.iridiumskyblock.upgrades;

import com.iridium.iridiumskyblock.support.material.IridiumMaterial;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class OresUpgrade extends UpgradeData {
    public Map<IridiumMaterial, Integer> ores;
    public Map<IridiumMaterial, Integer> netherOres;

    public OresUpgrade(int money, int crystals, Map<IridiumMaterial, Integer> ores, Map<IridiumMaterial, Integer> netherOres) {
        super(money, crystals);
        this.ores = ores;
        this.netherOres = netherOres;
    }
}
