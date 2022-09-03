package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.enhancements.*;
import com.iridium.iridiumteams.enhancements.Enhancement;
import com.iridium.iridiumteams.enhancements.EnhancementType;

import java.util.Arrays;
import java.util.HashMap;

public class Enhancements extends com.iridium.iridiumteams.configs.Enhancements {

    public Enhancements() {
        super("&9");
    }

    public Enhancement<SizeEnhancementData> sizeEnhancement = new Enhancement<>(true, EnhancementType.UPGRADE, new Item(XMaterial.GRASS_BLOCK, 9, 1, "&9&lSize Upgrade", Arrays.asList(
            "&7Need more room to expand? Buy this",
            "&7upgrade to increase your island size.",
            "",
            "&9&lInformation:",
            "&9&l * &7Current Level: &9%current_level%",
            "&9&l * &7Current Size: &9%size%x%size% Blocks",
            "&9&l * &7Upgrade Cost: &9%cost%",
            "&9&lLevels:",
            "&9&l * &7Level 1: &950x50 Blocks",
            "&9&l * &7Level 2: &975x75 Blocks",
            "&9&l * &7Level 3: &9100x100 Blocks",
            "&9&l * &7Level 4: &9125x125 Blocks",
            "&9&l * &7Level 5: &9150x150 Blocks",
            "",
            "&9&l[!] " + "&9Left Click to Purchase Level %next_level%."
    )), new ImmutableMap.Builder<Integer, SizeEnhancementData>()
            .put(0, new SizeEnhancementData(5, 10000, new HashMap<>(), 50))
            .put(1, new SizeEnhancementData(5, 10000, new HashMap<>(), 75))
            .put(2, new SizeEnhancementData(10, 10000, new HashMap<>(), 100))
            .put(3, new SizeEnhancementData(10, 10000, new HashMap<>(), 125))
            .put(4, new SizeEnhancementData(15, 10000, new HashMap<>(), 150))
            .build());

    public Enhancement<MembersEnhancementData> membersEnhancement = new Enhancement<>(true, EnhancementType.UPGRADE, new Item(XMaterial.PLAYER_HEAD, 11, 1, "&9&lMember Upgrade", "Peaches_MLG", Arrays.asList(
            "&7Need more members? Buy this",
            "&7upgrade to increase your member count.",
            "",
            "&9&lInformation:",
            "&9&l * &7Current Level: &9%current_level%",
            "&9&l * &7Current Members: &9%members% Members",
            "&9&l * &7Upgrade Cost: &9%cost%",
            "&9&lLevels:",
            "&9&l * &7Level 1: &95 Members",
            "&9&l * &7Level 2: &910 Members",
            "&9&l * &7Level 3: &915 Members",
            "&9&l * &7Level 4: &920 Members",
            "",
            "&9&l[!] " + "&9Left Click to Purchase Level %next_level%."
    )), new ImmutableMap.Builder<Integer, MembersEnhancementData>()
            .put(0, new MembersEnhancementData(5, 10000, new HashMap<>(), 5))
            .put(1, new MembersEnhancementData(5, 10000, new HashMap<>(), 10))
            .put(2, new MembersEnhancementData(10, 10000, new HashMap<>(), 15))
            .put(3, new MembersEnhancementData(15, 10000, new HashMap<>(), 20))
            .build());

    public Enhancement<WarpsEnhancementData> warpsEnhancement = new Enhancement<>(true, EnhancementType.UPGRADE, new Item(XMaterial.END_PORTAL_FRAME, 13, 1, "&9&lWarps Upgrade", Arrays.asList(
            "&7Need more members? Buy this",
            "&7upgrade to increase your member count.",
            "",
            "&9&lInformation:",
            "&9&l * &7Current Level: &9%current_level%",
            "&9&l * &7Current Warps: &9%warps% Warps",
            "&9&l * &7Upgrade Cost: &9%cost%",
            "&9&lLevels:",
            "&9&l * &7Level 1: &91 Warps",
            "&9&l * &7Level 2: &93 Warps",
            "&9&l * &7Level 3: &95 Warps",
            "&9&l * &7Level 4: &97 Warps",
            "&9&l * &7Level 5: &99 Warps",
            "",
            "&9&l[!] " + "&9Left Click to Purchase Level %next_level%."
    )), new ImmutableMap.Builder<Integer, WarpsEnhancementData>()
            .put(0, new WarpsEnhancementData(5, 10000, new HashMap<>(), 1))
            .put(1, new WarpsEnhancementData(5, 10000, new HashMap<>(), 3))
            .put(2, new WarpsEnhancementData(10, 10000, new HashMap<>(), 5))
            .put(3, new WarpsEnhancementData(15, 10000, new HashMap<>(), 7))
            .put(4, new WarpsEnhancementData(15, 10000, new HashMap<>(), 9))
            .build());

