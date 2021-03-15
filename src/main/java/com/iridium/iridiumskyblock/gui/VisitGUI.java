package com.iridium.iridiumskyblock.gui;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
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

public class VisitGUI implements GUI {
    private final List<Island> islands;
    private final int page;

    public VisitGUI(int page) {
        this.page = page;
        this.islands = IridiumSkyblock.getInstance().getDatabaseManager().getIslandList().stream().filter(Island::isVisitable).collect(Collectors.toList());
    }

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

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().visitGuiSize, StringUtils.color("&7Visit an Island"));

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
        }

        inventory.setItem(inventory.getSize() - 3, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().nextPage));
        inventory.setItem(inventory.getSize() - 7, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().previousPage));

        int elementsPerPage = inventory.getSize() - 9;
        AtomicInteger index = new AtomicInteger(0);
        islands.stream()
                .skip((long) (page - 1) * elementsPerPage)
                .limit(elementsPerPage)
                .forEachOrdered(island -> inventory.setItem( index.getAndIncrement(), ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().visit, Arrays.asList(
                        new Placeholder("name", island.getName()),
                        new Placeholder("owner", island.getOwner().isPresent() ? island.getOwner().get().getName() : island.getName()),
                        new Placeholder("time", island.getCreateTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat)))
                ))));

        return inventory;
    }
}