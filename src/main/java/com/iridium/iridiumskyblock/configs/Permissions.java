package com.iridium.iridiumskyblock.configs;

import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.Permission;

import java.util.Arrays;

/**
 * The Island permission configuration used by IridiumSkyblock (permissions.yml).
 * Is deserialized automatically on plugin startup and reload.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Permissions {

    public String allowed = "&a&lALLOWED";
    public String denied = "&c&lDENIED";
    public Permission blockBreak = new Permission(true, new Item(XMaterial.DIAMOND_PICKAXE, 10, 1, "&bBreak Blocks", Arrays.asList("&7Grant the ability to break any blocks on your Island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission blockPlace = new Permission(true, new Item(XMaterial.GRASS_BLOCK, 11, 1, "&bPlace Blocks", Arrays.asList("&7Grant the ability to place any blocks on your Island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission bucket = new Permission(true, new Item(XMaterial.BUCKET, 12, 1, "&bUse Buckets", Arrays.asList("&7Grant the ability to fill and empty buckets on your Island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission doors = new Permission(true, new Item(XMaterial.OAK_DOOR, 13, 1, "&bUse Doors", Arrays.asList("&7Grant the ability to use doors or trapdoors on your Island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission redstone = new Permission(true, new Item(XMaterial.REDSTONE, 14, 1, "&bUse Redstone", Arrays.asList("&7Grant the ability to use buttons, levels, or pressure plates on your Island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission spawners = new Permission(true, new Item(XMaterial.SPAWNER, 15, 1, "&bBreak Spawners", Arrays.asList("&7Grant the ability to mine spawners on your Island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission openContainers = new Permission(true, new Item(XMaterial.CHEST, 16, 1, "&bOpen Containers", Arrays.asList("&7Grant the ability to open containers on your Island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission killMobs = new Permission(true, new Item(XMaterial.DIAMOND_SWORD, 19, 1, "&bKill Mobs", Arrays.asList("&7Grant the ability to kill mobs on your Island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission changePermissions = new Permission(true, new Item(XMaterial.SUNFLOWER, 20, 1, "&bChange Permissions", Arrays.asList("&7Grant the ability to edit Island permissions.", "", "&b&lPermission", "%permission%")), 1, IslandRank.CO_OWNER);
    public Permission kick = new Permission(true, new Item(XMaterial.IRON_BOOTS, 21, 1, "&bKick Users", Arrays.asList("&7Grant the ability to kick Island members.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MODERATOR);
    public Permission invite = new Permission(true, new Item(XMaterial.DIAMOND, 22, 1, "&bInvite Users", Arrays.asList("&7Grant the ability to invite Island members.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MODERATOR);
    public Permission regen = new Permission(true, new Item(XMaterial.TNT, 23, 1, "&bRegenerate Island", Arrays.asList("&7Grant the ability to regenerate your Island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.CO_OWNER);
    public Permission promote = new Permission(true, new Item(XMaterial.PLAYER_HEAD, 24, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Y0NmFiYWQ5MjRiMjIzNzJiYzk2NmE2ZDUxN2QyZjFiOGI1N2ZkZDI2MmI0ZTA0ZjQ4MzUyZTY4M2ZmZjkyIn19fQ==", 1, "&bPromote Users", Arrays.asList("&7Grant the ability to promote users in your island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.CO_OWNER);
    public Permission demote = new Permission(true, new Item(XMaterial.PLAYER_HEAD, 25, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmU5YWU3YTRiZTY1ZmNiYWVlNjUxODEzODlhMmY3ZDQ3ZTJlMzI2ZGI1OWVhM2ViNzg5YTkyYzg1ZWE0NiJ9fX0=", 1, "&bDemote Users", Arrays.asList("&7Grant the ability to demote users in your island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.CO_OWNER);
    public Permission pickupItems = new Permission(true, new Item(XMaterial.HOPPER, 28, 1, "&bPickup Items", Arrays.asList("&7Grant the ability to pickup items on your island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission dropItems = new Permission(true, new Item(XMaterial.GHAST_TEAR, 29, 1, "&bDrop Items", Arrays.asList("&7Grant the ability to drop items on your island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission interactEntities = new Permission(true, new Item(XMaterial.CREEPER_HEAD, 30, 1, "&bInteract with Entities", Arrays.asList("&7Grant the ability to interact with entities on your island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission manageWarps = new Permission(true, new Item(XMaterial.END_PORTAL_FRAME, 31, 1, "&bManage Warps", Arrays.asList("&7Grant the ability to create/edit/delete warps for your island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission withdrawBank = new Permission(true, new Item(XMaterial.GOLD_INGOT, 32, 1, "&bWithdraw Bank", Arrays.asList("&7Grants the ability to withdraw money from the Island Bank.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission trust = new Permission(true, new Item(XMaterial.EMERALD, 33, 1, "&bTrust Users", Arrays.asList("&7Grant the ability to manage Island Trusts.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MODERATOR);
    public Permission border = new Permission(true, new Item(XMaterial.BEACON, 34, 1, "&bIsland Border", Arrays.asList("&7Grant the ability to Change the Island border.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission expel = new Permission(true, new Item(XMaterial.LEATHER_BOOTS, 37, 1, "&bExpel Visitors", Arrays.asList("&7Grant the ability to expel visitors from your Island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MODERATOR);
    public Permission ban = new Permission(true, new Item(XMaterial.BARRIER, 38, 1, "&bBan Visitors", Arrays.asList("&7Grant the ability to ban visitors from your Island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MODERATOR);
    public Permission unban = new Permission(true, new Item(XMaterial.MAGMA_CREAM, 39, 1, "&bUn-Ban Visitors", Arrays.asList("&7Grant the ability to unban visitors on your island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MODERATOR);
    public Permission destroyVehicle = new Permission(true, new Item(XMaterial.MINECART, 40, 1, "&bDestroy Vehicles", Arrays.asList("&7Grant the ability to destroy vehicles on your island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission trampleCrops = new Permission(true, new Item(XMaterial.WHEAT_SEEDS, 41, 1, "&bTrample Crops", Arrays.asList("&7Grant the ability to trample crops on your island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission interact = new Permission(true, new Item(XMaterial.STONE_BUTTON, 42, 1, "&bInteract", Arrays.asList("&7Grant the ability to interact with blocks on your island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission portal = new Permission(true, new Item(XMaterial.OBSIDIAN, 43, 1, "&bPortal", Arrays.asList("&7Grants the ability to use a portal on the island.", "", "&b&lPermission", "%permission%")), 1, IslandRank.MEMBER);
    public Permission islandSettings = new Permission(true, new Item(XMaterial.BOOKSHELF, 10, 1, "&bIsland Settings", Arrays.asList("&7Grant the ability to change the settings of your Island.", "", "&b&lPermission", "%permission%")), 2, IslandRank.MODERATOR);
}
