package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBlocks;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.Placeholder;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * GUI which shows the Islands with the highest Island value.
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
        Inventory inventory = Bukkit.createInventory(this, 27, StringUtils.color("&7Island Members"));

        InventoryUtils.fillInventory(inventory);

        for (int slot : IridiumSkyblock.getInstance().getConfiguration().islandTopSlots.values()) {
            if (islandSlots.containsKey(slot)) {
                Island island = islandSlots.get(slot);
                List<Placeholder> placeholders = new ArrayList<>(Arrays.asList(
                        new Placeholder("rank", String.valueOf(island.getRank())),
                        new Placeholder("value", String.valueOf(island.getValue())),
                        new Placeholder("owner", island.getOwner().getName()),
                        new Placeholder("name", island.getName())
                ));
                IridiumSkyblock.getInstance().getBlockValues().blockValues.keySet().stream().map(material -> new Placeholder(material.name() + "_AMOUNT", String.valueOf(IridiumSkyblock.getInstance().getIslandManager().getIslandBlock(island, material).map(IslandBlocks::getAmount).orElse(0)))).forEach(placeholders::add);
                inventory.setItem(slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().topIsland, placeholders));
            } else {
                inventory.setItem(slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().topFiller));
            }
        }

        return inventory;
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
        IridiumSkyblock.getInstance().getIslandManager().teleportHome((Player) event.getWhoClicked(), island);
    }

}
