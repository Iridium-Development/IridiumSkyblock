package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Item;

import java.util.Arrays;
import java.util.Collections;

public class Inventories {
    public String ConfirmationGUITitle = "&7Are you sure?";
    public Item yes = new Item(XMaterial.GREEN_STAINED_GLASS_PANE, 1, "&a&lYes", Collections.emptyList());
    public Item no = new Item(XMaterial.RED_STAINED_GLASS_PANE, 1, "&c&lNo", Collections.emptyList());
    public Item islandInvite = new Item(XMaterial.PLAYER_HEAD, 0, 1, "&e&l{player}", "{player}", Arrays.asList("&7Invited By: {inviter}", "&7Time: {time}", "", "&e&l[!] &7Click to un-invite"));
}
