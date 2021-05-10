package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandReward;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RewardsGUI implements GUI {

    private final Island island;

    public RewardsGUI(Island island) {
        this.island = island;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        List<IslandReward> islandRewards = IridiumSkyblock.getInstance().getDatabaseManager().getIslandRewardTableManager().getEntries(island);
        if (islandRewards.size() > event.getSlot()) {
            IslandReward islandReward = islandRewards.get(event.getSlot());
            islandReward.getReward().claim((Player) event.getWhoClicked(), island);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandRewardTableManager().delete(islandReward);
            event.getWhoClicked().closeInventory();
        }
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();

        InventoryUtils.fillInventory(inventory);

        AtomicInteger atomicInteger = new AtomicInteger(0);
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandRewardTableManager().getEntries(island).forEach(islandReward ->
                inventory.setItem(atomicInteger.getAndIncrement(), ItemStackUtils.makeItem(islandReward.getReward().item))
        );
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().islandReward.size, StringUtils.color(IridiumSkyblock.getInstance().getInventories().islandReward.title));

        addContent(inventory);

        return inventory;
    }
}
