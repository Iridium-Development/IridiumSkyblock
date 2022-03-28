package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.gui.PagedGUI;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.configs.inventories.NoItemGUI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandReward;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class IslandRewardsGUI extends PagedGUI<IslandReward> {

    private final Island island;

    public IslandRewardsGUI(Island island, Inventory previousInventory) {
        super(1,
                IridiumSkyblock.getInstance().getInventories().islandReward.size,
                IridiumSkyblock.getInstance().getInventories().islandReward.background,
                IridiumSkyblock.getInstance().getInventories().previousPage,
                IridiumSkyblock.getInstance().getInventories().nextPage,
                previousInventory,
                IridiumSkyblock.getInstance().getInventories().backButton
        );
        this.island = island;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        NoItemGUI noItemGUI = IridiumSkyblock.getInstance().getInventories().islandReward;
        Inventory inventory = Bukkit.createInventory(this, getSize(), StringUtils.color(noItemGUI.title));
        addContent(inventory);
        return inventory;
    }

    @Override
    public Collection<IslandReward> getPageObjects() {
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandRewardTableManager().getEntries(island);
    }

    @Override
    public ItemStack getItemStack(IslandReward islandReward) {
        return ItemStackUtils.makeItem(islandReward.getReward().item, new PlaceholderBuilder()
                .applyIslandPlaceholders(island)
                .build()
        );
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        IslandReward islandReward = getItem(event.getSlot());
        if (islandReward == null) return;
        islandReward.getReward().claim((Player) event.getWhoClicked(), island);
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandRewardTableManager().delete(islandReward);
        event.getWhoClicked().closeInventory();
    }
}
