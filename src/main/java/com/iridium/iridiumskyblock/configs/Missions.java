package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumskyblock.Item;
import com.iridium.iridiumskyblock.Mission;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Missions {
    //Stored as a hashmap to make the configs look better
    public Map<String, Mission> missions = ImmutableMap.<String, Mission>builder()
            .put("farmer", new Mission(new Item(XMaterial.SUGAR_CANE, 1, "&b&lFarmer", Arrays.asList("&7Complete Island Missions to gain rewards", "&7Which can be used to purchase Island Upgrades", "", "&b&lObjectives:", "&b&l* &7Grow 10 Sugarcane: %progress_1%/10", "&b&l* &7Grow 10 Wheat: %progress_2%/10", "&b&l* &7Grow 10 Carrots: %progress_3%/10", "", "&b&lRewards", "&b&l* &72 Island Crystals", "&b&l* &7$1000")), Arrays.asList("GROW:SUGAR_CANE:10", "GROW:WHEAT:10", "GROW:CARROT:10"), Mission.MissionType.DAILY))
            .put("hunter", new Mission(new Item(XMaterial.BONE, 1, "&b&lHunter", Arrays.asList("&7Complete Island Missions to gain rewards", "&7Which can be used to purchase Island Upgrades", "", "&b&lObjectives:", "&b&l* &7Kill 10 Zombies: %progress_1%/10", "&b&l* &7Kill 10 Skeletons: %progress_2%/10", "&b&l* &7Kill 10 Creepers: %progress_3%/10", "", "&b&lRewards", "&b&l* &72 Island Crystals", "&b&l* &7$1000")), Arrays.asList("KILL:ZOMBIE:10", "KILL:SKELETON:10", "KILL:CREEPER:10"), Mission.MissionType.DAILY))
            .put("enchanter", new Mission(new Item(XMaterial.ENCHANTING_TABLE, 1, "&b&lEnchanter", Arrays.asList("&7Complete Island Missions to gain rewards", "&7Which can be used to purchase Island Upgrades", "", "&b&lObjectives:", "&b&l* &7Enchant 10 Items: %progress_1%/10", "", "&b&lRewards", "&b&l* &72 Island Crystals", "&b&l* &7$1000")), Collections.singletonList("ENCHANT:ANY:10"), Mission.MissionType.DAILY))
            .put("baker", new Mission(new Item(XMaterial.BREAD, 1, "&b&lBaker", Arrays.asList("&7Complete Island Missions to gain rewards", "&7Which can be used to purchase Island Upgrades", "", "&b&lObjectives:", "&b&l* &7Bake 64 Bread: %progress_1%/10", "", "&b&lRewards", "&b&l* &72 Island Crystals", "&b&l* &7$1000")), Collections.singletonList("CRAFT:BREAD:64"), Mission.MissionType.DAILY))
            .put("miner", new Mission(new Item(XMaterial.GOLD_ORE, 1, "&b&lMiner", Arrays.asList("&7Complete Island Missions to gain rewards", "&7Which can be used to purchase Island Upgrades", "", "&b&lObjectives:", "&b&l* &7Mine 15 Iron Ores: %progress_1%/10", "&b&l* &7Mine 30 Coal Ores: %progress_2%/10", "&b&l* &7Mine 1 Diamond Ores: %progress_3%/10", "", "&b&lRewards", "&b&l* &72 Island Crystals", "&b&l* &7$1000")), Arrays.asList("MINE:IRON_ORE:15", "MINE:COAL_ORE:30", "MINE:DIAMOND_ORE:1"), Mission.MissionType.DAILY))
            .put("fisherman", new Mission(new Item(XMaterial.FISHING_ROD, 1, "&b&lFisherman", Arrays.asList("&7Complete Island Missions to gain rewards", "&7Which can be used to purchase Island Upgrades", "", "&b&lObjectives:", "&b&l* &7Catch 10 Fish: %progress_1%/10", "", "&b&lRewards", "&b&l* &72 Island Crystals", "&b&l* &7$1000")), Collections.singletonList("FISH:ANY:10"), Mission.MissionType.DAILY))
            .put("blacksmith", new Mission(new Item(XMaterial.IRON_INGOT, 1, "&b&lBlacksmith", Arrays.asList("&7Complete Island Missions to gain rewards", "&7Which can be used to purchase Island Upgrades", "", "&b&lObjectives:", "&b&l* &7Smelt 15 Iron Ores: %progress_1%/10", "&b&l* &7Smelt 30 Coal Ores: %progress_2%/10", "&b&l* &7Smelt 10 Gold Ores: %progress_3%/10", "", "&b&lRewards", "&b&l* &72 Island Crystals", "&b&l* &7$1000")), Arrays.asList("SMELT:IRON_ORE:15", "SMELT:COAL_ORE:30", "SMELT:GOLD_ORE:10"), Mission.MissionType.DAILY))
            .put("potionbrewer", new Mission(new Item(XMaterial.POTION, 1, "&b&lPotion Brewer", Arrays.asList("&7Complete Island Missions to gain rewards", "&7Which can be used to purchase Island Upgrades", "", "&b&lObjectives:", "&b&l* &7Brew 3 Speed II Potions: %progress_1%/3", "&b&l* &7Brew 3 Strength II Potions: %progress_2%/3", "", "&b&lRewards", "&b&l* &72 Island Crystals", "&b&l* &7$1000")), Arrays.asList("BREW:SPEED:2:3", "BREW:STRENGTH:2:3"), Mission.MissionType.DAILY))
            .put("lumberjack", new Mission(new Item(XMaterial.OAK_LOG, 1, "&b&lLumberJack", Arrays.asList("&7Complete Island Missions to gain rewards", "&7Which can be used to purchase Island Upgrades", "", "&b&lObjectives:", "&b&l* &7Collect 32 Logs: %progress_1%/10", "", "&b&lRewards", "&b&l* &72 Island Crystals", "&b&l* &7$1000")), Collections.singletonList("COLLECT:LOG:32"), Mission.MissionType.DAILY))
            .build();

    public List<Integer> dailySlots = Arrays.asList(10, 12, 14, 16);
}
