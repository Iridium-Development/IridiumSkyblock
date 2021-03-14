package com.iridium.iridiumskyblock.gui;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBlocks;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.Placeholder;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class IslandTopGUI implements GUI {

    private final HashMap<Integer, Island> islandSlots = new HashMap<>();

    public IslandTopGUI() {
        List<Island> islands = IridiumSkyblock.getInstance().getIslandManager().getIslands(IslandManager.SortType.VALUE);
        for (int rank : IridiumSkyblock.getInstance().getConfiguration().islandTopSlots.keySet()) {
            if (islands.size() < rank) continue;
            islandSlots.put(IridiumSkyblock.getInstance().getConfiguration().islandTopSlots.get(rank), islands.get(rank - 1));
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (islandSlots.containsKey(event.getSlot())) {
            Island island = islandSlots.get(event.getSlot());
            IridiumSkyblock.getInstance().getIslandManager().teleportHome((Player) event.getWhoClicked(), island);
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, StringUtils.color("&7Island Members"));
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
        }
        for (int slot : IridiumSkyblock.getInstance().getConfiguration().islandTopSlots.values()) {
            if (islandSlots.containsKey(slot)) {
                Island island = islandSlots.get(slot);
                List<Placeholder> placeholders = new ArrayList<>(Arrays.asList(
                        new Placeholder("rank", String.valueOf(island.getRank())),
                        new Placeholder("value", String.valueOf(island.getValue())),
                        new Placeholder("owner", island.getOwner().map(User::getName).orElse("N/A")),
                        new Placeholder("name", island.getName())
                ));
                IridiumSkyblock.getInstance().getBlockValues().blockValues.keySet().stream().map(material -> new Placeholder(material.name() + "_AMOUNT", String.valueOf(IridiumSkyblock.getInstance().getIslandManager().getIslandBlock(island, material).map(IslandBlocks::getAmount).orElse(0)))).forEach(placeholders::add);
                inventory.setItem(slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().topIsland, placeholders));
            } else {
                inventory.setItem(slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().topFiller));
            }
        }
        return inventory;
    }
}
