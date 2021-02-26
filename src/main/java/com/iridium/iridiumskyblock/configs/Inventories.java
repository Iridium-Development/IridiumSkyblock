package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Item;

import java.util.Arrays;
import java.util.Collections;

public class Inventories {
    public String ConfirmationGUITitle = "&7Are you sure?";

    public int visitGuiSize = 45;

    public Item yes = new Item(XMaterial.GREEN_STAINED_GLASS_PANE, 1, "&a&lYes", Collections.emptyList());
    public Item no = new Item(XMaterial.RED_STAINED_GLASS_PANE, 1, "&c&lNo", Collections.emptyList());
    public Item islandInvite = new Item(XMaterial.PLAYER_HEAD, 0, 1, "&b&l{player}", "{player}", Arrays.asList("&7Invited By: {inviter}", "&7Time: {time}", "", "&b&l[!] &7Click to un-invite"));
    public Item islandMember = new Item(XMaterial.PLAYER_HEAD, 0, 1, "&b&l{player}", "{player}", Arrays.asList("&7Joined: {time}", "&7Rank: {rank}", "", "&b&l[!] &7Right Click to promote", "&b&l[!] &7Left click to demote/kick"));
    public Item visit = new Item(XMaterial.PLAYER_HEAD, 1, "&c&l{name}", Collections.emptyList());
    public Item nextPage = new Item(XMaterial.LIME_STAINED_GLASS_PANE, 1, "&a&lNext Page", Collections.emptyList());
    public Item previousPage = new Item(XMaterial.RED_STAINED_GLASS_PANE, 1, "&c&lPrevious Page", Collections.emptyList());


}
