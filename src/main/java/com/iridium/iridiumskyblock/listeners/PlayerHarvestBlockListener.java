package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class PlayerHarvestBlockListener implements Listener {

    @EventHandler
    public void onPlayerHarvestBlockEvent(PlayerHarvestBlockEvent event) {
        if (event.isCancelled()) return;
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getHarvestedBlock().getWorld())) return;
        Bukkit.getScheduler().scheduleSyncDelayedTask(IridiumSkyblock.getInstance(), () -> {
            Optional<Island> optionalIsland = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getPlayer().getLocation(), true);

            if (optionalIsland.isPresent()) {
                Island island = optionalIsland.get();
                List<ItemStack> itemStacks = event.getItemsHarvested();
                for (ItemStack itemStack : itemStacks) {
                    IridiumSkyblock.getInstance().getMissionManager().handleMissionUpdates(island, "HARVEST", itemStack.getType().name(), 1);
                }
            }
        });
    }
}
