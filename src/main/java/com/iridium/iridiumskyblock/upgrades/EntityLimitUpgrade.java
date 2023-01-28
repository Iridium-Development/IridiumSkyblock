package com.iridium.iridiumskyblock.upgrades;

import lombok.NoArgsConstructor;
import org.bukkit.entity.EntityType;

import java.util.Map;

@NoArgsConstructor
public class EntityLimitUpgrade extends UpgradeData {
    public Map<EntityType, Integer> limits;

    public EntityLimitUpgrade(int money, int crystals, Map<EntityType, Integer> limits) {
        super(money, crystals);
        this.limits = limits;
    }
}
