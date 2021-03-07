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
    public Permission redstone = new Permission("redstone", new Item(XMaterial.REDSTONE, 14, 1, "&bUse Redstone", Arrays.asList("&7Grant the ability to use buttons, levels, or pressure plates on your island.", "", "&b&lPermission", "{permission}")), IslandRank.MEMBER);
    public Permission spawners = new Permission("spawners", new Item(XMaterial.SPAWNER, 15, 1, "&bBreak Spawners", Arrays.asList("&7Grant the ability to mine spawners on your island.", "", "&b&lPermission", "{permission}")), IslandRank.MEMBER);
    public Permission openContainers = new Permission("openContainers", new Item(XMaterial.CHEST, 16, 1, "&bOpen Containers", Arrays.asList("&7Grant the ability to open containers on your island.", "", "&b&lPermission", "{permission}")), IslandRank.MEMBER);
    public Permission killMobs = new Permission("killMobs", new Item(XMaterial.DIAMOND_SWORD, 19, 1, "&bKill Mobs", Arrays.asList("&7Grant the ability to kill mobs on your island.", "", "&b&lPermission", "{permission}")), IslandRank.MEMBER);
    public Permission changePermissions = new Permission("changePermissions", new Item(XMaterial.SUNFLOWER, 20, 1, "&bChange Permissions", Arrays.asList("&7Grant the ability to edit island permissions.", "", "&b&lPermission", "{permission}")), IslandRank.CO_OWNER);
    public Permission kick = new Permission("kick", new Item(XMaterial.IRON_BOOTS, 21, 1, "&bKick Users", Arrays.asList("&7Grant the ability to kick island members.", "", "&b&lPermission", "{permission}")), IslandRank.MODERATOR);
    public Permission invite = new Permission("invite", new Item(XMaterial.DIAMOND, 22, 1, "&bInvite Users", Arrays.asList("&7Grant the ability to invite island members.", "", "&b&lPermission", "{permission}")), IslandRank.MODERATOR);
    public Permission regen = new Permission("regen", new Item(XMaterial.TNT, 23, 1, "&bRegenerate Island", Arrays.asList("&7Grant the ability to regenerate your island.", "", "&b&lPermission", "{permission}")), IslandRank.CO_OWNER);
    public Permission promote = new Permission("promote", new Item(XMaterial.PLAYER_HEAD, 24, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Y0NmFiYWQ5MjRiMjIzNzJiYzk2NmE2ZDUxN2QyZjFiOGI1N2ZkZDI2MmI0ZTA0ZjQ4MzUyZTY4M2ZmZjkyIn19fQ==", 1, "&bPromote Users", Arrays.asList("&7Grant the ability to promote users in your island.", "", "&b&lPermission", "{permission}")), IslandRank.CO_OWNER);
    public Permission demote = new Permission("demote", new Item(XMaterial.PLAYER_HEAD, 25, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmU5YWU3YTRiZTY1ZmNiYWVlNjUxODEzODlhMmY3ZDQ3ZTJlMzI2ZGI1OWVhM2ViNzg5YTkyYzg1ZWE0NiJ9fX0=", 1, "&bDemote Users", Arrays.asList("&7Grant the ability to demote users in your island.", "", "&b&lPermission", "{permission}")), IslandRank.CO_OWNER);
    public Permission pickupItems = new Permission("pickupItems", new Item(XMaterial.HOPPER, 28, 1, "&bPickup Items", Arrays.asList("&7Grant the ability to pickup items on your island.", "", "&b&lPermission", "{permission}")), IslandRank.MEMBER);
    public Permission dropItems = new Permission("dropItems", new Item(XMaterial.GHAST_TEAR, 29, 1, "&bDrop Items", Arrays.asList("&7Grant the ability to drop items on your island.", "", "&b&lPermission", "{permission}")), IslandRank.MEMBER);
    public Permission interactEntities = new Permission("interactEntities", new Item(XMaterial.CREEPER_HEAD, 30, 1, "&bInteract with Entities", Arrays.asList("&7Grant the ability to interact with entities on your island.", "", "&b&lPermission", "{permission}")), IslandRank.MEMBER);
}
