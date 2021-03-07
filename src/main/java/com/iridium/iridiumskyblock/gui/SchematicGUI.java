package com.iridium.iridiumskyblock.gui;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public abstract class SchematicGUI implements GUI {

    /**
     * A class that gets extended by IslandRegenGUI and IslandCreateGUI
     */

    private final HashMap<Integer, Schematics.SchematicConfig> schematics = new HashMap<>();

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, StringUtils.color("&7Select a Schematic"));
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
        }
        for (Schematics.SchematicConfig schematicConfig : IridiumSkyblock.getInstance().getSchematics().schematics) {
            inventory.setItem(schematicConfig.item.slot, ItemStackUtils.makeItem(schematicConfig.item));
            schematics.put(schematicConfig.item.slot, schematicConfig);
        }
        return inventory;
    }

    public abstract void selectSchematic(Schematics.SchematicConfig schematicConfig);

    public void onInventoryClick(InventoryClickEvent event) {
        if (schematics.containsKey(event.getSlot())) {
            selectSchematic(schematics.get(event.getSlot()));
            event.getWhoClicked().closeInventory();
        }
    }
}