    public Enhancement<VoidEnhancementData> voidEnhancement = new Enhancement<>(true, EnhancementType.UPGRADE, new Item(XMaterial.RED_BED, 15, 1, "&9&lVoid Upgrade", Arrays.asList(
            "&7Keep falling off your island?",
            "&7upgrade to improve void teleport.",
            "",
            "&9&lInformation:",
            "&9&l * &7Current Level: &9%current_level%",
            "&9&l * &7Current Warps: &9%warps% Warps",
            "&9&l * &7Upgrade Cost: &9%cost%",
            "&9&lLevels:",
            "&9&l * &7Level 1: &9No Void Teleport",
            "&9&l * &7Level 2: &9100% Item Loss",
            "&9&l * &7Level 3: &975% Item Loss",
            "&9&l * &7Level 4: &950% Item Loss",
            "&9&l * &7Level 5: &925% Item Loss",
            "&9&l * &7Level 6: &90% Item Loss",
            "",
            "&9&l[!] " + "&9Left Click to Purchase Level %next_level%."
    )), new ImmutableMap.Builder<Integer, VoidEnhancementData>()
            .put(0, new VoidEnhancementData(5, 10000, new HashMap<>(), false, 1.00))
            .put(1, new VoidEnhancementData(5, 10000, new HashMap<>(), true, 1.00))
            .put(2, new VoidEnhancementData(10, 10000, new HashMap<>(), true, 0.75))
            .put(3, new VoidEnhancementData(10, 10000, new HashMap<>(), true, 0.50))
            .put(4, new VoidEnhancementData(10, 10000, new HashMap<>(), true, 0.25))
            .put(5, new VoidEnhancementData(15, 10000, new HashMap<>(), true, 0.00))
            .build());

    public Enhancement<GeneratorEnhancementData> generatorEnhancement = new Enhancement<>(true, EnhancementType.UPGRADE, new Item(XMaterial.DIAMOND_ORE, 17, 1, "&9&lGenerator Upgrade", Arrays.asList(
            "&7Want to improve your generator? Buy this",
            "&7upgrade to improve your island generator.",
            "",
            "&9&lInformation:",
            "&9&l * &7Current Level: &9%current_level%",
            "&9&l * &7Upgrade Cost: &9%cost%",
            "",
            "&9&l[!] " + "&9Left Click to Purchase Level %next_level%."
    )), new ImmutableMap.Builder<Integer, GeneratorEnhancementData>()
            .put(0, new GeneratorEnhancementData(5, 10000, new HashMap<>(),
                    ImmutableMap.<XMaterial, Integer>builder()
                            .put(XMaterial.COBBLESTONE, 3)
                            .put(XMaterial.COAL_ORE, 1)
                            .build(),
                    ImmutableMap.<XMaterial, Integer>builder()
                            .put(XMaterial.BASALT, 1)
                            .build()))
            .put(1, new GeneratorEnhancementData(5, 10000, new HashMap<>(),
                    ImmutableMap.<XMaterial, Integer>builder()
                            .put(XMaterial.REDSTONE_ORE, 10)
                            .put(XMaterial.LAPIS_ORE, 10)
                            .put(XMaterial.COAL_ORE, 20)
                            .put(XMaterial.COBBLESTONE, 40)
                            .build(),
                    ImmutableMap.<XMaterial, Integer>builder()
                            .put(XMaterial.BASALT, 20)
                            .put(XMaterial.GLOWSTONE, 20)
                            .put(XMaterial.NETHERRACK, 20)
                            .build()))
            .put(2, new GeneratorEnhancementData(5, 10000, new HashMap<>(),
                    ImmutableMap.<XMaterial, Integer>builder()
                            .put(XMaterial.IRON_ORE, 10)
                            .put(XMaterial.REDSTONE_ORE, 10)
                            .put(XMaterial.GOLD_ORE, 10)
                            .put(XMaterial.LAPIS_ORE, 10)
                            .put(XMaterial.COAL_ORE, 20)
                            .put(XMaterial.COBBLESTONE, 40)
                            .build(),
                    ImmutableMap.<XMaterial, Integer>builder()
                            .put(XMaterial.BASALT, 10)
                            .put(XMaterial.GLOWSTONE, 10)
                            .put(XMaterial.NETHER_QUARTZ_ORE, 10)
                            .put(XMaterial.NETHER_GOLD_ORE, 10)
                            .put(XMaterial.NETHERRACK, 10)
                            .build()))
            .put(3, new GeneratorEnhancementData(5, 10000, new HashMap<>(),
                    ImmutableMap.<XMaterial, Integer>builder()
                            .put(XMaterial.DIAMOND_ORE, 3)
                            .put(XMaterial.IRON_ORE, 10)
                            .put(XMaterial.REDSTONE_ORE, 10)
                            .put(XMaterial.GOLD_ORE, 10)
                            .put(XMaterial.LAPIS_ORE, 10)
                            .put(XMaterial.COAL_ORE, 20)
                            .put(XMaterial.COBBLESTONE, 40)
                            .build(),
                    ImmutableMap.<XMaterial, Integer>builder()
                            .put(XMaterial.BASALT, 10)
                            .put(XMaterial.GLOWSTONE, 10)
                            .put(XMaterial.NETHER_QUARTZ_ORE, 10)
                            .put(XMaterial.NETHER_GOLD_ORE, 10)
                            .put(XMaterial.NETHERRACK, 10)
                            .put(XMaterial.ANCIENT_DEBRIS, 1)
                            .build()))
            .build());
}
