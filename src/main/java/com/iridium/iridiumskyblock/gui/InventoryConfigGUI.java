package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.configs.inventories.InventoryConfig;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class InventoryConfigGUI implements GUI {

    private final InventoryConfig inventoryConfig;

    public InventoryConfigGUI(InventoryConfig inventoryConfig) {
        this.inventoryConfig = inventoryConfig;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        for (String command : inventoryConfig.items.keySet()) {
            if (inventoryConfig.items.get(command).slot == event.getSlot()) {
                Bukkit.getServer().dispatchCommand(event.getWhoClicked(), command);
            }
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, inventoryConfig.size, StringUtils.color(inventoryConfig.title));

        InventoryUtils.fillInventory(inventory);

        inventoryConfig.items.values().forEach(item -> inventory.setItem(item.slot, ItemStackUtils.makeItem(item)));
        return inventory;
    }
}
