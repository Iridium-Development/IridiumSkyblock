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
    public Permission bucket = new Permission("bucket", new Item(XMaterial.BUCKET, 12, 1, "&bUse Buckets", Arrays.asList("&7Grant the ability to fill and empty buckets on your island.", "", "&b&lPermission", "{permission}")), IslandRank.MEMBER);
    public Permission doors = new Permission("doors", new Item(XMaterial.OAK_DOOR, 13, 1, "&bUse Doors", Arrays.asList("&7Grant the ability to use doors or trapdoors on your island.", "", "&b&lPermission", "{permission}")), IslandRank.MEMBER);
    public Permission redstone = new Permission("redstone", new Item(XMaterial.REDSTONE, 14, 1, "&bUse Redstone", Arrays.asList("&7Grant the ability to use buttons, levels or pressure plates on your island.", "", "&b&lPermission", "{permission}")), IslandRank.MEMBER);
    public Permission spawners = new Permission("spawners", new Item(XMaterial.SPAWNER, 15, 1, "&bBreak Spawners", Arrays.asList("&7Grant the ability to mine spawners on your island.", "", "&b&lPermission", "{permission}")), IslandRank.MEMBER);
    public Permission openContainers = new Permission("openContainers", new Item(XMaterial.CHEST, 16, 1, "&bOpen Containers", Arrays.asList("&7Grant the ability to open containers on your island.", "", "&b&lPermission", "{permission}")), IslandRank.MEMBER);
    public Permission killMobs = new Permission("killMobs", new Item(XMaterial.DIAMOND_SWORD, 19, 1, "&bKill Mobs", Arrays.asList("&7Grant the ability to kill mobs on your island.", "", "&b&lPermission", "{permission}")), IslandRank.MEMBER);
    //KICK
    //INVITE
    //REGEN
    //Interact Entities
    //Change Permissions
    //Promote
    //Demote
}
