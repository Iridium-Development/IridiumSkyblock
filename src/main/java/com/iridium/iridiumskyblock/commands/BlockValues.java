package com.iridium.iridiumskyblock.commands;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import org.bukkit.entity.EntityType;

import java.util.Map;

public class BlockValues {

    public Map<XMaterial, Double> blockValues = ImmutableMap.<XMaterial, Double>builder()
            .put(XMaterial.IRON_BLOCK, 3.00)
            .put(XMaterial.GOLD_BLOCK, 5.00)
            .put(XMaterial.DIAMOND_BLOCK, 10.00)
            .put(XMaterial.EMERALD_BLOCK, 20.00)
            .put(XMaterial.NETHERITE_BLOCK, 150.00)
            .put(XMaterial.HOPPER, 1.00)
            .put(XMaterial.BEACON, 150.00)
            .build();

    public Map<EntityType, Double> spawnerValues = ImmutableMap.<EntityType, Double>builder()
            .put(EntityType.PIG, 100.00)
            .build();
}
