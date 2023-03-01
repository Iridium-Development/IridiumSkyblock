package com.iridium.iridiumskyblock.enhancements;

import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumteams.enhancements.EnhancementData;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class SizeEnhancementData extends EnhancementData {
    public int size;

    public SizeEnhancementData(int minLevel, int money, Map<String, Double> bankCosts, int size) {
        super(minLevel, money, bankCosts);
        this.size = size;
    }

    @Override
    public List<Placeholder> getPlaceholders() {
        return Collections.singletonList(
                new Placeholder("size", String.valueOf(size))
        );
    }
}
