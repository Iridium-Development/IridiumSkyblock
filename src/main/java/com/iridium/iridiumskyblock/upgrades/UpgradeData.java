package com.iridium.iridiumskyblock.upgrades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iridium.iridiumcore.utils.Placeholder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class UpgradeData {
    public int money;
    public int crystals;

    @JsonIgnore
    public List<Placeholder> getPlaceholders() {
        return Collections.emptyList();
    }
}
