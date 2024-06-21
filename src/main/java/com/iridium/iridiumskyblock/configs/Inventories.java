package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Background;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnore;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.configs.inventories.BorderInventoryConfig;
import com.iridium.iridiumteams.configs.inventories.InventoryConfig;
import com.iridium.iridiumteams.configs.inventories.NoItemGUI;
import com.iridium.iridiumteams.configs.inventories.SingleItemGUI;

import java.util.Arrays;
import java.util.Collections;

public class Inventories extends com.iridium.iridiumteams.configs.Inventories {

    @JsonIgnore
    private final Background background1 = new Background(ImmutableMap.<Integer, Item>builder().build());
    @JsonIgnore
    private final Background background2 = new Background(ImmutableMap.<Integer, Item>builder()
            .put(9, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()))
            .put(10, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()))
            .put(11, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()))
            .put(12, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()))
            .put(13, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()))
            .put(14, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()))
            .put(15, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()))
            .put(16, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()))
            .put(17, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList()))
            .build());

    public BorderInventoryConfig islandBorderGUI = new BorderInventoryConfig(27, "&7Island Border", background2,
            new Item(XMaterial.BLUE_STAINED_GLASS_PANE, 10, 1, "&9&lBlue", Collections.emptyList()),
            new Item(XMaterial.RED_STAINED_GLASS_PANE, 12, 1, "&c&lRed", Collections.emptyList()),
            new Item(XMaterial.GREEN_STAINED_GLASS_PANE, 14, 1, "&a&lGreen", Collections.emptyList()),
            new Item(XMaterial.WHITE_STAINED_GLASS_PANE, 16, 1, "&f&lOff", Collections.emptyList())
    );

    public InventoryConfig islandMenu = new InventoryConfig(45, "&7Island Menu", background1, ImmutableMap.<String, Item>builder()
            .put("is regen", new Item(XMaterial.GRASS_BLOCK, 12, 1, "&9&lIsland Regen", Collections.singletonList("&7Regenerate your island")))
            .put("is boosters", new Item(XMaterial.EXPERIENCE_BOTTLE, 23, 1, "&9&lIsland Boosters", Collections.singletonList("&7View your island boosters")))
            .put("is home", new Item(XMaterial.WHITE_BED, 13, 1, "&9&lIsland Home", Collections.singletonList("&7Teleport to your island home")))
            .put("is members", new Item(XMaterial.PLAYER_HEAD, 14, "Peaches_MLG", 1, "&9&lIsland Members", Collections.singletonList("&7View your island members")))
            .put("is warps", new Item(XMaterial.END_PORTAL_FRAME, 20, 1, "&9&lIsland Warps", Collections.singletonList("&7View your island warps")))
            .put("is upgrades", new Item(XMaterial.DIAMOND, 21, 1, "&9&lIsland Upgrades", Collections.singletonList("&7View your island upgrades")))
            .put("is missions", new Item(XMaterial.IRON_SWORD, 22, 1, "&9&lIsland Missions", Collections.singletonList("&7View your island missions")))
            .put("is border", new Item(XMaterial.BEACON, 24, 1, "&9&lIsland Border", Collections.singletonList("&7Change your island border")))
            .put("is bank", new Item(XMaterial.PLAYER_HEAD, 30, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODM4MWM1MjlkNTJlMDNjZDc0YzNiZjM4YmI2YmEzZmRlMTMzN2FlOWJmNTAzMzJmYWE4ODllMGEyOGU4MDgxZiJ9fX0", 1, "&9&lIsland Bank", Collections.singletonList("&7View your island bank")))
            .put("is permissions", new Item(XMaterial.WRITABLE_BOOK, 31, 1, "&9&lIsland Permissions", Collections.singletonList("&7View your island permissions")))
            .put("is invites", new Item(XMaterial.NAME_TAG, 32, 1, "&9&lIsland Invites", Collections.singletonList("&7View your island invites")))
            .put("is delete", new Item(XMaterial.BARRIER, 44, 1, "&9&lDelete Island", Collections.singletonList("&7Delete your island")))
            .build()
    );

    public SingleItemGUI visitGUI = new SingleItemGUI(54, "&7Visit an Island", background1, new Item(XMaterial.PLAYER_HEAD, 0, "%island_owner%", 1, "&9&l%island_name%", Arrays.asList(
            "&9Created: &7%island_create%",
            "&9Owner: &7%island_owner%"
    )));

    public NoItemGUI islandSchematicGUI = new NoItemGUI(27, "&7Select a Schematic", background2);
    public NoItemGUI biomeOverviewGUI = new NoItemGUI(27, "Biomes", background2);
    public NoItemGUI biomeCategoryGUI = new NoItemGUI(54, "Biomes - %biome_category_name%", background1);

    public Inventories() {
        super("Island", "&9");
        missionTypeSelectorGUI.weekly.enabled = false;
    }
}
