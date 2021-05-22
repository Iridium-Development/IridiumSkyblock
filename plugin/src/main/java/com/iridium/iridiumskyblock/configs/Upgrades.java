package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumskyblock.Item;
import com.iridium.iridiumskyblock.Upgrade;
import com.iridium.iridiumskyblock.upgrades.OresUpgrade;
import com.iridium.iridiumskyblock.upgrades.SizeUpgrade;
import com.iridium.iridiumskyblock.upgrades.WarpsUpgrade;

import java.util.Arrays;

public class Upgrades {
    public Upgrade<SizeUpgrade> sizeUpgrade = new Upgrade<>(true,
            new Item(XMaterial.GRASS_BLOCK, 11, 1, "&9&lIsland Size", Arrays.asList(
                    "&7Need more room to expand? Buy this",
                    "&7upgrade to increase your island size.",
                    "",
                    "&9&lInformation:",
                    "&9&l * &7Current Level: &9%level%",
                    "&9&l * &7Current Size: &9%size%x%size% Blocks",
                    "&9&l * &7Upgrade Cost: &9%crystalscost% Crystals and $%vaultcost%",
                    "&9&lLevels:",
                    "&9&l * &7Level 1: &950x50 Blocks",
                    "&9&l * &7Level 2: &9100x100 Blocks",
                    "&9&l * &7Level 3: &9150x150 Blocks",
                    "",
                    "&9&l[!] &9Left Click to Purchase this Upgrade"
            )), ImmutableMap.<Integer, SizeUpgrade>builder()
            .put(1, new SizeUpgrade(1000, 15, 50))
            .put(2, new SizeUpgrade(1000, 15, 100))
            .put(3, new SizeUpgrade(1000, 15, 150))
            .build());

    public Upgrade<OresUpgrade> oresUpgrade = new Upgrade<>(true,
            new Item(XMaterial.DIAMOND_ORE, 13, 1, "&9&lIsland Generator", Arrays.asList(
                    "&7Want to improve your generator? Buy this",
                    "&7upgrade to improve your island generator.",
                    "",
                    "&9&lInformation:",
                    "&9&l * &7Current Level: &9%level%",
                    "&9&l * &7Upgrade Cost: &9%crystalscost% Crystals and $%vaultcost%",
                    "",
                    "&9&l[!] &9Left Click to Purchase this Upgrade"
            )), ImmutableMap.<Integer, OresUpgrade>builder()
            .put(1, new OresUpgrade(1000, 15, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.COBBLESTONE, 1)
                    .build(), ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.BASALT, 1)
                    .build()))
            .put(2, new OresUpgrade(1000, 15, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.DIAMOND_ORE, 1)
                    .put(XMaterial.IRON_ORE, 10)
                    .put(XMaterial.REDSTONE_ORE, 10)
                    .put(XMaterial.GOLD_ORE, 10)
                    .put(XMaterial.LAPIS_ORE, 10)
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

    public Upgrade<WarpsUpgrade> warpsUpgrade = new Upgrade<>(true,
            new Item(XMaterial.END_PORTAL_FRAME, 15, 1, "&9&lIsland Warps", Arrays.asList(
                    "&7Need more island warps? Buy this",
                    "&7upgrade to increase your island warps.",
                    "",
                    "&9&lInformation:",
                    "&9&l * &7Current Level: &9%level%",
                    "&9&l * &7Upgrade Cost: &9%crystalscost% Crystals and $%vaultcost%",
                    "&9&lLevels:",
                    "&9&l * &7Level 1: &91 Warp",
                    "&9&l * &7Level 2: &92 Warp",
                    "&9&l * &7Level 3: &93 Warp",
                    "&9&l * &7Level 4: &94 Warp",
                    "&9&l * &7Level 5: &95 Warp",
                    "",
                    "&9&l[!] &9Left Click to Purchase this Upgrade"
            )), ImmutableMap.<Integer, WarpsUpgrade>builder()
            .put(1, new WarpsUpgrade(1000, 15, 1))
            .put(2, new WarpsUpgrade(1000, 15, 2))
            .put(3, new WarpsUpgrade(1000, 15, 3))
            .put(4, new WarpsUpgrade(1000, 15, 4))
            .put(5, new WarpsUpgrade(1000, 15, 5))
            .build());
}
