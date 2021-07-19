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
            .put(XMaterial.IRON_BLOCK, new ValuableBlock(3.0, "&b&lIron Block"))
            .put(XMaterial.GOLD_BLOCK, new ValuableBlock(5.00, "&b&lGold Block"))
            .put(XMaterial.DIAMOND_BLOCK, new ValuableBlock(10.00, "&b&lDiamond Block"))
            .put(XMaterial.EMERALD_BLOCK, new ValuableBlock(20.00, "&b&lEmerald Block"))
            .put(XMaterial.NETHERITE_BLOCK, new ValuableBlock(150.00, "&b&lNetherite Block"))
            .put(XMaterial.HOPPER, new ValuableBlock(1.00, "&b&lHopper"))
            .put(XMaterial.BEACON, new ValuableBlock(150.00, "&b&lBeacon"))
            .build();

    public Map<EntityType, ValuableBlock> spawnerValues = ImmutableMap.<EntityType, ValuableBlock>builder()
            .put(EntityType.PIG, new ValuableBlock(100.00, "&b&lPig Spawner"))
            .build();

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValuableBlock {
        public double value;
        public String name;
    }

}
