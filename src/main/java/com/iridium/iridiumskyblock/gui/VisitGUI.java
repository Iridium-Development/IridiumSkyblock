package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * GUI which shows a list of all Islands a user can visit.
 */
public class VisitGUI extends GUI {

    private final int page;
    private final User viewer;

    /**
     * The default constructor.
     *
     * @param page   The current page of this GUI
     * @param viewer The viewer of this GUI
     */
    public VisitGUI(int page, User viewer) {
        super(IridiumSkyblock.getInstance().getInventories().visitGUI);
        this.page = page;
        this.viewer = viewer;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();

        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().visitGUI.background);

        inventory.setItem(inventory.getSize() - 3, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().nextPage));
        inventory.setItem(inventory.getSize() - 7, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().previousPage));

        int elementsPerPage = inventory.getSize() - 9;
        List<Island> islands = IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries().stream()
                .filter(island -> viewer.isBypassing() || island.isVisitable())
                .skip((long) (page - 1) * elementsPerPage)
                .limit(elementsPerPage)
                .collect(Collectors.toList());
        AtomicInteger slot = new AtomicInteger(0);
        for (Island island : islands) {
            inventory.setItem(slot.getAndIncrement(), ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().visitGUI.item, new PlaceholderBuilder().applyIslandPlaceholders(island).build()));
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
        List<Island> islands = IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries().stream()
                .filter(island -> viewer.isBypassing() || island.isVisitable())
                .collect(Collectors.toList());
        if (event.getSlot() == getInventory().getSize() - 7) {
            if (page > 1) {
                event.getWhoClicked().openInventory(new VisitGUI(page - 1, viewer).getInventory());
            }
        } else if (event.getSlot() == getInventory().getSize() - 3) {
            if ((event.getInventory().getSize() - 9) * page < islands.size()) {
                event.getWhoClicked().openInventory(new VisitGUI(page + 1, viewer).getInventory());
            }
        } else if (event.getSlot() + 1 <= islands.size()) {
            int index = ((event.getInventory().getSize() - 9) * (page - 1)) + event.getSlot();
            if (islands.size() > index) {
                Island island = islands.get(index);
                IridiumSkyblock.getInstance().getCommands().visitCommand.execute(event.getWhoClicked(), new String[]{"", island.getOwner().getName()});
                event.getWhoClicked().closeInventory();
            }
        }
    }

}
