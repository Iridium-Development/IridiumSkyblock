package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Schematics;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows users to select a schematic.
 * Extended by {@link IslandRegenGUI} and {@link IslandCreateGUI}.
 */
public abstract class SchematicGUI extends GUI {

    private final Map<Integer, Map.Entry<String, Schematics.SchematicConfig>> schematics = new HashMap<>();

    public SchematicGUI() {
        super(IridiumSkyblock.getInstance().getInventories().islandSchematicGUI);
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();

        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().islandSchematicGUI.background);

        for (Map.Entry<String, Schematics.SchematicConfig> entry : IridiumSkyblock.getInstance().getSchematics().schematics.entrySet()) {
            inventory.setItem(entry.getValue().item.slot, ItemStackUtils.makeItem(entry.getValue().item));
            schematics.put(entry.getValue().item.slot, entry);
        }

        if (IridiumSkyblock.getInstance().getConfiguration().backButtons && getPreviousInventory() != null) {
            inventory.setItem(inventory.getSize() + IridiumSkyblock.getInstance().getInventories().backButton.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().backButton));
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
    public abstract void selectSchematic(Map.Entry<String, Schematics.SchematicConfig> schematicConfig);

}
