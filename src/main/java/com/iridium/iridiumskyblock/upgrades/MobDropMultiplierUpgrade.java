package com.iridium.iridiumskyblock.upgrades;

import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnore;
import com.iridium.iridiumcore.utils.Placeholder;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Villager;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class MobDropMultiplierUpgrade extends UpgradeData {
    public int amount;

    public MobDropMultiplierUpgrade(int money, int crystals, int mobcoins, int prestigeRequired, int amount) {
        super(money, crystals, mobcoins, prestigeRequired);
        this.amount = amount;
    }

    @JsonIgnore
    @Override
    public List<Placeholder> getPlaceholders() {
        return Collections.singletonList(new Placeholder("amount", String.valueOf(amount)));
    }
}
