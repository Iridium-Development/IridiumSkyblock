package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Item;

import java.util.Arrays;
import java.util.Collections;

public class Inventories {
    public String ConfirmationGUITitle = "&7Are you sure?";

    public int visitGuiSize = 54;

    public Item yes = new Item(XMaterial.GREEN_STAINED_GLASS_PANE, 1, "&a&lYes", Collections.emptyList());
    public Item no = new Item(XMaterial.RED_STAINED_GLASS_PANE, 1, "&c&lNo", Collections.emptyList());
    public Item islandInvite = new Item(XMaterial.PLAYER_HEAD, 0, 1, "&b&l{player}", "{player}", Arrays.asList("&7Invited By: {inviter}", "&7Time: {time}", "", "&b&l[!] &7Click to un-invite"));
    public Item islandMember = new Item(XMaterial.PLAYER_HEAD, 0, 1, "&b&l{player}", "{player}", Arrays.asList("&7Joined: {time}", "&7Rank: {rank}", "", "&b&l[!] &7Right Click to promote", "&b&l[!] &7Left click to demote/kick"));
    public Item visit = new Item(XMaterial.PLAYER_HEAD, 1, "&b&l{name}", "{owner}", Arrays.asList("&7Created: {time}", "&7Owner: {owner}"));
    public Item nextPage = new Item(XMaterial.LIME_STAINED_GLASS_PANE, 1, "&a&lNext Page", Collections.emptyList());
    public Item previousPage = new Item(XMaterial.RED_STAINED_GLASS_PANE, 1, "&c&lPrevious Page", Collections.emptyList());
    public Item islandRank = new Item(XMaterial.GREEN_STAINED_GLASS_PANE, 1, "&b&l{rank}", Collections.emptyList());
    public Item topIsland = new Item(XMaterial.PLAYER_HEAD, 1, "&b&lIsland Owner: &f{owner} &7(#{rank})", "{owner}", Arrays.asList("", "&b&l * &7Island Name: &b{name}", "&b&l * &7Island Rank: &b{rank}", "&b&l * &7Island Value: &b{value}", "&b&l * &7Netherite Blocks: &b{NETHERITE_BLOCK_AMOUNT}", "&b&l * &7Emerald Blocks: &b{EMERALD_BLOCK_AMOUNT}", "&b&l * &7Diamond Blocks: &b{DIAMOND_BLOCK_AMOUNT}", "&b&l * &7Gold Blocks: &b{GOLD_BLOCK_AMOUNT}", "&b&l * &7Iron Blocks: &b{IRON_BLOCK_AMOUNT}", "&b&l * &7Hopper Blocks: &b{HOPPER_AMOUNT}", "&b&l * &7Beacon Blocks: &b{BEACON_AMOUNT}", "", "&b&l[!] &bLeft Click to Teleport to this island."));
    public Item topFiller = new Item(XMaterial.BARRIER, 1, " ", Collections.emptyList());
}
