package com.iridium.iridiumskyblock.upgrades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iridium.iridiumcore.utils.Placeholder;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class SizeUpgrade extends UpgradeData {
    public int size;

    public SizeUpgrade(int money, int crystals, int size) {
        super(money, crystals);
        this.size = size;
    }

    @JsonIgnore
    @Override
    public List<Placeholder> getPlaceholders() {
        return Arrays.asList(new Placeholder("dimensions", String.valueOf(size)), new Placeholder("size", String.valueOf(size)));
    }
}
