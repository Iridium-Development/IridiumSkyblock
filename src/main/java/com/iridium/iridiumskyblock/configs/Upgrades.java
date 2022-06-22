package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.Upgrade;
import com.iridium.iridiumskyblock.upgrades.*;

import java.util.Arrays;
import java.util.Collections;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Upgrades {
    public Upgrade<SizeUpgrade> sizeUpgrade = new Upgrade<>(true, "Size",
            new Item(XMaterial.GRASS_BLOCK, 11, 1, "&b&lIsland Size", Arrays.asList(
                    "&7Need more room to expand? Buy this",
                    "&7upgrade to increase your island size.",
                    "",
                    "&b&lInformation:",
                    "&b&l * &7Current Level: &b%level%",
                    "&b&l * &7Current Size: &b%size%x%size% Blocks",
                    "&b&l * &7Upgrade Cost: &b%crystalscost% Crystals and $%vaultcost%",
                    "&b&lLevels:",
                    "&b&l * &7Level 1: &b50x50 Blocks",
                    "&b&l * &7Level 2: &b100x100 Blocks",
                    "&b&l * &7Level 3: &b150x150 Blocks",
                    "",
                    "&b&l[!] &bLeft Click to Purchase this Upgrade"
            )), ImmutableMap.<Integer, SizeUpgrade>builder()
            .put(1, new SizeUpgrade(1000, 15, 50))
            .put(2, new SizeUpgrade(1000, 15, 100))
            .put(3, new SizeUpgrade(1000, 15, 150))
            .build());

    public Upgrade<MemberUpgrade> memberUpgrade = new Upgrade<>(true, "Member",
            new Item(XMaterial.ARMOR_STAND, 12, 1, "&b&lIsland Members", Arrays.asList(
                    "&7Need more members? Buy this",
                    "&7upgrade to increase your member count.",
                    "",
                    "&b&lInformation:",
                    "&b&l * &7Current Level: &b%level%",
                    "&b&l * &7Current Member: &b%amount% Members",
                    "&b&l * &7Upgrade Cost: &b%crystalscost% Crystals and $%vaultcost%",
                    "&b&lLevels:",
                    "&b&l * &7Level 1: &b9 Members",
                    "&b&l * &7Level 2: &b18 Members",
                    "&b&l * &7Level 3: &b27 Members",
                    "",
                    "&b&l[!] &bLeft Click to Purchase this Upgrade"
            )), ImmutableMap.<Integer, MemberUpgrade>builder()
            .put(1, new MemberUpgrade(1000, 15, 9))
            .put(2, new MemberUpgrade(1000, 15, 18))
            .put(3, new MemberUpgrade(1000, 15, 27))
            .build());

    public Upgrade<BlockLimitUpgrade> blockLimitUpgrade = new Upgrade<>(true, "Block Limit",
            new Item(XMaterial.HOPPER, 13, 1, "&b&lIsland Block Limits", Arrays.asList(
                    "&7Need to place more blocks? Buy this",
                    "&7upgrade to increase the amount of blocks you can place.",
                    "",
                    "&b&lInformation:",
                    "&b&l * &7Current Level: &b%level%",
                    "&b&l * &7Upgrade Cost: &b%crystalscost% Crystals and $%vaultcost%",
                    "",
                    "&b&l[!] &bLeft Click to Purchase this Upgrade",
                    "&b&l[!] &bRight Click for details of limits"
            )), ImmutableMap.<Integer, BlockLimitUpgrade>builder()
            .put(1, new BlockLimitUpgrade(1000, 15, ImmutableMap.<XMaterial, BlockLimitUpgrade.LimitedBlock>builder()
                    .put(XMaterial.HOPPER, new BlockLimitUpgrade.LimitedBlock(15, "&b&lHopper", 1, 10))
                    .put(XMaterial.PISTON, new BlockLimitUpgrade.LimitedBlock(10, "&b&lPiston", 1, 11))
                    .put(XMaterial.SPAWNER, new BlockLimitUpgrade.LimitedBlock(10, "&b&lSpawner", 1, 12))
                    .build(), 1,
                    new Item(XMaterial.GOLD_BLOCK, 10, 1, "&b&lLevel 1", Collections.emptyList())))
            .put(2, new BlockLimitUpgrade(1000, 15, ImmutableMap.<XMaterial, BlockLimitUpgrade.LimitedBlock>builder()
                    .put(XMaterial.HOPPER, new BlockLimitUpgrade.LimitedBlock(20, "&b&lHopper", 1, 10))
                    .put(XMaterial.PISTON, new BlockLimitUpgrade.LimitedBlock(15, "&b&lPiston", 1, 11))
                    .put(XMaterial.SPAWNER, new BlockLimitUpgrade.LimitedBlock(15, "&b&lSpawner", 1, 12))
                    .build(), 1,
                    new Item(XMaterial.GOLD_BLOCK, 11, 1, "&b&lLevel 2", Collections.emptyList())))
            .put(3, new BlockLimitUpgrade(1000, 15, ImmutableMap.<XMaterial, BlockLimitUpgrade.LimitedBlock>builder()
                    .put(XMaterial.HOPPER, new BlockLimitUpgrade.LimitedBlock(25, "&b&lHopper", 1, 10))
                    .put(XMaterial.PISTON, new BlockLimitUpgrade.LimitedBlock(20, "&b&lPiston", 1, 11))
                    .put(XMaterial.SPAWNER, new BlockLimitUpgrade.LimitedBlock(20, "&b&lSpawner", 1, 12))
                    .build(), 1,
                    new Item(XMaterial.GOLD_BLOCK, 12, 1, "&b&lLevel 3", Collections.emptyList())))
            .put(4, new BlockLimitUpgrade(1000, 15, ImmutableMap.<XMaterial, BlockLimitUpgrade.LimitedBlock>builder()
                    .put(XMaterial.HOPPER, new BlockLimitUpgrade.LimitedBlock(30, "&b&lHopper", 1, 10))
                    .put(XMaterial.PISTON, new BlockLimitUpgrade.LimitedBlock(25, "&b&lPiston", 1, 11))
                    .put(XMaterial.SPAWNER, new BlockLimitUpgrade.LimitedBlock(25, "&b&lSpawner", 1, 12))
                    .build(), 1,
                    new Item(XMaterial.GOLD_BLOCK, 13, 1, "&b&lLevel 4", Collections.emptyList())))
            .put(5, new BlockLimitUpgrade(1000, 15, ImmutableMap.<XMaterial, BlockLimitUpgrade.LimitedBlock>builder()
                    .put(XMaterial.HOPPER, new BlockLimitUpgrade.LimitedBlock(40, "&b&lHopper", 1, 10))
                    .put(XMaterial.PISTON, new BlockLimitUpgrade.LimitedBlock(30, "&b&lPiston", 1, 11))
                    .put(XMaterial.SPAWNER, new BlockLimitUpgrade.LimitedBlock(30, "&b&lSpawner", 1, 12))
                    .build(), 1,
                    new Item(XMaterial.GOLD_BLOCK, 14, 1, "&b&lLevel 5", Collections.emptyList())))
            .build());

    public Upgrade<WarpsUpgrade> warpsUpgrade = new Upgrade<>(true, "Warps",
            new Item(XMaterial.END_PORTAL_FRAME, 14, 1, "&b&lIsland Warps", Arrays.asList(
                    "&7Need more island warps? Buy this",
                    "&7upgrade to increase your island warps.",
                    "",
                    "&b&lInformation:",
                    "&b&l * &7Current Level: &b%level%",
                    "&b&l * &7Upgrade Cost: &b%crystalscost% Crystals and $%vaultcost%",
                    "&b&lLevels:",
                    "&b&l * &7Level 1: &b1 Warp",
                    "&b&l * &7Level 2: &b2 Warp",
                    "&b&l * &7Level 3: &b3 Warp",
                    "&b&l * &7Level 4: &b4 Warp",
                    "&b&l * &7Level 5: &b5 Warp",
                    "",
                    "&b&l[!] &bLeft Click to Purchase this Upgrade"
            )), ImmutableMap.<Integer, WarpsUpgrade>builder()
            .put(1, new WarpsUpgrade(1000, 15, 1))
            .put(2, new WarpsUpgrade(1000, 15, 2))
            .put(3, new WarpsUpgrade(1000, 15, 3))
            .put(4, new WarpsUpgrade(1000, 15, 4))
            .put(5, new WarpsUpgrade(1000, 15, 5))
            .build());

    public Upgrade<OresUpgrade> oresUpgrade = new Upgrade<>(true, "Ore Generator",
            new Item(XMaterial.DIAMOND_ORE, 15, 1, "&b&lIsland Generator", Arrays.asList(
                    "&7Want to improve your generator? Buy this",
                    "&7upgrade to improve your island generator.",
                    "",
                    "&b&lInformation:",
                    "&b&l * &7Current Level: &b%level%",
                    "&b&l * &7Upgrade Cost: &b%crystalscost% Crystals and $%vaultcost%",
                    "",
                    "&b&l[!] &bLeft Click to Purchase this Upgrade"
            )), ImmutableMap.<Integer, OresUpgrade>builder()
            .put(1, new OresUpgrade(1000, 15, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.COBBLESTONE, 3)
                    .put(XMaterial.COAL_ORE, 1)
                    .build(), ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.BASALT, 1)
                    .build()))
            .put(2, new OresUpgrade(1000, 15, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.DIAMOND_ORE, 1)
                    .put(XMaterial.IRON_ORE, 10)
                    .put(XMaterial.REDSTONE_ORE, 10)
                    .put(XMaterial.GOLD_ORE, 10)
                    .put(XMaterial.LAPIS_ORE, 10)
                    .put(XMaterial.COAL_ORE, 20)
                    .put(XMaterial.COBBLESTONE, 40)
                    .build(), ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.BASALT, 20)
                    .put(XMaterial.GLOWSTONE, 20)
                    .put(XMaterial.NETHER_QUARTZ_ORE, 20)
                    .put(XMaterial.NETHER_GOLD_ORE, 20)
                    .put(XMaterial.NETHERRACK, 20)
                    .put(XMaterial.ANCIENT_DEBRIS, 1)
                    .build()))
            .put(3, new OresUpgrade(1000, 15, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.DIAMOND_ORE, 10)
                    .put(XMaterial.IRON_ORE, 10)
                    .put(XMaterial.REDSTONE_ORE, 10)
                    .put(XMaterial.GOLD_ORE, 10)
                    .put(XMaterial.LAPIS_ORE, 10)
                    .put(XMaterial.COAL_ORE, 20)
                    .put(XMaterial.COBBLESTONE, 40)
                    .build(), ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.BASALT, 10)
                    .put(XMaterial.GLOWSTONE, 10)
                    .put(XMaterial.NETHER_QUARTZ_ORE, 10)
                    .put(XMaterial.NETHER_GOLD_ORE, 10)
                    .put(XMaterial.NETHERRACK, 10)
                    .put(XMaterial.ANCIENT_DEBRIS, 1)
                    .build()))
            .build());

}
