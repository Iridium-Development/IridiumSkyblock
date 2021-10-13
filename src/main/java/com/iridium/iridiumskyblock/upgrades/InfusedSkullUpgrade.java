package com.iridium.iridiumskyblock.upgrades;

import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnore;
import com.iridium.iridiumcore.utils.Placeholder;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class InfusedSkullUpgrade extends UpgradeData {
    public double chance;

    public InfusedSkullUpgrade(int money, int crystals, int mobcoins, int prestigeRequired, double chance) {
        super(money, crystals, mobcoins, prestigeRequired);
        this.chance = chance;
    }

    @JsonIgnore
    @Override
    public List<Placeholder> getPlaceholders() {
        return Collections.singletonList(new Placeholder("chance", String.valueOf(chance)));
    }
}
