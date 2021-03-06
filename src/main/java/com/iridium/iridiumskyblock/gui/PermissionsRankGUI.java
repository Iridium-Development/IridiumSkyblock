package com.iridium.iridiumskyblock.gui;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.Placeholder;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class PermissionsRankGUI implements GUI {

    private final Island island;

    public PermissionsRankGUI(@NotNull Island island) {
        this.island = island;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        for (int i = 0; i < 5; i++) {
            if (event.getSlot() != i + 11) continue;
            IslandRank islandRank = IslandRank.getByLevel(i);
            if (islandRank != null) {
                event.getWhoClicked().openInventory(new PermissionsGUI(island, islandRank).getInventory());
            }
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, StringUtils.color("&7Island Permissions"));
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
        }
        for (int i = 0; i < 5; i++) {
            IslandRank islandRank = IslandRank.getByLevel(i);
            inventory.setItem(i + 11, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandRank, Collections.singletonList(new Placeholder("rank", islandRank.name()))));
        }
        return inventory;
    }
}
