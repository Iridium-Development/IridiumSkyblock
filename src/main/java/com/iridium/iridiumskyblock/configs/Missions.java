package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Item;
import com.iridium.iridiumskyblock.Mission;

import java.util.Arrays;
import java.util.List;

public class Missions {
    public List<Mission> missions = Arrays.asList(
            new Mission("cobblegen", new Item(XMaterial.COBBLESTONE, 0, 1, "&b&lMine Cobblestone", Arrays.asList("&7Mine 100 cobblestone", "&7Progress: &b{progress}/100")), "MINE:COBBLESTONE:100", Mission.MissionType.ONCE),
            new Mission("enchanter", new Item(XMaterial.ENCHANTING_TABLE, 0, 1, "&b&lEnchanter", Arrays.asList("&7Enchant 10 items", "&7Progress: &b{progress}/10")), "ENCHANT:ANY:10", Mission.MissionType.DAILY),
            new Mission("sugarCane", new Item(XMaterial.SUGAR_CANE, 0, 1, "&b&lFarmer", Arrays.asList("&7Harvest 100 Sugar Cane", "&7Progress: &b{progress}/100")), "MINE:SUGARCANE:100", Mission.MissionType.DAILY),
            new Mission("miner", new Item(XMaterial.COBBLESTONE, 0, 1, "&b&lMiner", Arrays.asList("&7Mine 100 Blocks", "&7Progress: &b{progress}/1000")), "MINE:ANY:1000", Mission.MissionType.DAILY),
            new Mission("builder", new Item(XMaterial.OAK_LOG, 0, 1, "&b&lBuilder", Arrays.asList("&7Place 100 Blocks", "&7Progress: &b{progress}/100")), "PLACE:ANY:100", Mission.MissionType.DAILY),
            new Mission("hunter", new Item(XMaterial.BLAZE_ROD, 0, 1, "&b&lHunter", Arrays.asList("&7Kill 100 Mobs", "&7Progress: &b{progress}/100")), "KILL:ANY:100", Mission.MissionType.DAILY)
    );

    public List<Integer> dailySlots = Arrays.asList(11, 13, 15);
}
