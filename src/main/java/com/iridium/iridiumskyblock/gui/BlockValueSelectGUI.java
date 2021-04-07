package com.iridium.iridiumskyblock.gui;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class BlockValueSelectGUI implements GUI {

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() == IridiumSkyblock.getInstance().getInventories().blockValue.slot) {
            event.getWhoClicked().openInventory(new BlockValueGUI(BlockValueGUI.BlockValueType.BLOCK).getInventory());
        } else if (event.getSlot() == IridiumSkyblock.getInstance().getInventories().spawnerBlockValue.slot) {
            event.getWhoClicked().openInventory(new BlockValueGUI(BlockValueGUI.BlockValueType.SPAWNER).getInventory());
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().blockValueSelectGuiSize, StringUtils.color(IridiumSkyblock.getInstance().getInventories().blockValueSelectGUITitle));

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().filler));
        }

        inventory.setItem(IridiumSkyblock.getInstance().getInventories().blockValue.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().blockValue));
        inventory.setItem(IridiumSkyblock.getInstance().getInventories().spawnerBlockValue.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().spawnerBlockValue));

        return inventory;
    }

}
