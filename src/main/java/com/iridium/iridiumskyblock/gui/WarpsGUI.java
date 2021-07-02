package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandWarp;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WarpsGUI extends GUI {

    private final Island island;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public WarpsGUI(@NotNull Island island) {
        super(IridiumSkyblock.getInstance().getInventories().warpsGUI, island);
        this.island = island;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().warpsGUI.background);

        AtomicInteger atomicInteger = new AtomicInteger(1);

        List<IslandWarp> islandWarps = IridiumSkyblock.getInstance().getDatabaseManager().getIslandWarpTableManager().getEntries(island);
        Collections.reverse(islandWarps);
        for (IslandWarp islandWarp : islandWarps) {
            int slot = IridiumSkyblock.getInstance().getConfiguration().islandWarpSlots.get(atomicInteger.getAndIncrement());
            ItemStack itemStack = ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().warpsGUI.item, Arrays.asList(
                    new Placeholder("island_name", island.getName()),
                    new Placeholder("warp_name", islandWarp.getName()),
                    new Placeholder("description", islandWarp.getDescription() != null ? islandWarp.getDescription() : "")
            ));
            Material material = islandWarp.getIcon().parseMaterial();
            if (material != null) itemStack.setType(material);
            inventory.setItem(slot, itemStack);
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
        List<IslandWarp> islandWarps = IridiumSkyblock.getInstance().getDatabaseManager().getIslandWarpTableManager().getEntries(island);
        Collections.reverse(islandWarps);
        AtomicInteger atomicInteger = new AtomicInteger(1);
        for (IslandWarp islandWarp : islandWarps) {
            if (IridiumSkyblock.getInstance().getConfiguration().islandWarpSlots.get(atomicInteger.getAndIncrement()) == event.getSlot()) {
                String command = null;
                switch (event.getClick()) {
                    case LEFT:
                        command = IridiumSkyblock.getInstance().getCommands().warpsCommand.aliases.get(0);
                        break;
                    case RIGHT:
                        command = IridiumSkyblock.getInstance().getCommands().deleteWarpCommand.aliases.get(0);
                }
                if (command != null)
                    Bukkit.dispatchCommand(event.getWhoClicked(), "is " + command + " " + islandWarp.getName());
                addContent(event.getInventory());
                return;
            }
        }
    }

}
