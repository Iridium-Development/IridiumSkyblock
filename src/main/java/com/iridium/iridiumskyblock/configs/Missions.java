package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Item;
import com.iridium.iridiumskyblock.Mission;

import java.util.Arrays;
import java.util.List;

public class Missions {
    public List<Mission> missions = Arrays.asList(
            new Mission("cobblegen", new Item(XMaterial.COBBLESTONE, 0, 1, "&b&lMine Cobblestone", Arrays.asList("&7Mine 100 cobblestone", "&7Progress: &b{progress}/100")), "MINE:COBBLESTONE:100", Mission.MissionType.ONCE)
    );
}
