package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.configs.inventories.SingleItemGUI;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * GUI which shows a list of all Islands a user can visit.
 */
public class VisitGUI implements GUI {

    private final List<Island> islands;
    private final int page;

    /**
     * The default constructor.
     *
     * @param page The current page of this GUI
     */
    public VisitGUI(int page) {
        this.page = page;
        this.islands = IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries().stream().filter(Island::isVisitable).collect(Collectors.toList());
    }

    /**
     * Builds and returns this inventory.
     *
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        SingleItemGUI singleItemGUI = IridiumSkyblock.getInstance().getInventories().visitGUI;
        Inventory inventory = Bukkit.createInventory(this, singleItemGUI.size, StringUtils.color(singleItemGUI.title));

        addContent(inventory);

        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();

        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().visitGUI.background);

        inventory.setItem(inventory.getSize() - 3, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().nextPage));
        inventory.setItem(inventory.getSize() - 7, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().previousPage));

        int elementsPerPage = inventory.getSize() - 9;
        AtomicInteger index = new AtomicInteger(0);
        islands.stream()
                .skip((long) (page - 1) * elementsPerPage)
                .limit(elementsPerPage)
                .forEachOrdered(island -> inventory.setItem(index.getAndIncrement(), ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().visitGUI.item, new PlaceholderBuilder().applyIslandPlaceholders(island).build())));
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() == getInventory().getSize() - 7) {
            if (page > 1) {
                event.getWhoClicked().openInventory(new VisitGUI(page - 1).getInventory());
            }
        } else if (event.getSlot() == getInventory().getSize() - 3) {
            if ((event.getInventory().getSize() - 9) * page < islands.size()) {
                event.getWhoClicked().openInventory(new VisitGUI(page + 1).getInventory());
            }
        } else if (event.getSlot() + 1 <= islands.size()) {
            int index = ((event.getInventory().getSize() - 9) * (page - 1)) + event.getSlot();
            if (islands.size() > index) {
                Island island = islands.get(index);
                String command = IridiumSkyblock.getInstance().getCommands().visitCommand.aliases.get(0);
                Bukkit.dispatchCommand(event.getWhoClicked(), "is " + command + " " + island.getOwner().getName());
                event.getWhoClicked().closeInventory();
            }
        }
    }

}