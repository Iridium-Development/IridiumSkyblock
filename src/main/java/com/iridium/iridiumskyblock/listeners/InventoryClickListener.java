package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.gui.GUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        //Old Inventories
        if (event.getClickedInventory() != null && event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof GUI) {
            event.setCancelled(true);
            if (event.getClickedInventory() == event.getInventory()) {
                GUI gui = (GUI) event.getInventory().getHolder();
                if (IridiumSkyblock.getInstance().getConfiguration().backButtons && gui.getPreviousInventory() != null && event.getSlot() == (event.getInventory().getSize() + IridiumSkyblock.getInstance().getInventories().backButton.slot)) {
                    event.getWhoClicked().openInventory(gui.getPreviousInventory());
                } else {
                    gui.onInventoryClick(event);
                }
            }
        }

        // New Inventories with IridiumCore
        if (event.getClickedInventory() != null && event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof com.iridium.iridiumcore.gui.GUI) {
            event.setCancelled(true);
            if (event.getClickedInventory() == event.getInventory()) {
                ((com.iridium.iridiumcore.gui.GUI) event.getInventory().getHolder()).onInventoryClick(event);
            }
        }
    }
}
