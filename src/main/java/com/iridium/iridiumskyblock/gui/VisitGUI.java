package com.iridium.iridiumskyblock.gui;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.Lists;
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
        int elementsPerPage = IridiumSkyblock.getInstance().getInventories().visitGuiSize - 9;
        this.islands = Lists.partition(IridiumSkyblock.getInstance().getDatabaseManager().getIslandList().stream().filter(Island::isVisitable)
                .collect(Collectors.toList()), elementsPerPage).get(page);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() == getInventory().getSize() - 7 && page != 0) {
            event.getWhoClicked().openInventory(new VisitGUI(page - 1).getInventory());
        } else if (event.getSlot() == getInventory().getSize() - 3) {
            int size = Lists.partition(IridiumSkyblock.getInstance().getDatabaseManager().getIslandList().stream().filter(Island::isVisitable)
                    .collect(Collectors.toList()), IridiumSkyblock.getInstance().getInventories().visitGuiSize - 9).size();
            if ((page + 1) < size) {
                event.getWhoClicked().openInventory(new VisitGUI(page + 1).getInventory());
            }
        } else if (event.getSlot() < islands.size()) {
            Island island = islands.get(event.getSlot());
            IridiumSkyblock.getInstance().getIslandManager().teleportHome((Player) event.getWhoClicked(), island);
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

        AtomicInteger index = new AtomicInteger(0);

        islands.forEach(island -> inventory.setItem(index.getAndIncrement(), ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().visit, Arrays.asList(
                new Placeholder("name", island.getName()),
                new Placeholder("owner", island.getOwner().getName()),
                new Placeholder("time", island.getCreateTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat)))
        ))));

        return inventory;
    }
}