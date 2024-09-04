package com.iridium.iridiumskyblock.enhancements;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumteams.enhancements.EnhancementData;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class GeneratorEnhancementData extends EnhancementData {
    public Map<XMaterial, Integer> ores;
    public Map<XMaterial, Integer> netherOres;

    public GeneratorEnhancementData(int minLevel, int money, Map<String, Double> bankCosts, Map<XMaterial, Integer> ores, Map<XMaterial, Integer> netherOres) {
        super(minLevel, money, bankCosts);
        this.ores = ores;
        this.netherOres = netherOres;
    }
}
