package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Item;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Inventories {

    public String ConfirmationGUITitle = "&7Are you sure?";
    public String blockValueSelectGUITitle = "&7Block Values";
    public String blockValueGUITitle = "&7Block Values";

    public int visitGuiSize = 54;
    public int blockValueSelectGuiSize = 27;
    public int blockValueGuiSize = 27;

    public List<String> blockValueLore = Collections.singletonList("&bValue per block: &7%value%");

    public Item filler = new Item(XMaterial.BLACK_STAINED_GLASS_PANE, 1, " ", Collections.emptyList());
    public Item yes = new Item(XMaterial.GREEN_STAINED_GLASS_PANE, 1, "&a&lYes", Collections.emptyList());
    public Item no = new Item(XMaterial.RED_STAINED_GLASS_PANE, 1, "&c&lNo", Collections.emptyList());
    public Item islandInvite = new Item(XMaterial.PLAYER_HEAD, 0, 1, "&b&l%player%", "%player%", Arrays.asList("&7Invited By: %inviter%", "&7Time: %time%", "", "&b&l[!] &7Click to un-invite"));
    public Item islandMember = new Item(XMaterial.PLAYER_HEAD, 0, 1, "&b&l%player%", "%player%", Arrays.asList("&7Joined: %time%", "&7Rank: %rank%", "", "&b&l[!] &7Right Click to promote", "&b&l[!] &7Left click to demote/kick"));
    public Item visit = new Item(XMaterial.PLAYER_HEAD, 1, "&b&l%name%", "%owner%", Arrays.asList("&7Created: %time%", "&7Owner: %owner%"));
    public Item nextPage = new Item(XMaterial.LIME_STAINED_GLASS_PANE, 1, "&a&lNext Page", Collections.emptyList());
    public Item previousPage = new Item(XMaterial.RED_STAINED_GLASS_PANE, 1, "&c&lPrevious Page", Collections.emptyList());
    public Item islandRank = new Item(XMaterial.GREEN_STAINED_GLASS_PANE, 1, "&b&l%rank%", Collections.emptyList());
    public Item topIsland = new Item(XMaterial.PLAYER_HEAD, 1, "&b&lIsland Owner: &f%owner% &7(#%rank%)", "%owner%", Arrays.asList("", "&b&l * &7Island Name: &b%name%", "&b&l * &7Island Rank: &b%rank%", "&b&l * &7Island Value: &b%value%", "&b&l * &7Netherite Blocks: &b%NETHERITE_BLOCK_AMOUNT%", "&b&l * &7Emerald Blocks: &b%EMERALD_BLOCK_AMOUNT%", "&b&l * &7Diamond Blocks: &b%DIAMOND_BLOCK_AMOUNT%", "&b&l * &7Gold Blocks: &b%GOLD_BLOCK_AMOUNT%", "&b&l * &7Iron Blocks: &b%IRON_BLOCK_AMOUNT%", "&b&l * &7Hopper Blocks: &b%HOPPER_AMOUNT%", "&b&l * &7Beacon Blocks: &b%BEACON_AMOUNT%", "", "&b&l[!] &bLeft Click to Teleport to this island."));
    public Item topFiller = new Item(XMaterial.BARRIER, 1, " ", Collections.emptyList());
    public Item blockValue = new Item(XMaterial.EMERALD_BLOCK, 11, 1, "&b&lValuable Blocks", Collections.emptyList());
    public Item spawnerBlockValue = new Item(XMaterial.SPAWNER, 15, 1, "&b&lValuable Spawners", Collections.emptyList());

}
