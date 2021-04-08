package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * GUI which allows to select the type of valuable for the {@link BlockValueGUI}.
 */
public class BlockValueSelectGUI implements GUI {

    /**
     * Builds and returns this inventory.
     *
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().blockValueSelectGuiSize, StringUtils.color(IridiumSkyblock.getInstance().getInventories().blockValueSelectGUITitle));

        InventoryUtils.fillInventory(inventory);

        inventory.setItem(IridiumSkyblock.getInstance().getInventories().blockValue.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().blockValue));
        inventory.setItem(IridiumSkyblock.getInstance().getInventories().spawnerBlockValue.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().spawnerBlockValue));

        return inventory;
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() == IridiumSkyblock.getInstance().getInventories().blockValue.slot) {
            event.getWhoClicked().openInventory(new BlockValueGUI(BlockValueGUI.BlockValueType.BLOCK).getInventory());
        } else if (event.getSlot() == IridiumSkyblock.getInstance().getInventories().spawnerBlockValue.slot) {
            event.getWhoClicked().openInventory(new BlockValueGUI(BlockValueGUI.BlockValueType.SPAWNER).getInventory());
        }
    }

}
