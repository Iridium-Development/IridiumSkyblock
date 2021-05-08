package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumskyblock.Item;
import com.iridium.iridiumskyblock.Upgrade;
import com.iridium.iridiumskyblock.upgrades.OresUpgrade;
import com.iridium.iridiumskyblock.upgrades.SizeUpgrade;

import java.util.Arrays;

public class Upgrades {
    public Upgrade<SizeUpgrade> sizeUpgrade = new Upgrade<>(true,
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

    public Upgrade<OresUpgrade> oresUpgrade = new Upgrade<>(true,
            new Item(XMaterial.DIAMOND_ORE, 13, 1, "&b&lIsland Generator", Arrays.asList(
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
                    .put(XMaterial.COBBLESTONE, 1)
                    .build()))
            .put(2, new OresUpgrade(1000, 15, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.DIAMOND_ORE, 1)
                    .put(XMaterial.IRON_ORE, 10)
                    .put(XMaterial.REDSTONE_ORE, 10)
                    .put(XMaterial.GOLD_ORE, 10)
                    .put(XMaterial.LAPIS_ORE, 10)
                    .put(XMaterial.COBBLESTONE, 40)
                    .build()))
            .put(3, new OresUpgrade(1000, 15, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.DIAMOND_ORE, 10)
                    .put(XMaterial.IRON_ORE, 10)
                    .put(XMaterial.REDSTONE_ORE, 10)
                    .put(XMaterial.GOLD_ORE, 10)
                    .put(XMaterial.LAPIS_ORE, 10)
                    .put(XMaterial.COBBLESTONE, 40)
                    .build()))
            .build());
}
