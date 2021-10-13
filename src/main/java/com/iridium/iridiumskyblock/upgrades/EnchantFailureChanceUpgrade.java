package com.iridium.iridiumskyblock.upgrades;

import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnore;
import com.iridium.iridiumcore.utils.Placeholder;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class EnchantFailureChanceUpgrade extends UpgradeData {
    public int amount;

    public EnchantFailureChanceUpgrade(int money, int crystals, int mobcoins, int prestigeRequired, int size) {
        super(money, crystals, mobcoins, prestigeRequired);
        this.amount = amount;
    }

    @JsonIgnore
    @Override
    public List<Placeholder> getPlaceholders() {
        return Collections.singletonList(new Placeholder("amount", String.valueOf(amount)));
    }
}
