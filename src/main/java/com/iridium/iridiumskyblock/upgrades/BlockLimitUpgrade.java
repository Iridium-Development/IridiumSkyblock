package com.iridium.iridiumskyblock.upgrades;

import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class BlockLimitUpgrade extends UpgradeData {
    public Map<XMaterial, LimitedBlock> limits;
    public int page;
    public Item item;

    public BlockLimitUpgrade(int money, int crystals, Map<XMaterial, LimitedBlock> limits, int page, Item item) {
        super(money, crystals);
        this.limits = limits;
        this.page = page;
        this.item = item;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class LimitedBlock {
        public int value;
        public String name;
        public int page;
        public int slot;
    }
}
