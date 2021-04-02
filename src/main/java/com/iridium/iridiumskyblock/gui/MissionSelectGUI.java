package com.iridium.iridiumskyblock.gui;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class MissionSelectGUI implements GUI {

    private final Island island;

    public MissionSelectGUI(@NotNull Island island) {
        this.island = island;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() == 15) {
            event.getWhoClicked().openInventory(new MissionsGUI(island, Mission.MissionType.DAILY).getInventory());
        } else if (event.getSlot() == 11) {
            event.getWhoClicked().openInventory(new MissionsGUI(island, Mission.MissionType.ONCE).getInventory());
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, StringUtils.color("&7Island Missions"));
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
        }
        inventory.setItem(11, ItemStackUtils.makeItem(XMaterial.COBBLESTONE, 1, "&b&lDaily Missions", Collections.emptyList()));
        inventory.setItem(15, ItemStackUtils.makeItem(XMaterial.COBBLESTONE, 1, "&b&lQuests", Collections.emptyList()));
        return inventory;
    }
}
