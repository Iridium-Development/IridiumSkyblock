package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.dependencies.xseries.XSound;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Missions {

    public Map<String, Mission> missions = ImmutableMap.<String, Mission>builder()
            //DAILY MISSIONS
            .put("farmer", new Mission(new Item(XMaterial.SUGAR_CANE, 1, "&b&lFarmer",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Grow 10 Sugarcane: %progress_1%/10",
                            "&b&l* &7Grow 10 Wheat: %progress_2%/10",
                            "&b&l* &7Grow 10 Carrots: %progress_3%/10",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Arrays.asList("GROW:SUGAR_CANE:10", "GROW:WHEAT:10", "GROW:CARROTS:10"), Mission.MissionType.DAILY, new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lFarmer Reward",
                    Arrays.asList(
                            "&b&l Rewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Farmer mission Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            .put("hunter", new Mission(new Item(XMaterial.BONE, 1, "&b&lHunter",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Kill 10 Zombies: %progress_1%/10",
                            "&b&l* &7Kill 10 Skeletons: %progress_2%/10",
                            "&b&l* &7Kill 10 Creepers: %progress_3%/10",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Arrays.asList("KILL:ZOMBIE:10", "KILL:SKELETON:10", "KILL:CREEPER:10"), Mission.MissionType.DAILY, new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lHunter Reward",
                    Arrays.asList(
                            "&b&l Rewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Hunter mission Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            .put("baker", new Mission(new Item(XMaterial.BREAD, 1, "&b&lBaker",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Bake 64 Bread: %progress_1%/64",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.singletonList("CRAFT:BREAD:64"), Mission.MissionType.DAILY, new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lBaker Reward",
                    Arrays.asList(
                            "&b&l Rewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Baker mission Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            .put("miner", new Mission(new Item(XMaterial.GOLD_ORE, 1, "&b&lMiner",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Mine 15 Iron Ores: %progress_1%/15",
                            "&b&l* &7Mine 30 Coal Ores: %progress_2%/30",
                            "&b&l* &7Mine 1 Diamond Ore: %progress_3%/1",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Arrays.asList("MINE:IRON_ORE:15", "MINE:COAL_ORE:30", "MINE:DIAMOND_ORE:1"), Mission.MissionType.DAILY, new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lMiner Reward",
                    Arrays.asList(
                            "&b&l Rewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Miner mission Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            .put("fisherman", new Mission(new Item(XMaterial.FISHING_ROD, 1, "&b&lFisherman",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Catch 10 Fish: %progress_1%/10",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.singletonList("FISH:ANY:10"), Mission.MissionType.DAILY, new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lFisherman Reward",
                    Arrays.asList(
                            "&b&l Rewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Fisherman mission Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            .put("blacksmith", new Mission(new Item(XMaterial.IRON_INGOT, 1, "&b&lBlacksmith",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Smelt 30 Iron Ores: %progress_1%/30",
                            "&b&l* &7Smelt 15 Gold Ores: %progress_2%/15",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Arrays.asList("SMELT:" + (XMaterial.supports(17) ? XMaterial.RAW_IRON.name() : XMaterial.IRON_ORE.name()) + ":30", "SMELT:" + (XMaterial.supports(17) ? XMaterial.RAW_GOLD.name() : XMaterial.GOLD_ORE.name()) + ":15"), Mission.MissionType.DAILY, new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lBlacksmith Reward",
                    Arrays.asList(
                            "&b&l Rewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Blacksmith mission Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            .put("potionbrewer", new Mission(new Item(XMaterial.POTION, 1, "&b&lPotion Brewer",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Brew 3 Speed II Potions: %progress_1%/3",
                            "&b&l* &7Brew 3 Strength II Potions: %progress_2%/3",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null
            ), Arrays.asList("BREW:SPEED:2:3", "BREW:STRENGTH:2:3"), Mission.MissionType.DAILY, new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lPotionBrewer Reward",
                    Arrays.asList(
                            "&b&l Rewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Potion Brewer mission Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            //QUESTS
            .put("lumberjack", new Mission(new Item(XMaterial.OAK_LOG, 1, "&b&lLumberjack",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Chop down 16 logs: %progress_1%/16",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null
            ), Collections.singletonList("MINE:LOGS:16"), Mission.MissionType.ONCE,
                    new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lLumberjack Reward",
                            Arrays.asList(
                                    "&b&l Rewards",
                                    "&b&l* &75 Island Crystals",
                                    "&b&l* &7$1000"
                            ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Lumberjack mission Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            .put("crafting", new Mission(new Item(XMaterial.CRAFTING_TABLE, 1, "&b&lCrafting",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Craft a crafting table: %progress_1%/1",
                            "&b&l* &7Craft 4 furnaces: %progress_2%/4",
                            "&b&l* &7Craft 4 chests: %progress_3%/4",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null
            ), Arrays.asList("CRAFT:CRAFTING_TABLE:1", "CRAFT:FURNACE:4", "CRAFT:CHEST:4"), Mission.MissionType.ONCE,
                    new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lCrafting Reward",
                            Arrays.asList(
                                    "&b&l Rewards",
                                    "&b&l* &75 Island Crystals",
                                    "&b&l* &7$1000"
                            ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Crafting Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            .put("mining", new Mission(new Item(XMaterial.COBBLESTONE, 1, "&b&lMiner",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Create a cobble generator and mine 100 cobblestone: %progress_1%/100",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null
            ), Collections.singletonList("MINE:COBBLESTONE:100"), Mission.MissionType.ONCE,
                    new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lMining Reward",
                            Arrays.asList(
                                    "&b&l Rewards",
                                    "&b&l* &75 Island Crystals",
                                    "&b&l* &7$1000"
                            ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Mining Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            .put("killMobs", new Mission(new Item(XMaterial.DIAMOND_SWORD, 1, "&b&lHunter",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Kill 10 Zombies: %progress_1%/10",
                            "&b&l* &7Kill 10 Skeletons: %progress_2%/10",
                            "&b&l* &7Kill 10 Creepers: %progress_3%/10",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Arrays.asList("KILL:ZOMBIE:10", "KILL:SKELETON:10", "KILL:CREEPER:10"), Mission.MissionType.ONCE, new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lHunter Reward",
                    Arrays.asList(
                            "&b&l Rewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Hunter mission Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            .put("wheat", new Mission(new Item(XMaterial.WHEAT, 1, "&b&lWheat Farmer",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Grow 64 Wheat: %progress_1%/64",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.singletonList("GROW:WHEAT:64"), Mission.MissionType.ONCE, new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lWheat Farmer Reward",
                    Arrays.asList(
                            "&b&l Rewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Wheat Farmer mission Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            .put("carrots", new Mission(new Item(XMaterial.CARROT, 1, "&b&lCarrot Farmer",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Grow 64 Carrots: %progress_1%/64",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.singletonList("GROW:CARROTS:64"), Mission.MissionType.ONCE, new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lCarrot Farmer Reward",
                    Arrays.asList(
                            "&b&l Rewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Carrot Farmer mission Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            .put("melon", new Mission(new Item(XMaterial.MELON_SLICE, 1, "&b&lMelon Farmer",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Grow 64 Melons: %progress_1%/64",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.singletonList("GROW:MELON:64"), Mission.MissionType.ONCE, new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lMelon Farmer Reward",
                    Arrays.asList(
                            "&b&l Rewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Melon Farmer mission Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            .put("sugarcane", new Mission(new Item(XMaterial.SUGAR_CANE, 1, "&b&lSugar Cane Farmer",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Grow 64 Sugar Cane: %progress_1%/64",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.singletonList("GROW:SUGAR_CANE:64"), Mission.MissionType.ONCE, new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lSugar Cane Farmer Reward",
                    Arrays.asList(
                            "&b&l Rewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Sugar Cane Farmer mission Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            .put("cactus", new Mission(new Item(XMaterial.CACTUS, 1, "&b&lCactus Farmer",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Grow 64 Cactus: %progress_1%/64",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.singletonList("GROW:CACTUS:64"), Mission.MissionType.ONCE, new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lCactus Farmer Reward",
                    Arrays.asList(
                            "&b&l Rewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Cactus Farmer mission Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            .put("iron", new Mission(new Item(XMaterial.IRON_ORE, 1, "&b&lMine & Smelt Iron",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Fine and mine 32 Iron Ores: %progress_1%/32",
                            "&b&l* &7Smelt 32 Iron Ingots: %progress_2%/32",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null
            ), Arrays.asList("MINE:IRON_ORE:32", "SMELT:" + (XMaterial.supports(17) ? XMaterial.RAW_IRON.name() : XMaterial.IRON_ORE.name()) + ":32"), Mission.MissionType.ONCE,
                    new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lMining Reward",
                            Arrays.asList(
                                    "&b&l Rewards",
                                    "&b&l* &75 Island Crystals",
                                    "&b&l* &7$1000"
                            ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Mining and smelting Iron Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            .put("diamonds", new Mission(new Item(XMaterial.DIAMOND_ORE, 1, "&b&lMine Diamonds",
                    Arrays.asList(
                            "&7Complete Island Missions to gain rewards",
                            "&7Which can be used to purchase Island Upgrades",
                            "",
                            "&b&lObjectives:",
                            "&b&l* &7Fine and mine 5 diamond ore: %progress_1%/5",
                            "",
                            "&b&lRewards",
                            "&b&l* &75 Island Crystals",
                            "&b&l* &7$1000"
                    ), null
            ), Collections.singletonList("MINE:DIAMOND_ORE:5"), Mission.MissionType.ONCE,
                    new Reward(new Item(XMaterial.DIAMOND, 1, "&b&lMining Reward",
                            Arrays.asList(
                                    "&b&l Rewards",
                                    "&b&l* &75 Island Crystals",
                                    "&b&l* &7$1000"
                            ), null), Collections.emptyList(), 3, 5, 1000, 0, XSound.ENTITY_PLAYER_LEVELUP), Arrays.asList(
                    "%prefix% &7Mining Diamonds Completed!",
                    "&b&l* &7+3 Island Experience",
                    "&b&l* &7+5 Island Crystals",
                    "&b&l* &7+1000 Money",
                    "&7/is rewards to redeem rewards"
            )))

            .build();

    public List<Integer> dailySlots = Arrays.asList(10, 12, 14, 16);

    public Map<String, List<String>> customMaterialLists = ImmutableMap.<String, List<String>>builder()
            // CUSTOM LISTS
            .put("LOGS", Arrays.asList(
                    "OAK_LOG",
                    "BIRCH_LOG",
                    "SPRUCE_LOG",
                    "DARK_OAK_LOG",
                    "ACACIA_LOG",
                    "JUNGLE_LOG",
                    "CRIMSON_STEM",
                    "WARPED_STEM"
            ))
            .build();

}
