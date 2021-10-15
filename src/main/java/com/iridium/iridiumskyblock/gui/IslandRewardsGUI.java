package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandReward;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IslandRewardsGUI extends IslandGUI {

    public IslandRewardsGUI(Island island) {
        super(IridiumSkyblock.getInstance().getInventories().islandReward, island);
    }

    @Override
    public void addContent(Inventory inventory) {
        clearInventory(inventory);

        List<Placeholder> placeholders = new PlaceholderBuilder().applyIslandPlaceholders(getIsland()).build();

        AtomicInteger atomicInteger = new AtomicInteger(0);
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandRewardTableManager().getEntries(getIsland()).forEach(islandReward ->
                inventory.setItem(atomicInteger.getAndIncrement(), ItemStackUtils.makeItem(islandReward.getReward().item, placeholders))
        );
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (isBackButton(event)) return;

        List<IslandReward> islandRewards = IridiumSkyblock.getInstance().getDatabaseManager().getIslandRewardTableManager().getEntries(getIsland());
        if (islandRewards.size() > event.getSlot()) {
            IslandReward islandReward = islandRewards.get(event.getSlot());
            islandReward.getReward().claim(player, getIsland());
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandRewardTableManager().delete(islandReward);
            player.closeInventory();
        }
    }
}
