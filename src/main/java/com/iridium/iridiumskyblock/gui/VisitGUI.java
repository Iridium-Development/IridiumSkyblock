package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.Placeholder;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
        this.islands = IridiumSkyblock.getInstance().getDatabaseManager().getIslandList().stream().filter(Island::isVisitable).collect(Collectors.toList());
    }

    /**
     * Builds and returns this inventory.
     *
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().visitGuiSize, StringUtils.color("&7Visit an Island"));

        InventoryUtils.fillInventory(inventory);

        inventory.setItem(inventory.getSize() - 3, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().nextPage));
        inventory.setItem(inventory.getSize() - 7, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().previousPage));

        int elementsPerPage = inventory.getSize() - 9;
        AtomicInteger index = new AtomicInteger(0);
        islands.stream()
                .skip((long) (page - 1) * elementsPerPage)
                .limit(elementsPerPage)
                .forEachOrdered(island -> inventory.setItem(index.getAndIncrement(), ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().visit, Arrays.asList(
                        new Placeholder("name", island.getName()),
                        new Placeholder("owner", island.getOwner().getName()),
                        new Placeholder("time", island.getCreateTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat)))
                ))));

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
                IridiumSkyblock.getInstance().getIslandManager().teleportHome((Player) event.getWhoClicked(), island);
            }
        }
    }

}