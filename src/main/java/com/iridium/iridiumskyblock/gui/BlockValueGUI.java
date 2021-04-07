package com.iridium.iridiumskyblock.gui;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlockValueGUI implements GUI {

    private final @NotNull BlockValueType guiType;

    public BlockValueGUI(BlockValueType type) {
        this.guiType = type;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        // Don't do anything here, it gets cancelled automatically
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().blockValueGuiSize, StringUtils.color(IridiumSkyblock.getInstance().getInventories().blockValueGUITitle));

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().filler));
        }

        if (guiType == BlockValueType.BLOCK) {
            Iterator<Integer> slots = IridiumSkyblock.getInstance().getBlockValues().blockSlots.iterator();
            for (Map.Entry<XMaterial, Double> valuableBlock : IridiumSkyblock.getInstance().getBlockValues().blockValues.entrySet()) {
                XMaterial material = valuableBlock.getKey();
                String itemName = IridiumSkyblock.getInstance().getBlockValues().blockNames.getOrDefault(material, material.name());
                ItemStack blockItem = ItemStackUtils.makeItem(material, 1, itemName, getColoredValueLore(valuableBlock.getValue()));
                inventory.setItem(slots.next(), blockItem);
            }
        } else if (guiType == BlockValueType.SPAWNER) {
            Iterator<Integer> slots = IridiumSkyblock.getInstance().getBlockValues().spawnerSlots.iterator();
            for (Map.Entry<EntityType, Double> valuableSpawner : IridiumSkyblock.getInstance().getBlockValues().spawnerValues.entrySet()) {
                EntityType spawnerType = valuableSpawner.getKey();
                String itemName = IridiumSkyblock.getInstance().getBlockValues().spawnerNames.getOrDefault(spawnerType, spawnerType.name());
                ItemStack spawnerItem = ItemStackUtils.makeItem(XMaterial.SPAWNER, 1, itemName, getColoredValueLore(valuableSpawner.getValue()));
                inventory.setItem(slots.next(), spawnerItem);
            }
        }

        return inventory;
    }

    private List<String> getColoredValueLore(double value) {
        return IridiumSkyblock.getInstance().getInventories().blockValueLore.stream()
                .map(StringUtils::color)
                .map(line -> line.replace("%value%", String.valueOf(value)))
                .collect(Collectors.toList());
    }

    enum BlockValueType {
        BLOCK,
        SPAWNER
    }

}
