package com.iridium.iridiumskyblock.upgrades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iridium.iridiumcore.utils.Placeholder;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class WarpsUpgrade extends UpgradeData {
    public int amount;

    public WarpsUpgrade(int money, int crystals, int amount) {
        super(money, crystals);
        this.amount = amount;
    }

    @JsonIgnore
    @Override
    public List<Placeholder> getPlaceholders() {
        return Collections.singletonList(new Placeholder("amount", String.valueOf(amount)));
    }
}
