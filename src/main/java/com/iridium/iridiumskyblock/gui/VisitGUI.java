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
import java.util.Optional;
import java.util.stream.Collectors;

public class VisitGUI implements GUI {
    private final List<Island> islands;
    private final int page;

    public VisitGUI(int page) {
        this.page = page;
        List<Island> islandList = IridiumSkyblock.getInstance().getDatabaseManager().getIslandList().stream().filter(Island::getVisitable).collect(Collectors.toList());
        int toIndex = page * IridiumSkyblock.getInstance().getInventories().visitGuiSize;
        int fromIndex = toIndex - IridiumSkyblock.getInstance().getInventories().visitGuiSize;
        islands = islandList.subList(fromIndex, Math.min(islandList.size(), toIndex));
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        // TODO: Add a check thing for next/previous page is exist
        if (event.getSlot() == getInventory().getSize() - 7) {
            event.getWhoClicked().openInventory(new VisitGUI(page - 1).getInventory());
        } else if (event.getSlot() == getInventory().getSize() - 3) {
            event.getWhoClicked().openInventory(new VisitGUI(page + 1).getInventory());
        } else if (event.getSlot() + 1 <= islands.size()) {
            Optional<Island> islandOptional = Optional.ofNullable(islands.get(event.getSlot()));
            islandOptional.ifPresent(island -> IridiumSkyblock.getInstance().getIslandManager().teleportHome((Player) event.getWhoClicked(), island));
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().visitGuiSize, StringUtils.color("&7Visitable Islands"));
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
        }
        inventory.setItem(getInventory().getSize() - 3, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().nextPage));
        inventory.setItem(getInventory().getSize() - 7, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().previousPage));
        int i = 0;
        while (i <= (islands.size() - 1)) {
            Optional<Island> islandOptional = Optional.ofNullable(islands.get(i));
            int finalI = i;
            islandOptional.ifPresent(island -> inventory.setItem(finalI, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().visit, Arrays.asList(
                    new Placeholder("name", island.getName()),
                    new Placeholder("votes", String.valueOf(island.getVotes())),
                    new Placeholder("value", String.valueOf(island.getValue())),
                    new Placeholder("membersize", String.valueOf(island.getMembers().size())),
                    // TODO: get this date format from config (i couldn't cause that added after then this branch created)
                    new Placeholder("time", island.getCreateTime().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd HH:mm:ss")))
            ))));
            if (islandOptional.isPresent()) i++;
        }
        return inventory;
    }
}