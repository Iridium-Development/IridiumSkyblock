package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.entity.EntityType;

import java.util.Map;

/**
 * The block value configuration used by IridiumSkyblock (blockvalues.yml).
 * Is deserialized automatically on plugin startup and reload.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockValues {

    public Map<XMaterial, ValuableBlock> blockValues = ImmutableMap.<XMaterial, ValuableBlock>builder()
            .put(XMaterial.IRON_BLOCK, new ValuableBlock(3.0, "&b&lIron Block", 1, 10))
            .put(XMaterial.GOLD_BLOCK, new ValuableBlock(5.00, "&b&lGold Block", 1, 11))
            .put(XMaterial.DIAMOND_BLOCK, new ValuableBlock(10.00, "&b&lDiamond Block", 1, 12))
            .put(XMaterial.EMERALD_BLOCK, new ValuableBlock(20.00, "&b&lEmerald Block", 1, 13))
            .put(XMaterial.NETHERITE_BLOCK, new ValuableBlock(150.00, "&b&lNetherite Block", 1, 14))
            .put(XMaterial.HOPPER, new ValuableBlock(1.00, "&b&lHopper", 1, 15))
            .put(XMaterial.BEACON, new ValuableBlock(150.00, "&b&lBeacon", 1, 16))
            .put(XMaterial.LAPIS_BLOCK, new ValuableBlock(4.0, "&b&lLapis Block", 2,11))
            .put(XMaterial.GLOWSTONE, new ValuableBlock(5.0, "&b&lGlowstone block", 2,12))
            .put(XMaterial.OBSIDIAN, new ValuableBlock(5.0, "&b&lObsidian", 2,13))
            .put(XMaterial.QUARTZ_BLOCK, new ValuableBlock(4.0, "&b&lQuartz Block", 2,14))
            .put(XMaterial.SEA_LANTERN, new ValuableBlock(4.0, "&b&lSea Lantern", 2,15))
            .build();

    public Map<EntityType, ValuableBlock> spawnerValues = ImmutableMap.<EntityType, ValuableBlock>builder()
            .put(EntityType.PIG, new ValuableBlock(100.00, "&b&lPig Spawner", 1, 10))
            .build();

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValuableBlock {
        public double value;
        public String name;
        public int page;
        public int slot;
    }

}
