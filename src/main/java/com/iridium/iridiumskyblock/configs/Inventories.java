package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Item;

import java.util.Arrays;
import java.util.Collections;

public class Inventories {
    public String ConfirmationGUITitle = "&7Are you sure?";
    public Item yes = new Item(XMaterial.GREEN_STAINED_GLASS_PANE, 1, "&a&lYes", Collections.emptyList());
    public Item no = new Item(XMaterial.RED_STAINED_GLASS_PANE, 1, "&c&lNo", Collections.emptyList());
    public Item islandInvite = new Item(XMaterial.PLAYER_HEAD, 0, 1, "&b&l{player}", "{player}", Arrays.asList("&7Invited By: {inviter}", "&7Time: {time}", "", "&b&l[!] &7Click to un-invite"));
    public Item islandMember = new Item(XMaterial.PLAYER_HEAD, 0, 1, "&b&l{player}", "{player}", Arrays.asList("&7Joined: {time}", "&7Rank: {rank}", "", "&b&l[!] &7Right Click to promote", "&b&l[!] &7Left click to demote/kick"));
}
