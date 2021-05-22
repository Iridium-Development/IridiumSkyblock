package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumskyblock.Item;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.Reward;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The mission configuration used by IridiumSkyblock (missions.yml).
 * Is deserialized automatically on plugin startup and reload.
 */
public class Missions {

    public Map<String, Mission> missions = ImmutableMap.<String, Mission>builder()
            //DAILY MISSIONS
            .put("farmer", new Mission(new Item(XMaterial.SUGAR_CANE, 1, "&9&lFarmer",
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
                            "&9&l* &72 Island Crystals",
                            "&9&l* &7$1000"
                    )), Arrays.asList("GROW:SUGAR_CANE:10", "GROW:WHEAT:10", "GROW:CARROTS:10"), Mission.MissionType.DAILY, new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lFarmer Reward",
                    Arrays.asList(
                            "&9&l Rewards",
                            "&9&l* &73 Island Crystals"
                    )), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Farmer mission Completed!",
                    "&9&l* &7+5 Island Experience",
                    "&9&l* &7+3 Island Crystals",
                    "&7/is rewards to redeem rewards"
            )))

            .put("hunter", new Mission(new Item(XMaterial.BONE, 1, "&9&lHunter",
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
                            "&9&l* &72 Island Crystals",
                            "&9&l* &7$1000"
                    )), Arrays.asList("KILL:ZOMBIE:10", "KILL:SKELETON:10", "KILL:CREEPER:10"), Mission.MissionType.DAILY, new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lHunter Reward",
                    Arrays.asList(
                            "&9&l Rewards",
                            "&9&l* &73 Island Crystals"
                    )), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Hunter mission Completed!",
                    "&9&l* &7+5 Island Experience",
                    "&9&l* &7+3 Island Crystals",
                    "&7/is rewards to redeem rewards"
            )))

            .put("baker", new Mission(new Item(XMaterial.BREAD, 1, "&9&lBaker",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&9&lObjectives:",
                            "&9&l* &7Bake 64 Bread: %progress_1%/64",
                            "",
                            "&9&lRewards",
                            "&9&l* &72 Island Crystals",
                            "&9&l* &7$1000"
                    )), Collections.singletonList("CRAFT:BREAD:64"), Mission.MissionType.DAILY, new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lBaker Reward",
                    Arrays.asList(
                            "&9&l Rewards",
                            "&9&l* &73 Island Crystals"
                    )), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Baker mission Completed!",
                    "&9&l* &7+5 Island Experience",
                    "&9&l* &7+3 Island Crystals",
                    "&7/is rewards to redeem rewards"
            )))

            .put("miner", new Mission(new Item(XMaterial.GOLD_ORE, 1, "&9&lMiner",
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
                            "&9&l* &72 Island Crystals",
                            "&9&l* &7$1000"
                    )), Arrays.asList("MINE:IRON_ORE:15", "MINE:COAL_ORE:30", "MINE:DIAMOND_ORE:1"), Mission.MissionType.DAILY, new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lMiner Reward",
                    Arrays.asList(
                            "&9&l Rewards",
                            "&9&l* &73 Island Crystals"
                    )), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Miner mission Completed!",
                    "&9&l* &7+5 Island Experience",
                    "&9&l* &7+3 Island Crystals",
                    "&7/is rewards to redeem rewards"
            )))

            .put("fisherman", new Mission(new Item(XMaterial.FISHING_ROD, 1, "&9&lFisherman",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&9&lObjectives:",
                            "&9&l* &7Catch 10 Fish: %progress_1%/10",
                            "",
                            "&9&lRewards",
                            "&9&l* &72 Island Crystals",
                            "&9&l* &7$1000"
                    )), Collections.singletonList("FISH:ANY:10"), Mission.MissionType.DAILY, new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lFisherman Reward",
                    Arrays.asList(
                            "&9&l Rewards",
                            "&9&l* &73 Island Crystals"
                    )), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Fisherman mission Completed!",
                    "&9&l* &7+5 Island Experience",
                    "&9&l* &7+3 Island Crystals",
                    "&7/is rewards to redeem rewards"
            )))

            .put("blacksmith", new Mission(new Item(XMaterial.IRON_INGOT, 1, "&9&lBlacksmith",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&9&lObjectives:",
                            "&9&l* &7Smelt 30 Iron Ores: %progress_1%/30",
                            "&9&l* &7Smelt 15 Gold Ores: %progress_2%/15",
                            "",
                            "&9&lRewards",
                            "&9&l* &72 Island Crystals",
                            "&9&l* &7$1000"
                    )), Arrays.asList("SMELT:IRON_ORE:30", "SMELT:GOLD_ORE:15"), Mission.MissionType.DAILY, new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lBlacksmith Reward",
                    Arrays.asList(
                            "&9&l Rewards",
                            "&9&l* &73 Island Crystals"
                    )), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Blacksmith mission Completed!",
                    "&9&l* &7+5 Island Experience",
                    "&9&l* &7+3 Island Crystals",
                    "&7/is rewards to redeem rewards"
            )))

            .put("potionbrewer", new Mission(new Item(XMaterial.POTION, 1, "&9&lPotion Brewer",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&9&lObjectives:",
                            "&9&l* &7Brew 3 Speed II Potions: %progress_1%/3",
                            "&9&l* &7Brew 3 Strength II Potions: %progress_2%/3",
                            "",
                            "&9&lRewards",
                            "&9&l* &72 Island Crystals",
                            "&9&l* &7$1000"
                    )
            ), Arrays.asList("BREW:SPEED:2:3", "BREW:STRENGTH:2:3"), Mission.MissionType.DAILY, new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lPotionBrewer Reward",
                    Arrays.asList(
                            "&9&l Rewards",
                            "&9&l* &73 Island Crystals"
                    )), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Potion Brewer mission Completed!",
                    "&9&l* &7+5 Island Experience",
                    "&9&l* &7+3 Island Crystals",
                    "&7/is rewards to redeem rewards"
            )))

            //QUESTS
            .put("lumberjack", new Mission(new Item(XMaterial.OAK_LOG, 1, "&9&lLumberjack",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&9&lObjectives:",
                            "&9&l* &7Chop down 16 logs: %progress_1%/16",
                            "",
                            "&9&lRewards",
                            "&9&l* &72 Island Crystals",
                            "&9&l* &7$1000"
                    )
            ), Collections.singletonList("MINE:OAK_LOG:16"), Mission.MissionType.ONCE,
                    new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lLumberjack Reward",
                            Arrays.asList(
                                    "&9&l Rewards",
                                    "&9&l* &73 Island Crystals",
                                    "&9&l* &7$1000"
                            )), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Lumberjack mission Completed!",
                    "&9&l* &7+5 Island Experience",
                    "&9&l* &7+3 Island Crystals",
                    "&7/is rewards to redeem rewards"
            )))

            .put("crafting", new Mission(new Item(XMaterial.CRAFTING_TABLE, 1, "&9&lCrafting",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&9&lObjectives:",
                            "&9&l* &7Craft a crafting table: %progress_1%/1",
                            "&9&l* &7Craft 4 furnaces: %progress_2%/4",
                            "&9&l* &7Craft 4 chests: %progress_3%/4",
                            "",
                            "&9&lRewards",
                            "&9&l* &72 Island Crystals",
                            "&9&l* &7$1000"
                    )
            ), Arrays.asList("CRAFT:CRAFTING_TABLE:1", "CRAFT:FURNACE:4", "CRAFT:CHEST:4"), Mission.MissionType.ONCE,
                    new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lCrafting Reward",
                            Arrays.asList(
                                    "&9&l Rewards",
                                    "&9&l* &73 Island Crystals",
                                    "&9&l* &7$1000"
                            )), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Crafting Completed!",
                    "&9&l* &7+5 Island Experience",
                    "&9&l* &7+3 Island Crystals",
                    "&7/is rewards to redeem rewards"
            )))

            .put("mining", new Mission(new Item(XMaterial.COBBLESTONE, 1, "&9&lMiner",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&9&lObjectives:",
                            "&9&l* &7Create a cobble generator and mine 100 cobblestone: %progress_1%/100",
                            "",
                            "&9&lRewards",
                            "&9&l* &72 Island Crystals",
                            "&9&l* &7$1000"
                    )
            ), Collections.singletonList("MINE:COBBLESTONE:100"), Mission.MissionType.ONCE,
                    new Reward(new Item(XMaterial.DIAMOND, 1, "&9&lMining Reward",
                            Arrays.asList(
                                    "&9&l Rewards",
                                    "&9&l* &73 Island Crystals",
                                    "&9&l* &7$1000"
                            )), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Mining Completed!",
                    "&9&l* &7+5 Island Experience",
                    "&9&l* &7+3 Island Crystals",
                    "&7/is rewards to redeem rewards"
            )))

            .build();

    public List<Integer> dailySlots = Arrays.asList(10, 12, 14, 16);

}
