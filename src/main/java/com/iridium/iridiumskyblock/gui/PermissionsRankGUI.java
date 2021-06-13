package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * GUI which allows users to select ranks to edit in the {@link PermissionsGUI}.
 */
public class PermissionsRankGUI implements GUI {

    private final Island island;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public PermissionsRankGUI(@NotNull Island island) {
        this.island = island;
    }

    /**
     * Builds and returns this inventory.
     *
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().permissionsRankGUI.size, StringUtils.color(IridiumSkyblock.getInstance().getInventories().permissionsRankGUI.title));

        addContent(inventory);

        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().permissionsRankGUI.background);

        for (int i = 0; i < 5; i++) {
            IslandRank islandRank = IslandRank.getByLevel(i);
            String rankName = IridiumSkyblock.getInstance().getConfiguration().islandRanks.get(islandRank);
            inventory.setItem(i + 11, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().permissionsRankGUI.item, Collections.singletonList(new Placeholder("rank", rankName))));
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
        for (int i = 0; i < 5; i++) {
            if (event.getSlot() != i + 11) continue;

            IslandRank islandRank = IslandRank.getByLevel(i);
            if (islandRank != null) {
                event.getWhoClicked().openInventory(new PermissionsGUI(island, islandRank).getInventory());
            }
        }
    }

}
