package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumskyblock.support.material.Item;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;
import com.iridium.iridiumskyblock.support.material.IridiumMaterial;
import com.iridium.iridiumskyblock.Upgrade;
import com.iridium.iridiumskyblock.upgrades.*;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Upgrades {
    public Upgrade<SizeUpgrade> sizeUpgrade = new Upgrade<>(true, "Size",
            new Item(IridiumMaterial.GRASS_BLOCK, 11, 1, "&b&lIsland Size", Arrays.asList(
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
            new Item(IridiumMaterial.ARMOR_STAND, 12, 1, "&b&lIsland Members", Arrays.asList(
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
            new Item(IridiumMaterial.HOPPER, 13, 1, "&b&lIsland Block Limits", Arrays.asList(
                    "&7Need to place more blocks? Buy this",
                    "&7upgrade to increase the amount of blocks you can place.",
                    "",
                    "&b&lInformation:",
                    "&b&l * &7Current Level: &b%level%",
                    "&b&l * &7Upgrade Cost: &b%crystalscost% Crystals and $%vaultcost%",
                    "",
                    "&b&l[!] &bLeft Click to Purchase this Upgrade"
            )), ImmutableMap.<Integer, BlockLimitUpgrade>builder()
            .put(1, new BlockLimitUpgrade(1000, 15, ImmutableMap.<IridiumMaterial, Integer>builder()
                    .put(IridiumMaterial.HOPPER, 15)
                    .put(IridiumMaterial.PISTON, 10)
                    .put(IridiumMaterial.SPAWNER, 10)
                    .build()))
            .put(2, new BlockLimitUpgrade(1000, 15, ImmutableMap.<IridiumMaterial, Integer>builder()
                    .put(IridiumMaterial.HOPPER, 20)
                    .put(IridiumMaterial.PISTON, 15)
                    .put(IridiumMaterial.SPAWNER, 15)
                    .build()))
            .put(3, new BlockLimitUpgrade(1000, 15, ImmutableMap.<IridiumMaterial, Integer>builder()
                    .put(IridiumMaterial.HOPPER, 25)
                    .put(IridiumMaterial.PISTON, 20)
                    .put(IridiumMaterial.SPAWNER, 20)
                    .build()))
            .put(4, new BlockLimitUpgrade(1000, 15, ImmutableMap.<IridiumMaterial, Integer>builder()
                    .put(IridiumMaterial.HOPPER, 30)
                    .put(IridiumMaterial.PISTON, 25)
                    .put(IridiumMaterial.SPAWNER, 25)
                    .build()))
            .put(5, new BlockLimitUpgrade(1000, 15, ImmutableMap.<IridiumMaterial, Integer>builder()
                    .put(IridiumMaterial.HOPPER, 40)
                    .put(IridiumMaterial.PISTON, 30)
                    .put(IridiumMaterial.SPAWNER, 30)
                    .build()))
            .build());

    public Upgrade<WarpsUpgrade> warpsUpgrade = new Upgrade<>(true, "Warps",
            new Item(IridiumMaterial.END_PORTAL_FRAME, 14, 1, "&b&lIsland Warps", Arrays.asList(
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
            new Item(IridiumMaterial.DIAMOND_ORE, 15, 1, "&b&lIsland Generator", Arrays.asList(
                    "&7Want to improve your generator? Buy this",
                    "&7upgrade to improve your island generator.",
                    "",
                    "&b&lInformation:",
                    "&b&l * &7Current Level: &b%level%",
                    "&b&l * &7Upgrade Cost: &b%crystalscost% Crystals and $%vaultcost%",
                    "",
                    "&b&l[!] &bLeft Click to Purchase this Upgrade"
            )), ImmutableMap.<Integer, OresUpgrade>builder()
            .put(1, new OresUpgrade(1000, 15, ImmutableMap.<IridiumMaterial, Integer>builder()
                    .put(IridiumMaterial.COBBLESTONE, 3)
                    .put(IridiumMaterial.COAL_ORE, 1)
                    .build(), ImmutableMap.<IridiumMaterial, Integer>builder()
                    .put(IridiumMaterial.BASALT, 1)
                    .build()))
            .put(2, new OresUpgrade(1000, 15, ImmutableMap.<IridiumMaterial, Integer>builder()
                    .put(IridiumMaterial.DIAMOND_ORE, 1)
                    .put(IridiumMaterial.IRON_ORE, 10)
                    .put(IridiumMaterial.REDSTONE_ORE, 10)
                    .put(IridiumMaterial.GOLD_ORE, 10)
                    .put(IridiumMaterial.LAPIS_ORE, 10)
                    .put(IridiumMaterial.COAL_ORE, 20)
                    .put(IridiumMaterial.COBBLESTONE, 40)
                    .build(), ImmutableMap.<IridiumMaterial, Integer>builder()
                    .put(IridiumMaterial.BASALT, 20)
                    .put(IridiumMaterial.GLOWSTONE, 20)
                    .put(IridiumMaterial.NETHER_QUARTZ_ORE, 20)
                    .put(IridiumMaterial.NETHER_GOLD_ORE, 20)
                    .put(IridiumMaterial.NETHERRACK, 20)
                    .put(IridiumMaterial.ANCIENT_DEBRIS, 1)
                    .build()))
            .put(3, new OresUpgrade(1000, 15, ImmutableMap.<IridiumMaterial, Integer>builder()
                    .put(IridiumMaterial.DIAMOND_ORE, 10)
                    .put(IridiumMaterial.IRON_ORE, 10)
                    .put(IridiumMaterial.REDSTONE_ORE, 10)
                    .put(IridiumMaterial.GOLD_ORE, 10)
                    .put(IridiumMaterial.LAPIS_ORE, 10)
                    .put(IridiumMaterial.COAL_ORE, 20)
                    .put(IridiumMaterial.COBBLESTONE, 40)
                    .build(), ImmutableMap.<IridiumMaterial, Integer>builder()
                    .put(IridiumMaterial.BASALT, 10)
                    .put(IridiumMaterial.GLOWSTONE, 10)
                    .put(IridiumMaterial.NETHER_QUARTZ_ORE, 10)
                    .put(IridiumMaterial.NETHER_GOLD_ORE, 10)
                    .put(IridiumMaterial.NETHERRACK, 10)
                    .put(IridiumMaterial.ANCIENT_DEBRIS, 1)
                    .build()))
            .build());

}
