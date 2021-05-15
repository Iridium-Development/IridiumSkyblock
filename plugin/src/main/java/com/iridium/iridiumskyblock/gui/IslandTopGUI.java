package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.configs.inventories.IslandTopInventoryConfig;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * GUI which shows the Islands with the highest Island value.
 *
 * @see Island#getValue()
 */
public class IslandTopGUI implements GUI {

    private final HashMap<Integer, Island> islandSlots = new HashMap<>();

    /**
     * The default constructor.
     */
    public IslandTopGUI() {
        List<Island> islands = IridiumSkyblock.getInstance().getIslandManager().getIslands(IslandManager.SortType.VALUE);

        for (int rank : IridiumSkyblock.getInstance().getConfiguration().islandTopSlots.keySet()) {
            if (islands.size() < rank) continue;
            islandSlots.put(IridiumSkyblock.getInstance().getConfiguration().islandTopSlots.get(rank), islands.get(rank - 1));
        }
    }

    /**
     * Builds and returns this inventory.
     *
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        IslandTopInventoryConfig topInventoryConfig = IridiumSkyblock.getInstance().getInventories().islandTopGUI;
        Inventory inventory = Bukkit.createInventory(this, topInventoryConfig.size, StringUtils.color(topInventoryConfig.title));

        addContent(inventory);

        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().islandTopGUI.background);


        for (int slot : IridiumSkyblock.getInstance().getConfiguration().islandTopSlots.values()) {
            if (islandSlots.containsKey(slot)) {
                Island island = islandSlots.get(slot);
                inventory.setItem(slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandTopGUI.item, new PlaceholderBuilder().applyIslandPlaceholders(island).build()));
            } else {
                inventory.setItem(slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandTopGUI.filler));
            }
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
        if (!islandSlots.containsKey(event.getSlot())) return;

        Island island = islandSlots.get(event.getSlot());
        String command = IridiumSkyblock.getInstance().getCommands().visitCommand.aliases.get(0);
        Bukkit.dispatchCommand(event.getWhoClicked(), "is " + command + " " + island.getOwner().getName());
    }

}
