package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.managers.IslandManager;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;

/**
 * GUI which shows the Islands with the highest Island value.
 *
 * @see Island#getValue()
 */
public class IslandTopGUI extends GUI {

    private final HashMap<Integer, Island> islandSlots = new HashMap<>();

    /**
     * The default constructor.
     */
    public IslandTopGUI() {
        super(IridiumSkyblock.getInstance().getInventories().islandTopGUI, null);
    }

    @Override
    public void addContent(Inventory inventory) {
        List<Island> islands = IridiumSkyblock.getInstance().getIslandManager().getIslands(IslandManager.SortType.VALUE);
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().islandTopGUI.background);

        islandSlots.clear();
        inventory.clear();


        for (int rank : IridiumSkyblock.getInstance().getConfiguration().islandTopSlots.keySet()) {
            int slot = IridiumSkyblock.getInstance().getConfiguration().islandTopSlots.get(rank);
            if (islands.size() >= rank) {
                Island island = islands.get(rank - 1);
                islandSlots.put(slot, island);
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
