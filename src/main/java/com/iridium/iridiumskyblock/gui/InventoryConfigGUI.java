package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.configs.inventories.InventoryConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryConfigGUI extends GUI {


    public InventoryConfigGUI(InventoryConfig inventoryConfig) {
        super(inventoryConfig);

    }

    @Override
    public void addContent(Inventory inventory) {
        clearInventory(inventory);

        ((InventoryConfig) getNoItemGUI()).items.values().forEach(item -> inventory.setItem(item.slot, ItemStackUtils.makeItem(item)));
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (isBackButton(event)) return;

        for (String command : ((InventoryConfig) getNoItemGUI()).items.keySet()) {
            if (((InventoryConfig) getNoItemGUI()).items.get(command).slot == event.getSlot()) {
                player.closeInventory();
                Bukkit.getServer().dispatchCommand(player, command);
            }
        }
    }

}
