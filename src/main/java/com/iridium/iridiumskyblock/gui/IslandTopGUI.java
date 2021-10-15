package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.managers.IslandManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GUI which shows the Islands with the highest Island value.
 *
 * @see Island#getValue()
 */
public class IslandTopGUI extends GUI {

    private final Map<Integer, Island> islandSlots = new HashMap<>();

    /**
     * The default constructor.
     */
    public IslandTopGUI() {
        super(IridiumSkyblock.getInstance().getInventories().islandTopGUI);
    }

    @Override
    public void addContent(Inventory inventory) {
        clearInventory(inventory);

        List<Island> islands = IridiumSkyblock.getInstance().getIslandManager().getIslands(IslandManager.SortType.VALUE);
        islandSlots.clear();

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
        Player player = (Player) event.getWhoClicked();
        if (isBackButton(event) || !islandSlots.containsKey(event.getSlot())) return;

        Island island = islandSlots.get(event.getSlot());
        String command = IridiumSkyblock.getInstance().getCommands().visitCommand.aliases.get(0);
        Bukkit.dispatchCommand(player, "is " + command + " " + island.getOwner().getName());
    }

}
