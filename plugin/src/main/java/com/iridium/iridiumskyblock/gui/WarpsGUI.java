package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandWarp;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.Placeholder;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WarpsGUI implements GUI {

    private final Island island;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public WarpsGUI(@NotNull Island island) {
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
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().warpsGUI.size,
                StringUtils.color(IridiumSkyblock.getInstance().getInventories().warpsGUI.title.replace("%island_name%", island.getName())));

        addContent(inventory);

        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory);

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
                if (event.getClick().equals(ClickType.RIGHT)) {
                    Bukkit.dispatchCommand(event.getWhoClicked(), "is deletewarp " + islandWarp.getName());
                } else {
                    Bukkit.dispatchCommand(event.getWhoClicked(), "is warp " + islandWarp.getName());
                    event.getWhoClicked().closeInventory();
                }
                addContent(event.getInventory());
                return;
            }
        }
    }
}
