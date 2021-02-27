package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.Item;
import com.iridium.iridiumskyblock.Permission;

import java.util.Arrays;

public class Permissions {
    public String allowed = "&a&lALLOWED";
    public String denied = "&c&lDENIED";
    public Permission blockBreak = new Permission("blockBreak", new Item(XMaterial.DIAMOND_PICKAXE, 10, 1, "&bBreak Blocks", Arrays.asList("&7Grant the ability to break any blocks on your island.", "", "&b&lPermission", "{permission}")), IslandRank.MEMBER);
    public Permission blockPlace = new Permission("blockPlace", new Item(XMaterial.GRASS_BLOCK, 11, 1, "&bPlace Blocks", Arrays.asList("&7Grant the ability to place any blocks on your island.", "", "&b&lPermission", "{permission}")), IslandRank.MEMBER);
}
