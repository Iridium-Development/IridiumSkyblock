package com.iridium.iridiumskyblock.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Represents a clickable GUI.
 * Base for all other classes in this package.
 */
public interface GUI extends InventoryHolder {

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    void onInventoryClick(InventoryClickEvent event);

    /**
     * Called when updating the Inventories contents
     */
    void addContent(Inventory inventory);

}
