package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Background;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnore;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.inventories.BorderInventoryConfig;
//import com.iridium.iridiumskyblock.gui.BiomeCategoryGUI;
//import com.iridium.iridiumskyblock.gui.BiomeOverviewGUI;
import com.iridium.iridiumskyblock.gui.BiomeCategoryGUI;
import com.iridium.iridiumskyblock.gui.BiomeOverviewGUI;
import com.iridium.iridiumteams.configs.inventories.InventoryConfig;
import com.iridium.iridiumteams.configs.inventories.NoItemGUI;
import com.iridium.iridiumteams.configs.inventories.SingleItemGUI;
import org.bukkit.inventory.Inventory;

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

    public InventoryConfig islandMenu = new InventoryConfig(54, "&7Island Menu", background1, ImmutableMap.<String, Item>builder()
            .put("is trusted", new Item(XMaterial.NAME_TAG, 10, 1, "&b&lTrusted Members", Collections.singletonList("&7View your island's trusted members")))
            .put("is members", new Item(XMaterial.PLAYER_HEAD, 11, 1, "&b&lIsland Members", "Peaches_MLG", Collections.singletonList("&7View your island members")))
            .put("is permissions", new Item(XMaterial.WRITABLE_BOOK, 19, 1, "&b&lIsland Permissions", Collections.singletonList("&7View your island permissions")))
            .put("is warps", new Item(XMaterial.END_PORTAL_FRAME, 20, 1, "&b&lIsland Warps", Collections.singletonList("&7View your island warps")))

            .put("is shop", new Item(XMaterial.NETHER_STAR, 30, 1, "&b&lIsland Shop", Collections.singletonList("&7Open the Island Shop")))
            .put("is home", new Item(XMaterial.WHITE_BED, 22, 1, "&b&lIsland Home", Collections.singletonList("&7Teleport to your island home")))
            .put("is bank", new Item(XMaterial.PLAYER_HEAD, 32, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODM4MWM1MjlkNTJlMDNjZDc0YzNiZjM4YmI2YmEzZmRlMTMzN2FlOWJmNTAzMzJmYWE4ODllMGEyOGU4MDgxZiJ9fX0", 1, "&b&lIsland Bank", Collections.singletonList("&7View your island bank")))

            .put("is biomes", new Item(XMaterial.GRASS_BLOCK, 15, 1, "&b&lIsland Biomes", Collections.singletonList("&7Change your island biome")))
            .put("is boosters", new Item(XMaterial.EXPERIENCE_BOTTLE, 16, 1, "&b&lIsland Boosters", Collections.singletonList("&7View your island boosters")))
            .put("is missions", new Item(XMaterial.IRON_SWORD, 24, 1, "&b&lIsland Missions", Collections.singletonList("&7View your island missions")))
            .put("is upgrade", new Item(XMaterial.DIAMOND, 25, 1, "&b&lIsland Upgrades", Collections.singletonList("&7View your island upgrades")))

            .put("is border", new Item(XMaterial.BEACON, 44, 1, "&b&lIsland Border", Collections.singletonList("&7Change your island border")))
            .put("is regen", new Item(XMaterial.CAMPFIRE, 52, 1, "&b&lIsland Regen", Collections.singletonList("&7Regenerate your island")))
            .put("is delete", new Item(XMaterial.BARRIER, 53, 1, "&b&lDelete Island", Collections.singletonList("&7Delete your island")))
            .build()
    );

    public SingleItemGUI visitGUI = new SingleItemGUI(54, "&7Visit an Island", background1, new Item(XMaterial.PLAYER_HEAD, 1, "&9&l%island_name%", "%island_owner%", Arrays.asList(
            "&9Created: &7%island_create%",
            "&9Owner: &7%island_owner%"
    )));

    public NoItemGUI islandSchematicGUI = new NoItemGUI(27, "&7Select a Schematic", background2);

    public NoItemGUI biomeOverviewGUI = new NoItemGUI(54, "Biomes", background1);
    public NoItemGUI biomeCategoryGUI = new NoItemGUI(54, "Biomes - %biome_category_name%", background2);

    public Inventories() {
        super("Island", "&9");
        missionTypeSelectorGUI.weekly.enabled = false;
    }
}
