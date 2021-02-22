package com.iridium.iridiumskyblock.gui.schematicgui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SchematicGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() != null) {
            if (event.getClickedInventory().getHolder() instanceof SchematicGUI) {
                event.setCancelled(true);
                SchematicGUI schematicGUI = (SchematicGUI) event.getClickedInventory().getHolder();
                if (schematicGUI.getSchematics().containsKey(event.getSlot())) {
                    schematicGUI.createIsland(schematicGUI.getSchematics().get(event.getSlot()));
                    event.getWhoClicked().closeInventory();
                }
            }
        }
    }

}
