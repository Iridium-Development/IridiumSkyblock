package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.Upgrade;
import com.iridium.iridiumskyblock.upgrades.*;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Upgrades {
    public Upgrade<SizeUpgrade> sizeUpgrade = new Upgrade<>(true, "Size",
            new Item(XMaterial.GRASS_BLOCK, 11, 1, "&b&lIsland Size", Arrays.asList(
                    "",
                    "&7Need more room to expand? Buy this",
                    "&7upgrade to increase your island size",
                    "",
                    "&b&lInformation:",
                    "&b&l • &7Current Level: &b%level%",
                    "&b&l • &7Current Size: &b%size%x%size% Blocks",
                    "&b&l • &7Upgrade Cost: &e%mobcoinscost% Mob Coins",
                    "",
                    "&b&lLevels:",
                    "&b&l • &7Level 1: &b50x50 Blocks",
                    "&b&l • &7Level 2: &b100x100 Blocks",
                    "&b&l • &7Level 3: &b150x150 Blocks",
                    "",
                    "&b&l[!] &bClick to Purchase this Upgrade"
            )), ImmutableMap.<Integer, SizeUpgrade>builder()
            .put(1, new SizeUpgrade(0, 0, 0, 0, 50))
            .put(2, new SizeUpgrade(0, 0, 0, 0, 100))
            .put(3, new SizeUpgrade(0, 0, 0, 0, 150))
            .build());

    public Upgrade<MemberUpgrade> memberUpgrade = new Upgrade<>(true, "Member",
            new Item(XMaterial.ARMOR_STAND, 12, 1, "&b&lIsland Members", Arrays.asList(
                    "",
                    "&7Need more members? Buy this",
                    "&7upgrade to increase your member count",
                    "",
                    "&b&lInformation:",
                    "&b&l • &7Current Level: &b%level%",
                    "&b&l • &7Current Member: &b%amount% Members",
                    "&b&l • &7Upgrade Cost: &e%mobcoinscost% Mob Coins",
                    "",
                    "&b&lLevels:",
                    "&b&l • &7Level 1: &b5 Members",
                    "&b&l • &7Level 2: &b6 Members",
                    "&b&l • &7Level 3: &b7 Members",
                    "&b&l • &7Level 4: &b8 Members",
                    "&b&l • &7Level 5: &b9 Members",
                    "&b&l • &7Level 6: &b10 Members",
                    "",
                    "&b&l[!] &bClick to Purchase this Upgrade"
            )), ImmutableMap.<Integer, MemberUpgrade>builder()
            .put(1, new MemberUpgrade(0, 0, 0, 0, 5))
            .put(2, new MemberUpgrade(0, 0, 6000, 0, 6))
            .put(3, new MemberUpgrade(0, 0, 21000, 0, 7))
            .put(4, new MemberUpgrade(0, 0, 80000, 0, 8))
            .put(5, new MemberUpgrade(0, 0, 180000, 0, 9))
            .put(6, new MemberUpgrade(0, 0, 500000, 0, 10))
            .build());

    public Upgrade<BlockLimitUpgrade> blockLimitUpgrade = new Upgrade<>(true, "Block Limit",
            new Item(XMaterial.HOPPER, 13, 1, "&b&lIsland Hopper Limits", Arrays.asList(
                    "",
                    "&7Need to place more blocks? Buy this upgrade",
                    "&7 to increase the amount of blocks you can place",
                    "",
                    "&b&lInformation:",
                    "&b&l • &7Current Level: &b%level%",
                    "&b&l • &7Upgrade Cost: &e%mobcoinscost% Mob Coins",
                    "",
                    "&b&lLevels:",
                    "&b&l • &7Level 1: &b0 Hoppers",
                    "&b&l • &7Level 2: &b50 Hoppers",
                    "&b&l • &7Level 3: &b75 Hoppers",
                    "&b&l • &7Level 4: &b100 Hoppers",
                    "&b&l • &7Level 5: &b150 Hoppers",
                    "&b&l • &7Level 6: &b200 Hoppers",
                    "&b&l • &7Level 7: &b250 Hoppers",
                    "&b&l • &7Level 8: &b350 Hoppers",
                    "",
                    "&b&l[!] &bClick to Purchase this Upgrade"
            )), ImmutableMap.<Integer, BlockLimitUpgrade>builder()
            .put(1, new BlockLimitUpgrade(0, 0, 0, 0, ImmutableMap.<XMaterial, Integer>builder().put(XMaterial.HOPPER, 0).build()))
            .put(2, new BlockLimitUpgrade(0, 0, 3000, 0, ImmutableMap.<XMaterial, Integer>builder().put(XMaterial.HOPPER, 50).build()))
            .put(3, new BlockLimitUpgrade(0, 0, 7000, 0, ImmutableMap.<XMaterial, Integer>builder().put(XMaterial.HOPPER, 75).build()))
            .put(4, new BlockLimitUpgrade(0, 0, 24000, 0, ImmutableMap.<XMaterial, Integer>builder().put(XMaterial.HOPPER, 100).build()))
            .put(5, new BlockLimitUpgrade(0, 0, 90000, 0, ImmutableMap.<XMaterial, Integer>builder().put(XMaterial.HOPPER, 150).build()))
            .put(6, new BlockLimitUpgrade(0, 0, 200000, 0, ImmutableMap.<XMaterial, Integer>builder().put(XMaterial.HOPPER, 200).build()))
            .put(7, new BlockLimitUpgrade(0, 0, 400000, 0, ImmutableMap.<XMaterial, Integer>builder().put(XMaterial.HOPPER, 250).build()))
            .put(8, new BlockLimitUpgrade(0, 0, 750000, 0, ImmutableMap.<XMaterial, Integer>builder().put(XMaterial.HOPPER, 350).build()))
            .build());

    public Upgrade<WarpsUpgrade> warpsUpgrade = new Upgrade<>(true, "Warps",
            new Item(XMaterial.END_PORTAL_FRAME, 14, 1, "&b&lIsland Warps", Arrays.asList(
                    "",
                    "&7Need more island warps? Buy this",
                    "&7upgrade to increase your island warps",
                    "",
                    "&b&lInformation:",
                    "&b&l • &7Current Level: &b%level%",
                    "&b&l • &7Upgrade Cost: &e%mobcoinscost% Mob Coins",
                    "",
                    "&b&lLevels:",
                    "&b&l • &7Level 1: &b1 Warp",
                    "&b&l • &7Level 2: &b2 Warps",
                    "&b&l • &7Level 3: &b3 Warps",
                    "&b&l • &7Level 4: &b4 Warps",
                    "&b&l • &7Level 5: &b5 Warps",
                    "",
                    "&b&l[!] &bClick to Purchase this Upgrade"
            )), ImmutableMap.<Integer, WarpsUpgrade>builder()
            .put(1, new WarpsUpgrade(0, 0, 0, 0, 1))
            .put(2, new WarpsUpgrade(0, 0, 5000, 0, 2))
            .put(3, new WarpsUpgrade(0, 0, 30000, 0, 3))
            .put(4, new WarpsUpgrade(0, 0, 100000, 0, 4))
            .put(5, new WarpsUpgrade(0, 0, 250000, 0, 5))
            .build());

    public Upgrade<OresUpgrade> oresUpgrade = new Upgrade<>(true, "Ore Generator",
            new Item(XMaterial.DIAMOND_ORE, 15, 1, "&b&lIsland Generator", Arrays.asList(
                    "",
                    "&7Want to improve your generator? Buy this",
                    "&7upgrade to improve your island generator",
                    "",
                    "&b&lInformation:",
                    "&b&l • &7Current Level: &b%level%",
                    "&b&l • &7Upgrade Cost: &e%mobcoinscost% Mob Coins",
                    "",
                    "&b&l[!] &bClick to Purchase this Upgrade"
            )), ImmutableMap.<Integer, OresUpgrade>builder()
            .put(1, new OresUpgrade(0, 0, 0, 0, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.STONE, 62)
                    .put(XMaterial.COAL_ORE, 11)
                    .put(XMaterial.IRON_ORE, 10)
                    .put(XMaterial.LAPIS_ORE, 5)
                    .put(XMaterial.REDSTONE_ORE, 5)
                    .put(XMaterial.GOLD_ORE, 4)
                    .put(XMaterial.DIAMOND_ORE, 2)
                    .put(XMaterial.EMERALD_ORE, 1)
                    .build(), ImmutableMap.<XMaterial, Integer>builder().build()))
            .put(2, new OresUpgrade(0, 0, 1200, 0, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.STONE, 55)
                    .put(XMaterial.COAL_ORE, 15)
                    .put(XMaterial.IRON_ORE, 12)
                    .put(XMaterial.LAPIS_ORE, 5)
                    .put(XMaterial.REDSTONE_ORE, 5)
                    .put(XMaterial.GOLD_ORE, 5)
                    .put(XMaterial.DIAMOND_ORE, 3)
                    .put(XMaterial.EMERALD_ORE, 1)
                    .build(), ImmutableMap.<XMaterial, Integer>builder().build()))
            .put(3, new OresUpgrade(0, 0, 14000, 0, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.STONE, 50)
                    .put(XMaterial.COAL_ORE, 15)
                    .put(XMaterial.IRON_ORE, 12)
                    .put(XMaterial.LAPIS_ORE, 8)
                    .put(XMaterial.REDSTONE_ORE, 8)
                    .put(XMaterial.GOLD_ORE, 7)
                    .put(XMaterial.DIAMOND_ORE, 3)
                    .put(XMaterial.EMERALD_ORE, 1)
                    .build(), ImmutableMap.<XMaterial, Integer>builder().build()))
            .put(4, new OresUpgrade(0, 0, 48000, 0, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.STONE, 46)
                    .put(XMaterial.COAL_ORE, 15)
                    .put(XMaterial.IRON_ORE, 12)
                    .put(XMaterial.LAPIS_ORE, 10)
                    .put(XMaterial.REDSTONE_ORE, 10)
                    .put(XMaterial.GOLD_ORE, 7)
                    .put(XMaterial.DIAMOND_ORE, 3)
                    .put(XMaterial.EMERALD_ORE, 3)
                    .put(XMaterial.NETHER_QUARTZ_ORE, 3)
                    .put(XMaterial.NETHERRACK, 20)
                    .build(), ImmutableMap.<XMaterial, Integer>builder().build()))
            .put(5, new OresUpgrade(0, 0, 180000, 0, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.STONE, 42)
                    .put(XMaterial.COAL_ORE, 15)
                    .put(XMaterial.IRON_ORE, 15)
                    .put(XMaterial.LAPIS_ORE, 10)
                    .put(XMaterial.REDSTONE_ORE, 10)
                    .put(XMaterial.GOLD_ORE, 8)
                    .put(XMaterial.DIAMOND_ORE, 4)
                    .put(XMaterial.EMERALD_ORE, 4)
                    .put(XMaterial.NETHER_QUARTZ_ORE, 4)
                    .put(XMaterial.NETHERRACK, 20)
                    .put(XMaterial.END_STONE, 2)
                    .put(XMaterial.IRON_BLOCK, 4)
                    .put(XMaterial.GOLD_BLOCK, 2)
                    .build(), ImmutableMap.<XMaterial, Integer>builder().build()))
            .put(6, new OresUpgrade(0, 0, 500000, 0, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.STONE, 40)
                    .put(XMaterial.COAL_ORE, 15)
                    .put(XMaterial.IRON_ORE, 15)
                    .put(XMaterial.LAPIS_ORE, 10)
                    .put(XMaterial.REDSTONE_ORE, 10)
                    .put(XMaterial.GOLD_ORE, 8)
                    .put(XMaterial.DIAMOND_ORE, 5)
                    .put(XMaterial.EMERALD_ORE, 5)
                    .put(XMaterial.NETHER_QUARTZ_ORE, 6)
                    .put(XMaterial.NETHERRACK, 20)
                    .put(XMaterial.END_STONE, 4)
                    .put(XMaterial.IRON_BLOCK, 4)
                    .put(XMaterial.GOLD_BLOCK, 3)
                    .put(XMaterial.DIAMOND_BLOCK, 1)
                    .put(XMaterial.EMERALD_BLOCK, 1)
                    .build(), ImmutableMap.<XMaterial, Integer>builder().build()))
            .put(7, new OresUpgrade(0, 0, 750000, 0, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.STONE, 35)
                    .put(XMaterial.COAL_ORE, 15)
                    .put(XMaterial.IRON_ORE, 15)
                    .put(XMaterial.LAPIS_ORE, 10)
                    .put(XMaterial.REDSTONE_ORE, 10)
                    .put(XMaterial.GOLD_ORE, 8)
                    .put(XMaterial.DIAMOND_ORE, 6)
                    .put(XMaterial.EMERALD_ORE, 6)
                    .put(XMaterial.NETHER_QUARTZ_ORE, 6)
                    .put(XMaterial.NETHERRACK, 15)
                    .put(XMaterial.END_STONE, 5)
                    .put(XMaterial.IRON_BLOCK, 5)
                    .put(XMaterial.GOLD_BLOCK, 3)
                    .put(XMaterial.DIAMOND_BLOCK, 1)
                    .put(XMaterial.EMERALD_BLOCK, 2)
                    .put(XMaterial.LAPIS_BLOCK, 5)
                    .build(), ImmutableMap.<XMaterial, Integer>builder().build()))
            .put(8, new OresUpgrade(0, 0, 1000000, 0, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.STONE, 35)
                    .put(XMaterial.COAL_ORE, 14)
                    .put(XMaterial.IRON_ORE, 14)
                    .put(XMaterial.LAPIS_ORE, 10)
                    .put(XMaterial.REDSTONE_ORE, 10)
                    .put(XMaterial.GOLD_ORE, 7)
                    .put(XMaterial.DIAMOND_ORE, 6)
                    .put(XMaterial.EMERALD_ORE, 6)
                    .put(XMaterial.NETHER_QUARTZ_ORE, 6)
                    .put(XMaterial.NETHERRACK, 15)
                    .put(XMaterial.END_STONE, 6)
                    .put(XMaterial.IRON_BLOCK, 5)
                    .put(XMaterial.GOLD_BLOCK, 4)
                    .put(XMaterial.DIAMOND_BLOCK, 1)
                    .put(XMaterial.EMERALD_BLOCK, 2)
                    .put(XMaterial.LAPIS_BLOCK, 6)
                    .build(), ImmutableMap.<XMaterial, Integer>builder().build()))
            .put(9, new OresUpgrade(0, 0, 1250000, 0, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.STONE, 35)
                    .put(XMaterial.COAL_ORE, 12)
                    .put(XMaterial.IRON_ORE, 12)
                    .put(XMaterial.LAPIS_ORE, 9)
                    .put(XMaterial.REDSTONE_ORE, 9)
                    .put(XMaterial.GOLD_ORE, 7)
                    .put(XMaterial.DIAMOND_ORE, 7)
                    .put(XMaterial.EMERALD_ORE, 7)
                    .put(XMaterial.NETHER_QUARTZ_ORE, 6)
                    .put(XMaterial.NETHERRACK, 15)
                    .put(XMaterial.END_STONE, 7)
                    .put(XMaterial.IRON_BLOCK, 6)
                    .put(XMaterial.GOLD_BLOCK, 5)
                    .put(XMaterial.DIAMOND_BLOCK, 2)
                    .put(XMaterial.EMERALD_BLOCK, 2)
                    .put(XMaterial.LAPIS_BLOCK, 7)
                    .build(), ImmutableMap.<XMaterial, Integer>builder().build()))
            .put(10, new OresUpgrade(0, 0, 1500000, 0, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.STONE, 35)
                    .put(XMaterial.COAL_ORE, 11)
                    .put(XMaterial.IRON_ORE, 11)
                    .put(XMaterial.LAPIS_ORE, 8)
                    .put(XMaterial.REDSTONE_ORE, 8)
                    .put(XMaterial.GOLD_ORE, 7)
                    .put(XMaterial.DIAMOND_ORE, 7)
                    .put(XMaterial.EMERALD_ORE, 7)
                    .put(XMaterial.NETHER_QUARTZ_ORE, 6)
                    .put(XMaterial.NETHERRACK, 15)
                    .put(XMaterial.END_STONE, 8)
                    .put(XMaterial.IRON_BLOCK, 7)
                    .put(XMaterial.GOLD_BLOCK, 5)
                    .put(XMaterial.DIAMOND_BLOCK, 2)
                    .put(XMaterial.EMERALD_BLOCK, 2)
                    .put(XMaterial.LAPIS_BLOCK, 7)
                    .build(), ImmutableMap.<XMaterial, Integer>builder().build()))
            .put(11, new OresUpgrade(0, 0, 2000000, 0, ImmutableMap.<XMaterial, Integer>builder()
                    .put(XMaterial.STONE, 30)
                    .put(XMaterial.COAL_ORE, 11)
                    .put(XMaterial.IRON_ORE, 11)
                    .put(XMaterial.LAPIS_ORE, 8)
                    .put(XMaterial.REDSTONE_ORE, 8)
                    .put(XMaterial.GOLD_ORE, 7)
                    .put(XMaterial.DIAMOND_ORE, 7)
                    .put(XMaterial.EMERALD_ORE, 7)
                    .put(XMaterial.NETHER_QUARTZ_ORE, 6)
                    .put(XMaterial.NETHERRACK, 15)
                    .put(XMaterial.END_STONE, 9)
                    .put(XMaterial.IRON_BLOCK, 8)
                    .put(XMaterial.GOLD_BLOCK, 8)
                    .put(XMaterial.DIAMOND_BLOCK, 2)
                    .put(XMaterial.EMERALD_BLOCK, 3)
                    .put(XMaterial.LAPIS_BLOCK, 8)
                    .build(), ImmutableMap.<XMaterial, Integer>builder().build()))
            .build());

    public Upgrade<CropSpeedUpgrade> cropSpeedUpgrade = new Upgrade<>(true, "Crop Growth Rate",
            new Item(XMaterial.WHEAT_SEEDS, 20, 1, "&b&lIsland Crop Growth Rate", Arrays.asList(
                    "",
                    "&7Want to speed farm crops quicker? Buy this",
                    "&7upgrade to speed up your crops growth",
                    "",
                    "&b&lInformation:",
                    "&b&l • &7Current Level: &b%level%",
                    "&b&l • &7Upgrade Cost: &e%mobcoinscost% Mob Coins",
                    "",
                    "&b&lLevels:",
                    "&b&l • &7Level 1: &b10% Increase",
                    "&b&l • &7Level 2: &b20% Increase",
                    "&b&l • &7Level 3: &b30% Increase",
                    "&b&l • &7Level 4: &b40% Increase",
                    "&b&l • &7Level 5: &b50% Increase",
                    "&b&l • &7Level 6: &b60% Increase",
                    "&b&l • &7Level 7: &b70% Increase",
                    "&b&l • &7Level 8: &b80% Increase",
                    "&b&l • &7Level 9: &b90% Increase",
                    "&b&l • &7Level 10: &b100% Increase",
                    "",
                    "&b&l[!] &bClick to Purchase this Upgrade"
            )), ImmutableMap.<Integer, CropSpeedUpgrade>builder()
            .put(1, new CropSpeedUpgrade(0, 0, 1200, 0, 10))
            .put(2, new CropSpeedUpgrade(0, 0, 10500, 0, 20))
            .put(3, new CropSpeedUpgrade(0, 0, 32000, 0, 30))
            .put(4, new CropSpeedUpgrade(0, 0, 90000, 0, 40))
            .put(5, new CropSpeedUpgrade(0, 0, 200000, 0, 50))
            .put(6, new CropSpeedUpgrade(0, 0, 300000, 1, 60))
            .put(7, new CropSpeedUpgrade(0, 0, 400000, 2, 70))
            .put(8, new CropSpeedUpgrade(0, 0, 500000, 3, 80))
            .put(9, new CropSpeedUpgrade(0, 0, 750000, 4, 90))
            .put(10, new CropSpeedUpgrade(0, 0, 1000000, 5, 100))
            .build());

    public Upgrade<MobDropMultiplierUpgrade> mobDropMultiplierUpgrade = new Upgrade<>(true, "Mob Drop Mutliplier",
            new Item(XMaterial.ROTTEN_FLESH, 21, 1, "&b&lIsland Mob Drop Multiplier", Arrays.asList(
                    "",
                    "&7Want to receive more drops from mobs? Buy this",
                    "&7upgrade to multiply all mob drops",
                    "",
                    "&b&lInformation:",
                    "&b&l • &7Current Level: &b%level%",
                    "&b&l • &7Upgrade Cost: &e%mobcoinscost% Mob Coins",
                    "",
                    "&b&lLevels:",
                    "&b&l • &7Level 1: &b1x",
                    "&b&l • &7Level 2: &b2x",
                    "&b&l • &7Level 3: &b3x",
                    "&b&l • &7Level 4: &b4x",
                    "&b&l • &7Level 5: &b5x",
                    "&b&l • &7Level 6: &b6x",
                    "&b&l • &7Level 7: &b7x",
                    "&b&l • &7Level 8: &b8x",
                    "&b&l • &7Level 9: &b9x",
                    "&b&l • &7Level 10: &b10x",
                    "",
                    "&b&l[!] &bClick to Purchase this Upgrade"
            )), ImmutableMap.<Integer, MobDropMultiplierUpgrade>builder()
            .put(1, new MobDropMultiplierUpgrade(0, 0, 0, 0, 1))
            .put(2, new MobDropMultiplierUpgrade(0, 0, 6000, 0, 2))
            .put(3, new MobDropMultiplierUpgrade(0, 0, 35000, 0, 3))
            .put(4, new MobDropMultiplierUpgrade(0, 0, 160000, 0, 4))
            .put(5, new MobDropMultiplierUpgrade(0, 0, 360000, 0, 5))
            .put(6, new MobDropMultiplierUpgrade(0, 0, 600000, 1, 6))
            .put(7, new MobDropMultiplierUpgrade(0, 0, 800000, 2, 7))
            .put(8, new MobDropMultiplierUpgrade(0, 0, 1000000, 3, 8))
            .put(9, new MobDropMultiplierUpgrade(0, 0, 1200000, 4, 9))
            .put(10, new MobDropMultiplierUpgrade(0, 0, 1500000, 5, 10))
            .build());

    public Upgrade<FireResistanceUpgrade> fireResistanceUpgrade = new Upgrade<>(true, "Fire Resistance",
            new Item(XMaterial.FIRE_CHARGE, 22, 1, "&b&lIsland Fire Resistance", Arrays.asList(
                    "",
                    "&7Fed up of being burnt? Buy this",
                    "&7upgrade to receive permanent fire,",
                    "&7resistance on your island",
                    "",
                    "&b&lInformation:",
                    "&b&l • &7Upgrade Cost: &e%mobcoinscost% Mob Coins",
                    "",
                    "&b&l[!] &bClick to Purchase this Upgrade"
            )), ImmutableMap.<Integer, FireResistanceUpgrade>builder()
            .put(1, new FireResistanceUpgrade(0, 0, 0, 0))
            .put(2, new FireResistanceUpgrade(0, 0, 300000, 0))
            .build());

    public Upgrade<WaterBreathingUpgrade> waterBreathingUpgrade = new Upgrade<>(true, "Water Breathing",
            new Item(XMaterial.BUBBLE_CORAL, 23, 1, "&b&lIsland Water Breathing", Arrays.asList(
                    "",
                    "&7Want to breathe under water? Buy this",
                    "&7upgrade to receive permanent water breathing",
                    "",
                    "&b&lInformation:",
                    "&b&l • &7Upgrade Cost: &e%mobcoinscost% Mob Coins",
                    "",
                    "&b&l[!] &bClick to Purchase this Upgrade"
            )), ImmutableMap.<Integer, WaterBreathingUpgrade>builder()
            .put(1, new WaterBreathingUpgrade(0, 0, 0, 0))
            .put(2, new WaterBreathingUpgrade(0, 0, 300000, 0))
            .build());

    public Upgrade<AutoSellChestUpgrade> autoSellChestUpgrade = new Upgrade<>(true, "AutoSell Chest",
            new Item(XMaterial.CHEST, 24, 1, "&b&lIsland AutoSell Chest", Arrays.asList(
                    "",
                    "&7Want to automatically sell items? Buy this upgrade",
                    "&7to reduce sell interval and increase sell value",
                    "",
                    "&b&lInformation:",
                    "&b&l • &7Current Level: &b%level%",
                    "&b&l • &7Upgrade Cost: &e%mobcoinscost% Mob Coins",
                    "&b&l • &7Current Sell Interval: &e%sellInterval% mins",
                    "&b&l • &7Current Sell Value: &e%sellValue%%",
                    "",
                    "&b&l[!] &bClick to Purchase this Upgrade"
            )), ImmutableMap.<Integer, AutoSellChestUpgrade>builder()
            .put(1, new AutoSellChestUpgrade(0, 0, 0, 0, 5, 70))
            .put(2, new AutoSellChestUpgrade(0, 0, 50000, 0, 4, 75))
            .put(3, new AutoSellChestUpgrade(0, 0, 200000, 0, 3, 80))
            .put(4, new AutoSellChestUpgrade(0, 0, 400000, 0, 2, 85))
            .put(5, new AutoSellChestUpgrade(0, 0, 750000, 0, 1, 90))
            .build());

    public Upgrade<InfusedSkullUpgrade> infusedSkullUpgrade = new Upgrade<>(true, "Infused Skull Rate",
            new Item(XMaterial.CREEPER_HEAD, 29, 1, "&b&lIsland Infused Skull Rate", Arrays.asList(
                    "",
                    "&7Want to obtain mob armor? Buy this",
                    "&7upgrade to increase infused skull drops",
                    "",
                    "&b&lInformation:",
                    "&b&l • &7Current Level: &b%level%",
                    "&b&l • &7Upgrade Cost: &e%mobcoinscost% Mob Coins",
                    "",
                    "&b&lLevels:",
                    "&b&l • &7Level 1: &b0.05%",
                    "&b&l • &7Level 2: &b0.1%",
                    "&b&l • &7Level 3: &b0.15%",
                    "&b&l • &7Level 4: &b0.2%",
                    "&b&l • &7Level 5: &b0.25%",
                    "&b&l • &7Level 6: &b0.3%",
                    "",
                    "&b&l[!] &bClick to Purchase this Upgrade"
            )), ImmutableMap.<Integer, InfusedSkullUpgrade>builder()
            .put(1, new InfusedSkullUpgrade(0, 0, 0, 0, 0.05))
            .put(2, new InfusedSkullUpgrade(0, 0, 1200, 0, 0.1))
            .put(3, new InfusedSkullUpgrade(0, 0, 14000, 0, 0.15))
            .put(4, new InfusedSkullUpgrade(0, 0, 48000, 0, 0.2))
            .put(5, new InfusedSkullUpgrade(0, 0, 180000, 0, 0.25))
            .put(6, new InfusedSkullUpgrade(0, 0, 400000, 0, 0.3))
            .build());

    public Upgrade<EnchantSuccessChanceUpgrade> enchantSuccessChanceUpgrade = new Upgrade<>(true, "Enchant Success Chance",
            new Item(XMaterial.GLOWSTONE_DUST, 30, 1, "&b&lIsland Enchant Success Chance", Arrays.asList(
                    "",
                    "&7Want to enchant more items? Buy this",
                    "&7upgrade to increase success chance on books",
                    "",
                    "&b&lInformation:",
                    "&b&l • &7Current Level: &b%level%",
                    "&b&l • &7Upgrade Cost: &e%mobcoinscost% Mob Coins",
                    "",
                    "&b&lLevels:",
                    "&b&l • &7Level 1: &b0%",
                    "&b&l • &7Level 2: &b1%",
                    "&b&l • &7Level 3: &b2%",
                    "&b&l • &7Level 4: &b3%",
                    "&b&l • &7Level 5: &b4%",
                    "&b&l • &7Level 6: &b5%",
                    "&b&l • &7Level 6: &b6%",
                    "&b&l • &7Level 6: &b7%",
                    "&b&l • &7Level 6: &b8%",
                    "&b&l • &7Level 6: &b9%",
                    "&b&l • &7Level 6: &b10%",
                    "",
                    "&b&l[!] &bClick to Purchase this Upgrade"
            )), ImmutableMap.<Integer, EnchantSuccessChanceUpgrade>builder()
            .put(1, new EnchantSuccessChanceUpgrade(0, 0, 0, 0, 0))
            .put(2, new EnchantSuccessChanceUpgrade(0, 0, 1200, 0, 1))
            .put(3, new EnchantSuccessChanceUpgrade(0, 0, 14000, 0, 2))
            .put(4, new EnchantSuccessChanceUpgrade(0, 0, 48000, 0, 3))
            .put(5, new EnchantSuccessChanceUpgrade(0, 0, 180000, 0, 4))
            .put(6, new EnchantSuccessChanceUpgrade(0, 0, 400000, 0, 5))
            .put(7, new EnchantSuccessChanceUpgrade(0, 0, 600000, 0, 6))
            .put(8, new EnchantSuccessChanceUpgrade(0, 0, 800000, 0, 7))
            .put(9, new EnchantSuccessChanceUpgrade(0, 0, 1000000, 0, 8))
            .put(10, new EnchantSuccessChanceUpgrade(0, 0, 1250000, 0, 9))
            .put(11, new EnchantSuccessChanceUpgrade(0, 0, 1500000, 0, 10))
            .build());

    public Upgrade<EnchantSuccessChanceUpgrade> enchantFailureChanceUpgrade = new Upgrade<>(true, "Enchant Failure Chance",
            new Item(XMaterial.REDSTONE, 31, 1, "&b&lIsland Enchant Failure Chance", Arrays.asList(
                    "",
                    "&7Want to stop your items destroying? Buy this",
                    "&7upgrade to decrease failure chance on books",
                    "",
                    "&b&lInformation:",
                    "&b&l • &7Current Level: &b%level%",
                    "&b&l • &7Upgrade Cost: &e%mobcoinscost% Mob Coins",
                    "",
                    "&b&lLevels:",
                    "&b&l • &7Level 1: &b0%",
                    "&b&l • &7Level 2: &b1%",
                    "&b&l • &7Level 3: &b2%",
                    "&b&l • &7Level 4: &b3%",
                    "&b&l • &7Level 5: &b4%",
                    "&b&l • &7Level 6: &b5%",
                    "&b&l • &7Level 6: &b6%",
                    "&b&l • &7Level 6: &b7%",
                    "&b&l • &7Level 6: &b8%",
                    "&b&l • &7Level 6: &b9%",
                    "&b&l • &7Level 6: &b10%",
                    "",
                    "&b&l[!] &bClick to Purchase this Upgrade"
            )), ImmutableMap.<Integer, EnchantSuccessChanceUpgrade>builder()
            .put(1, new EnchantSuccessChanceUpgrade(0, 0, 0, 0, 0))
            .put(2, new EnchantSuccessChanceUpgrade(0, 0, 1200, 0, 1))
            .put(3, new EnchantSuccessChanceUpgrade(0, 0, 14000, 0, 2))
            .put(4, new EnchantSuccessChanceUpgrade(0, 0, 48000, 0, 3))
            .put(5, new EnchantSuccessChanceUpgrade(0, 0, 180000, 0, 4))
            .put(6, new EnchantSuccessChanceUpgrade(0, 0, 400000, 0, 5))
            .put(7, new EnchantSuccessChanceUpgrade(0, 0, 600000, 0, 6))
            .put(8, new EnchantSuccessChanceUpgrade(0, 0, 800000, 0, 7))
            .put(9, new EnchantSuccessChanceUpgrade(0, 0, 1000000, 0, 8))
            .put(10, new EnchantSuccessChanceUpgrade(0, 0, 1250000, 0, 9))
            .put(11, new EnchantSuccessChanceUpgrade(0, 0, 1500000, 0, 10))
            .build());
}
