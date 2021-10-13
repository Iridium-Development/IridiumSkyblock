package com.iridium.iridiumskyblock.upgrades;

import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnore;
import com.iridium.iridiumcore.utils.Placeholder;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class AutoSellChestUpgrade extends UpgradeData {

    public int sellInterval;
    public int sellValue;

    public AutoSellChestUpgrade(int money, int crystals, int mobcoins, int prestigeRequired, int sellInterval, int sellValue) {
        super(money, crystals, mobcoins, prestigeRequired);
        this.sellInterval = sellInterval;
        this.sellValue = sellValue;
    }

    @JsonIgnore
    @Override
    public List<Placeholder> getPlaceholders() {
        return Arrays.asList(new Placeholder("sellInterval", String.valueOf(sellInterval)), new Placeholder("sellValue", String.valueOf(sellValue)));
    }
}
