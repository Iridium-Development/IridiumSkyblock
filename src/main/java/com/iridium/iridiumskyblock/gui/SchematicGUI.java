package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.gui.BackGUI;
import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumteams.configs.inventories.NoItemGUI;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class SchematicGUI extends BackGUI {

    public SchematicGUI(Inventory previousInventory) {
        super(
                IridiumSkyblock.getInstance().getInventories().islandSchematicGUI.background,
                previousInventory,
                IridiumSkyblock.getInstance().getInventories().backButton
        );
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        NoItemGUI noItemGUI = IridiumSkyblock.getInstance().getInventories().islandSchematicGUI;
        Inventory inventory = Bukkit.createInventory(this, noItemGUI.size, StringUtils.color(noItemGUI.title));
        addContent(inventory);
        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();

        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().islandSchematicGUI.background);

        for (Map.Entry<String, Schematics.SchematicConfig> entry : IridiumSkyblock.getInstance().getSchematics().schematics.entrySet()) {
            inventory.setItem(entry.getValue().item.slot, ItemStackUtils.makeItem(entry.getValue().item));
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        for (Map.Entry<String, Schematics.SchematicConfig> entry : IridiumSkyblock.getInstance().getSchematics().schematics.entrySet()) {
            if (entry.getValue().item.slot != event.getSlot()) continue;
            selectSchematic(entry.getKey());
            event.getWhoClicked().closeInventory();
        }
    }

    public abstract void selectSchematic(String schematic);

}
