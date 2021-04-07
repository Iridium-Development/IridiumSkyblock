package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;
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

    public Map<XMaterial, String> blockNames = ImmutableMap.<XMaterial, String>builder()
            .put(XMaterial.IRON_BLOCK, "&b&lIron Block")
            .put(XMaterial.GOLD_BLOCK, "&b&lGold Block")
            .put(XMaterial.DIAMOND_BLOCK, "&b&lDiamond Block")
            .put(XMaterial.EMERALD_BLOCK, "&b&lEmerald Block")
            .put(XMaterial.NETHERITE_BLOCK, "&b&lNetherite Block")
            .put(XMaterial.HOPPER, "&b&lHopper")
            .put(XMaterial.BEACON, "&b&lBeacon")
            .build();

    public List<Integer> blockSlots = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 17);

    public Map<EntityType, Double> spawnerValues = ImmutableMap.<EntityType, Double>builder()
            .put(EntityType.PIG, 100.00)
            .build();

    public Map<EntityType, String> spawnerNames = ImmutableMap.<EntityType, String>builder()
            .put(EntityType.PIG, "&b&lPig Spawner")
            .build();

    public List<Integer> spawnerSlots = Arrays.asList(10, 11, 12, 13, 14, 15, 16);

}
