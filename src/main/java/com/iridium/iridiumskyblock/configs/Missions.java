package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.reflection.XReflection;
import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumteams.Reward;
import com.iridium.iridiumteams.missions.Mission;
import com.iridium.iridiumteams.missions.MissionData;
import com.iridium.iridiumteams.missions.MissionType;

import java.util.Arrays;
import java.util.Collections;

public class Missions extends com.iridium.iridiumteams.configs.Missions {

    public Missions() {
        super("&9");
        this.missions = ImmutableMap.<String, Mission>builder()
                .put("farmer", new Mission(ImmutableMap.<Integer, MissionData>builder()
                        .put(1, new MissionData(new Item(XMaterial.SUGAR_CANE, 1, "&9&lFarmer",
                                Arrays.asList(
                                        "&7Complete Island Missions to gain rewards",
                                        "&7Which can be used to purchase Island Upgrades",
                                        "",
                                        "&9&lObjectives:",
                                        "&9&l* &7Grow 10 Sugarcane: %progress_1%/10",
                                        "&9&l* &7Grow 10 Wheat: %progress_2%/10",
                                        "&9&l* &7Grow 10 Carrots: %progress_3%/10",
                                        "",
                                        "&9&lRewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000",
                                        "",
                                        "&9&l * &7Time Remaining: " + "&9%timeremaining_hours% hours %timeremaining_minutes% minutes and %timeremaining_seconds% seconds"
                                )
                        ), Arrays.asList("GROW:SUGAR_CANE:10", "GROW:WHEAT:10", "GROW:CARROTS:10"), 1, new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lFarmer Reward",
                                Arrays.asList(
                                        "&9&l Rewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000"
                                )), Collections.emptyList(), 1000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(), 0, 10, XSound.ENTITY_PLAYER_LEVELUP),
                                "%prefix% &7Farmer mission Completed!\n" +
                                        "&9&l* &7+3 Island Experience\n" +
                                        "&9&l* &7+5 Island Crystals\n" +
                                        "&9&l* &7+1000 Money\n" +
                                        "&7/is rewards to redeem rewards"
                        )).build(), MissionType.DAILY))

                .put("hunter", new Mission(ImmutableMap.<Integer, MissionData>builder()
                        .put(1, new MissionData(new Item(XMaterial.BONE, 1, "&9&lHunter",
                                Arrays.asList(
                                        "&7Complete Island Missions to gain rewards",
                                        "&7Which can be used to purchase Island Upgrades",
                                        "",
                                        "&9&lObjectives:",
                                        "&9&l* &7Kill 10 Zombies: %progress_1%/10",
                                        "&9&l* &7Kill 10 Skeletons: %progress_2%/10",
                                        "&9&l* &7Kill 10 Creepers: %progress_3%/10",
                                        "",
                                        "&9&lRewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000",
                                        "",
                                        "&9&l * &7Time Remaining: " + "&9%timeremaining_hours% hours %timeremaining_minutes% minutes and %timeremaining_seconds% seconds"
                                )
                        ), Arrays.asList("KILL:ZOMBIE:10", "KILL:SKELETON:10", "KILL:CREEPER:10"), 1, new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lHunter Reward",
                                Arrays.asList(
                                        "&9&l Rewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000"
                                )), Collections.emptyList(), 1000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(), 0, 10, XSound.ENTITY_PLAYER_LEVELUP),
                                "%prefix% &7Hunter mission Completed!\n" +
                                        "&9&l* &7+3 Island Experience\n" +
                                        "&9&l* &7+5 Island Crystals\n" +
                                        "&9&l* &7+1000 Money\n" +
                                        "&7/is rewards to redeem rewards"
                        )).build(), MissionType.DAILY))

                .put("baker", new Mission(ImmutableMap.<Integer, MissionData>builder()
                        .put(1, new MissionData(new Item(XMaterial.BREAD, 1, "&9&lBaker",
                                Arrays.asList(
                                        "&7Complete Island Missions to gain rewards",
                                        "&7Which can be used to purchase Island Upgrades",
                                        "",
                                        "&9&lObjectives:",
                                        "&9&l* &7Bake 64 Bread: %progress_1%/64",
                                        "",
                                        "&9&lRewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000",
                                        "",
                                        "&9&l * &7Time Remaining: " + "&9%timeremaining_hours% hours %timeremaining_minutes% minutes and %timeremaining_seconds% seconds"
                                )
                        ), Collections.singletonList("CRAFT:BREAD:64"), 1, new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lBaker Reward",
                                Arrays.asList(
                                        "&9&l Rewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000"
                                )), Collections.emptyList(), 1000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(), 0, 10, XSound.ENTITY_PLAYER_LEVELUP),
                                "%prefix% &7Baker mission Completed!\n" +
                                        "&9&l* &7+3 Island Experience\n" +
                                        "&9&l* &7+5 Island Crystals\n" +
                                        "&9&l* &7+1000 Money\n" +
                                        "&7/is rewards to redeem rewards"
                        )).build(), MissionType.DAILY))

                .put("miner", new Mission(ImmutableMap.<Integer, MissionData>builder()
                        .put(1, new MissionData(new Item(XMaterial.GOLD_ORE, 1, "&9&lMiner",
                                Arrays.asList(
                                        "&7Complete Island Missions to gain rewards",
                                        "&7Which can be used to purchase Island Upgrades",
                                        "",
                                        "&9&lObjectives:",
                                        "&9&l* &7Mine 15 Iron Ores: %progress_1%/15",
                                        "&9&l* &7Mine 30 Coal Ores: %progress_2%/30",
                                        "&9&l* &7Mine 1 Diamond Ore: %progress_3%/1",
                                        "",
                                        "&9&lRewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000",
                                        "",
                                        "&9&l * &7Time Remaining: " + "&9%timeremaining_hours% hours %timeremaining_minutes% minutes and %timeremaining_seconds% seconds"
                                )
                        ), Arrays.asList("MINE:IRON_ORE:15", "MINE:COAL_ORE:30", "MINE:DIAMOND_ORE:1"), 1, new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lMiner Reward",
                                Arrays.asList(
                                        "&9&l Rewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000"
                                )), Collections.emptyList(), 1000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(), 0, 10, XSound.ENTITY_PLAYER_LEVELUP),
                                "%prefix% &7Miner mission Completed!\n" +
                                        "&9&l* &7+3 Island Experience\n" +
                                        "&9&l* &7+5 Island Crystals\n" +
                                        "&9&l* &7+1000 Money\n" +
                                        "&7/is rewards to redeem rewards"
                        )).build(), MissionType.DAILY))

                .put("fisherman", new Mission(ImmutableMap.<Integer, MissionData>builder()
                        .put(1, new MissionData(new Item(XMaterial.FISHING_ROD, 1, "&9&lFisherman",
                                Arrays.asList(
                                        "&7Complete Island Missions to gain rewards",
                                        "&7Which can be used to purchase Island Upgrades",
                                        "",
                                        "&9&lObjectives:",
                                        "&9&l* &7Catch 10 Fish: %progress_1%/10",
                                        "",
                                        "&9&lRewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000",
                                        "",
                                        "&9&l * &7Time Remaining: " + "&9%timeremaining_hours% hours %timeremaining_minutes% minutes and %timeremaining_seconds% seconds"
                                )
                        ), Collections.singletonList("FISH:ANY:10"), 1, new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lFisherman Reward",
                                Arrays.asList(
                                        "&9&l Rewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000"
                                )), Collections.emptyList(), 1000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(), 0, 10, XSound.ENTITY_PLAYER_LEVELUP),
                                "%prefix% &7Fisherman mission Completed!\n" +
                                        "&9&l* &7+3 Island Experience\n" +
                                        "&9&l* &7+5 Island Crystals\n" +
                                        "&9&l* &7+1000 Money\n" +
                                        "&7/is rewards to redeem rewards"
                        )).build(), MissionType.DAILY))

                .put("blacksmith", new Mission(ImmutableMap.<Integer, MissionData>builder()
                        .put(1, new MissionData(new Item(XMaterial.IRON_INGOT, 1, "&9&lBlacksmith",
                                Arrays.asList(
                                        "&7Complete Island Missions to gain rewards",
                                        "&7Which can be used to purchase Island Upgrades",
                                        "",
                                        "&9&lObjectives:",
                                        "&9&l* &7Smelt 30 Iron Ores: %progress_1%/30",
                                        "&9&l* &7Smelt 15 Gold Ores: %progress_2%/15",
                                        "",
                                        "&9&lRewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000",
                                        "",
                                        "&9&l * &7Time Remaining: " + "&9%timeremaining_hours% hours %timeremaining_minutes% minutes and %timeremaining_seconds% seconds"
                                )), Arrays.asList("SMELT:" + (XReflection.supports(17) ? XMaterial.RAW_IRON.name() : XMaterial.IRON_ORE.name()) + ":30", "SMELT:" + (XReflection.supports(17) ? XMaterial.RAW_GOLD.name() : XMaterial.GOLD_ORE.name()) + ":15"), 1, new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lBlacksmith Reward",
                                Arrays.asList(
                                        "&9&l Rewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000"
                                )), Collections.emptyList(), 1000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(), 0, 10, XSound.ENTITY_PLAYER_LEVELUP),
                                "%prefix% &7Blacksmith mission Completed!\n" +
                                        "&9&l* &7+3 Island Experience\n" +
                                        "&9&l* &7+5 Island Crystals\n" +
                                        "&9&l* &7+1000 Money\n" +
                                        "&7/is rewards to redeem rewards"
                        )).build(), MissionType.DAILY))

                .put("brewer", new Mission(ImmutableMap.<Integer, MissionData>builder()
                        .put(1, new MissionData(new Item(XMaterial.POTION, 1, "&9&lPotion Brewer",
                                Arrays.asList(
                                        "&7Complete Island Missions to gain rewards",
                                        "&7Which can be used to purchase Island Upgrades",
                                        "",
                                        "&9&lObjectives:",
                                        "&9&l* &7Brew 3 Speed II Potions: %progress_1%/3",
                                        "&9&l* &7Brew 3 Strength II Potions: %progress_2%/3",
                                        "",
                                        "&9&lRewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000",
                                        "",
                                        "&9&l * &7Time Remaining: " + "&9%timeremaining_hours% hours %timeremaining_minutes% minutes and %timeremaining_seconds% seconds"
                                )
                        ), Arrays.asList("BREW:SPEED:2:3", "BREW:STRENGTH:2:3"), 1, new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lPotionBrewer Reward",
                                Arrays.asList(
                                        "&9&l Rewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000"
                                )), Collections.emptyList(), 1000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(), 0, 10, XSound.ENTITY_PLAYER_LEVELUP),
                                "%prefix% &7Potion Brewer mission Completed!\n" +
                                        "&9&l* &7+3 Island Experience\n" +
                                        "&9&l* &7+5 Island Crystals\n" +
                                        "&9&l* &7+1000 Money\n" +
                                        "&7/is rewards to redeem rewards"
                        )).build(), MissionType.DAILY))

                .put("mine_oak", new Mission(ImmutableMap.<Integer, MissionData>builder()
                        .put(1, new MissionData(new Item(XMaterial.OAK_LOG, 0, 1, "&9&lMine 10 Logs",
                                Arrays.asList(
                                        "&7Complete Island Missions to gain rewards",
                                        "&7Which can be used to purchase Island Upgrades",
                                        "",
                                        "&9&lObjectives:",
                                        "&9&l* &7Mine 10 logs: %progress_1%/10",
                                        "",
                                        "&9&lRewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000"
                                )
                        ), Collections.singletonList("MINE:LOGS:10"), 1, new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lPotionBrewer Reward",
                                Arrays.asList(
                                        "&9&l Rewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000"
                                )), Collections.emptyList(), 1000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(), 0, 10, XSound.ENTITY_PLAYER_LEVELUP),
                                "%prefix% &7Mission Completed!\n" +
                                        "&9&l* &7+3 Island Experience\n" +
                                        "&9&l* &7+5 Island Crystals\n" +
                                        "&9&l* &7+1000 Money\n" +
                                        "&7/is rewards to redeem rewards"
                        ))
                        .put(2, new MissionData(new Item(XMaterial.OAK_LOG, 0, 1, "&9&lMine 100 Logs",
                                Arrays.asList(
                                        "&7Complete Island Missions to gain rewards",
                                        "&7Which can be used to purchase Island Upgrades",
                                        "",
                                        "&9&lObjectives:",
                                        "&9&l* &7Mine 100 logs: %progress_1%/100",
                                        "",
                                        "&9&lRewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000"
                                )
                        ), Collections.singletonList("MINE:LOGS:100"), 1, new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lPotionBrewer Reward",
                                Arrays.asList(
                                        "&9&l Rewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000"
                                )), Collections.emptyList(), 1000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(), 0, 10, XSound.ENTITY_PLAYER_LEVELUP),
                                "%prefix% &7Mission Completed!\n" +
                                        "&9&l* &7+3 Island Experience\n" +
                                        "&9&l* &7+5 Island Crystals\n" +
                                        "&9&l* &7+1000 Money\n" +
                                        "&7/is rewards to redeem rewards"
                        ))
                        .put(3, new MissionData(new Item(XMaterial.OAK_LOG, 0, 1, "&9&lMine 1000 Logs",
                                Arrays.asList(
                                        "&7Complete Island Missions to gain rewards",
                                        "&7Which can be used to purchase Island Upgrades",
                                        "",
                                        "&9&lObjectives:",
                                        "&9&l* &7Mine 1000 logs: %progress_1%/1000",
                                        "",
                                        "&9&lRewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000"
                                )
                        ), Collections.singletonList("MINE:LOGS:1000"), 1, new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lPotionBrewer Reward",
                                Arrays.asList(
                                        "&9&l Rewards",
                                        "&9&l* &75 Island Crystals",
                                        "&9&l* &7$1000"
                                )), Collections.emptyList(), 1000, new ImmutableMap.Builder<String, Double>().put("Crystals", 5.00).build(), 0, 10, XSound.ENTITY_PLAYER_LEVELUP),
                                "%prefix% &7Mission Completed!\n" +
                                        "&9&l* &7+3 Island Experience\n" +
                                        "&9&l* &7+5 Island Crystals\n" +
                                        "&9&l* &7+1000 Money\n" +
                                        "&7/is rewards to redeem rewards"
                        ))
                        .build(), MissionType.ONCE))
                .build();
    }

}
