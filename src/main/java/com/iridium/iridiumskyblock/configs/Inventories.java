package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumskyblock.InventoryConfig;
import com.iridium.iridiumskyblock.Item;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Inventories {

    public InventoryConfig islandBorder = new InventoryConfig(27, "&7Island Border", ImmutableMap.<String, Item>builder()
            .put("is border blue", new Item(XMaterial.BLUE_STAINED_GLASS_PANE, 10, 1, "&b&lBlue", Collections.emptyList()))
            .put("is border red", new Item(XMaterial.RED_STAINED_GLASS_PANE, 12, 1, "&c&lRed", Collections.emptyList()))
            .put("is border green", new Item(XMaterial.GREEN_STAINED_GLASS_PANE, 14, 1, "&a&lGreen", Collections.emptyList()))
            .put("is border off", new Item(XMaterial.WHITE_STAINED_GLASS_PANE, 16, 1, "&f&lOff", Collections.emptyList()))
            .build()
    );

    public InventoryConfig islandMenu = new InventoryConfig(45, "&7Island Menu", ImmutableMap.<String, Item>builder()
            .put("is biomes", new Item(XMaterial.PLAYER_HEAD, 12, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTI4OWQ1YjE3ODYyNmVhMjNkMGIwYzNkMmRmNWMwODVlODM3NTA1NmJmNjg1YjVlZDViYjQ3N2ZlODQ3MmQ5NCJ9fX0=", 1, "&b&lIsland Biomes", Collections.singletonList("&7Change your island Biome")))
            .put("is home", new Item(XMaterial.WHITE_BED, 13, 1, "&b&lIsland Home", Collections.singletonList("&7Teleport to your island home")))
            .put("is members", new Item(XMaterial.PLAYER_HEAD, 14, 1, "&b&lIsland Home", "Peaches_MLG", Collections.singletonList("&7View your island members")))
            .put("is warps", new Item(XMaterial.END_PORTAL_FRAME, 20, 1, "&b&lIsland Warps", Collections.singletonList("&7View your island warps")))
            .put("is upgrade", new Item(XMaterial.DIAMOND, 21, 1, "&b&lIsland Upgrades", Collections.singletonList("&7View your island upgrades")))
            .put("is missions", new Item(XMaterial.IRON_SWORD, 22, 1, "&b&lIsland Missions", Collections.singletonList("&7View your island missions")))
            .put("is border", new Item(XMaterial.BEACON, 24, 1, "&b&lIsland Border", Collections.singletonList("&7Change your island border")))
            .put("is bank", new Item(XMaterial.PLAYER_HEAD, 30, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODM4MWM1MjlkNTJlMDNjZDc0YzNiZjM4YmI2YmEzZmRlMTMzN2FlOWJmNTAzMzJmYWE4ODllMGEyOGU4MDgxZiJ9fX0", 1, "&b&lIsland Bank", Collections.singletonList("&7View your island bank")))
            .put("is permissions", new Item(XMaterial.WRITABLE_BOOK, 31, 1, "&b&lIsland Permissions", Collections.singletonList("&7View your island permissions")))
            .put("is trusted", new Item(XMaterial.NAME_TAG, 32, 1, "&b&lTrusted Members", Collections.singletonList("&7View your island's trusted members")))
            .put("is regen", new Item(XMaterial.GRASS_BLOCK, 36, 1, "&b&lIsland Regen", Collections.singletonList("&7Regenerate your island"))).put("is boosters", new Item(XMaterial.EXPERIENCE_BOTTLE, 23, 1, "&b&lIsland Boosters", Collections.singletonList("&7View your island boosters")))
            .put("is delete", new Item(XMaterial.BARRIER, 44, 1, "&b&lDelete Island", Collections.singletonList("&7Delete your island")))
            .build()
    );

    public String ConfirmationGUITitle = "&7Are you sure?";
    public String blockValueSelectGUITitle = "&7Block Values";
    public String blockValueGUITitle = "&7Block Values";

    public int visitGuiSize = 54;
    public int missionsGUISize = 54;
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
    public Item topIsland = new Item(XMaterial.PLAYER_HEAD, 1, "&b&lIsland Owner: &f%owner% &7(#%rank%)", "%owner%", Arrays.asList("", "&b&l * &7Island Name: &b%name%", "&b&l * &7Island Rank: &b%rank%", "&b&l * &7Island Value: &b%value%", "&b&l * &7Netherite Blocks: &b%NETHERITE_BLOCK_AMOUNT%", "&b&l * &7Emerald Blocks: &b%EMERALD_BLOCK_AMOUNT%", "&b&l * &7Diamond Blocks: &b%DIAMOND_BLOCK_AMOUNT%", "&b&l * &7Gold Blocks: &b%GOLD_BLOCK_AMOUNT%", "&b&l * &7Iron Blocks: &b%IRON_BLOCK_AMOUNT%", "&b&l * &7Hopper Blocks: &b%HOPPER_AMOUNT%", "&b&l * &7Beacon Blocks: &b%BEACON_AMOUNT%", "", "&b&l[!] &bLeft Click to Teleport to this Island."));
    public Item topFiller = new Item(XMaterial.BARRIER, 1, " ", Collections.emptyList());
    public Item dailyQuests = new Item(XMaterial.PLAYER_HEAD, 11, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE2YWU1YjM0YzRmNzlhNWY5ZWQ2Y2NjMzNiYzk4MWZjNDBhY2YyYmZjZDk1MjI2NjRmZTFjNTI0ZDJlYjAifX19", 1, "&b&lDaily Missions", Collections.emptyList());
    public Item oneTimeQuests = new Item(XMaterial.PLAYER_HEAD, 15, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE2YWU1YjM0YzRmNzlhNWY5ZWQ2Y2NjMzNiYzk4MWZjNDBhY2YyYmZjZDk1MjI2NjRmZTFjNTI0ZDJlYjAifX19", 1, "&b&lQuests", Collections.emptyList());
    public Item blockValue = new Item(XMaterial.EMERALD_BLOCK, 11, 1, "&b&lValuable Blocks", Collections.emptyList());
    public Item spawnerBlockValue = new Item(XMaterial.SPAWNER, 15, 1, "&b&lValuable Spawners", Collections.emptyList());

}
