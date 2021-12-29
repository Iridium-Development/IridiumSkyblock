package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.inventories.InventoryConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryConfigGUI extends GUI {

    private final InventoryConfig inventoryConfig;

    public InventoryConfigGUI(InventoryConfig inventoryConfig, Inventory previousInventory) {
        super(inventoryConfig, previousInventory);
        this.inventoryConfig = inventoryConfig;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, inventoryConfig.background);

        inventoryConfig.items.values().forEach(item -> inventory.setItem(item.slot, ItemStackUtils.makeItem(item)));


        if (IridiumSkyblock.getInstance().getConfiguration().backButtons && getPreviousInventory() != null) {
            inventory.setItem(inventory.getSize() + IridiumSkyblock.getInstance().getInventories().backButton.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().backButton));
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        for (String command : inventoryConfig.items.keySet()) {
            if (inventoryConfig.items.get(command).slot == event.getSlot()) {
                if(command.equalsIgnoreCase("is missions")) {
                    ((Player) event.getWhoClicked()).chat("/is missions");
                    return;
                }
                Bukkit.getServer().dispatchCommand(event.getWhoClicked(), command);
            }
        }
    }

}
