package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.configs.inventories.InventoryConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryConfigGUI extends GUI {

    private final InventoryConfig inventoryConfig;

    public InventoryConfigGUI(InventoryConfig inventoryConfig) {
        super(inventoryConfig);
        this.inventoryConfig = inventoryConfig;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, inventoryConfig.background);

        inventoryConfig.items.values().forEach(item -> inventory.setItem(item.slot, ItemStackUtils.makeItem(item)));
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        for (String command : inventoryConfig.items.keySet()) {
            if (inventoryConfig.items.get(command).slot == event.getSlot()) {
                event.getWhoClicked().closeInventory();
                Bukkit.getServer().dispatchCommand(event.getWhoClicked(), command);
            }
        }
    }

}
