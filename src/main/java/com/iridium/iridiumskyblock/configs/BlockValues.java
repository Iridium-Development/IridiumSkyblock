package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;
import com.iridium.iridiumskyblock.support.material.IridiumMaterial;
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

    public Map<IridiumMaterial, ValuableBlock> blockValues = ImmutableMap.<IridiumMaterial, ValuableBlock>builder()
            .put(IridiumMaterial.IRON_BLOCK, new ValuableBlock(3.0, "&b&lIron Block", 1, 10))
            .put(IridiumMaterial.GOLD_BLOCK, new ValuableBlock(5.00, "&b&lGold Block", 1, 11))
            .put(IridiumMaterial.DIAMOND_BLOCK, new ValuableBlock(10.00, "&b&lDiamond Block", 1, 12))
            .put(IridiumMaterial.EMERALD_BLOCK, new ValuableBlock(20.00, "&b&lEmerald Block", 1, 13))
            .put(IridiumMaterial.NETHERITE_BLOCK, new ValuableBlock(150.00, "&b&lNetherite Block", 1, 14))
            .put(IridiumMaterial.HOPPER, new ValuableBlock(1.00, "&b&lHopper", 1, 15))
            .put(IridiumMaterial.BEACON, new ValuableBlock(150.00, "&b&lBeacon", 1, 16))
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
