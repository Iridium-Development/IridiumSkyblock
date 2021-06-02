package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Schematics;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows users to select a schematic.
 * Extended by {@link IslandRegenGUI} and {@link IslandCreateGUI}.
 */
public abstract class SchematicGUI implements GUI {

    private final HashMap<Integer, Schematics.SchematicConfig> schematics = new HashMap<>();

    /**
     * Builds and returns this inventory.
     *
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().islandSchematicGUI.size, StringUtils.color(IridiumSkyblock.getInstance().getInventories().islandSchematicGUI.title));

        addContent(inventory);

        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();

        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().islandSchematicGUI.background);

        for (Map.Entry<String, Schematics.SchematicConfig> entry : IridiumSkyblock.getInstance().getSchematics().schematics.entrySet()) {
            inventory.setItem(entry.getValue().item.slot, ItemStackUtils.makeItem(entry.getValue().item));
            schematics.put(entry.getValue().item.slot, entry.getValue());
        }
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (!schematics.containsKey(event.getSlot())) return;
        selectSchematic(schematics.get(event.getSlot()));
        event.getWhoClicked().closeInventory();
    }

    /**
     * Executed when the player selects the Island schematic.
     *
     * @param schematicConfig The data of the selected schematic
     */
    public abstract void selectSchematic(Schematics.SchematicConfig schematicConfig);

}
