package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Background;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnore;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumteams.configs.inventories.InventoryConfig;
import com.iridium.iridiumteams.configs.inventories.SingleItemGUI;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Inventories extends com.iridium.iridiumteams.configs.Inventories {
    @JsonIgnore
    private final Background background1 = new Background(new HashMap<>());

    public InventoryConfig islandMenu = new InventoryConfig(45, "&7Island Menu", background1, ImmutableMap.<String, Item>builder()
            .put("is home", new Item(XMaterial.WHITE_BED, 13, 1, "&9&lIsland Home", Collections.singletonList("&7Teleport to your island home")))
            .put("is boosters", new Item(XMaterial.EXPERIENCE_BOTTLE, 21, 1, "&9&lIsland Boosters", Collections.singletonList("&7View your island boosters")))
            .put("is members", new Item(XMaterial.PLAYER_HEAD, 22, 1, "&9&lIsland Members", "Peaches_MLG", Collections.singletonList("&7View your island members")))
            .put("is warps", new Item(XMaterial.END_PORTAL_FRAME, 23, 1, "&9&lIsland Warps", Collections.singletonList("&7View your island warps")))
            .put("is upgrade", new Item(XMaterial.DIAMOND, 29, 1, "&9&lIsland Upgrades", Collections.singletonList("&7View your island upgrades")))
            .put("is missions", new Item(XMaterial.IRON_SWORD, 30, 1, "&9&lIsland Missions", Collections.singletonList("&7View your island missions")))
            .put("is bank", new Item(XMaterial.PLAYER_HEAD, 31, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODM4MWM1MjlkNTJlMDNjZDc0YzNiZjM4YmI2YmEzZmRlMTMzN2FlOWJmNTAzMzJmYWE4ODllMGEyOGU4MDgxZiJ9fX0", 1, "&9&lIsland Bank", Collections.singletonList("&7View your island bank")))
            .put("is permissions", new Item(XMaterial.WRITABLE_BOOK, 32, 1, "&9&lIsland Permissions", Collections.singletonList("&7View your island permissions")))
            .put("is delete", new Item(XMaterial.BARRIER, 33, 1, "&9&lDelete Island", Collections.singletonList("&7Delete your island")))
            .build()
    );

    public SingleItemGUI visitGUI = new SingleItemGUI(54, "&7Visit an Island", background1, new Item(XMaterial.PLAYER_HEAD, 1, "&9&l%island_name%", "%island_owner%", Arrays.asList(
            "&9Created: &7%island_create%",
            "&9Owner: &7%island_owner%"
    )));

    public Inventories() {
        super("Island", "&9");
    }
}
