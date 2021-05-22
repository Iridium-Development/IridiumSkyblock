package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.entity.EntityType;

import java.util.Map;

/**
 * The block value configuration used by IridiumSkyblock (blockvalues.yml).
 * Is deserialized automatically on plugin startup and reload.
 */
public class BlockValues {

    public Map<XMaterial, ValuableBlock> blockValues = ImmutableMap.<XMaterial, ValuableBlock>builder()
            .put(XMaterial.IRON_BLOCK, new ValuableBlock(3.0, "&9&lIron Block", 10))
            .put(XMaterial.GOLD_BLOCK, new ValuableBlock(5.00, "&9&lGold Block", 11))
            .put(XMaterial.DIAMOND_BLOCK, new ValuableBlock(10.00, "&9&lDiamond Block", 12))
            .put(XMaterial.EMERALD_BLOCK, new ValuableBlock(20.00, "&9&lEmerald Block", 13))
            .put(XMaterial.NETHERITE_BLOCK, new ValuableBlock(150.00, "&9&lNetherite Block", 14))
            .put(XMaterial.HOPPER, new ValuableBlock(1.00, "&9&lHopper", 15))
            .put(XMaterial.BEACON, new ValuableBlock(150.00, "&9&lBeacon", 16))
            .build();

    public Map<EntityType, ValuableBlock> spawnerValues = ImmutableMap.<EntityType, ValuableBlock>builder()
            .put(EntityType.PIG, new ValuableBlock(100.00, "&9&lPig Spawner", 10))
            .build();

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValuableBlock {
        public double value;
        public String name;
        public int slot;
    }

}
