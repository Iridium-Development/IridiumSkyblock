package com.iridium.iridiumskyblock.upgrades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iridium.iridiumskyblock.utils.Placeholder;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class SpawnerUpgrade extends UpgradeData {
    public double modifier;

    public SpawnerUpgrade(int money, int crystals, double modifier) {
        super(money, crystals);
        this.modifier = modifier;
    }

    @JsonIgnore
    @Override
    public List<Placeholder> getPlaceholders() {
        return Collections.singletonList(new Placeholder("modifier", String.valueOf(modifier)));
    }
}
