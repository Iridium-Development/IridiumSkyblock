package com.iridium.iridiumskyblock.enhancements;

import com.iridium.iridiumteams.enhancements.EnhancementData;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class VoidEnhancementData extends EnhancementData {
    public boolean enabled;
    public double itemLossChance;

    public VoidEnhancementData(int minLevel, int money, Map<String, Double> bankCosts, boolean enabled, double itemLossChance) {
        super(minLevel, money, bankCosts);
        this.enabled = enabled;
        this.itemLossChance = itemLossChance;
    }
}
