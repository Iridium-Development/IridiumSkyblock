package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.dependencies.nbtapi.NBTItem;
import com.iridium.iridiumcore.dependencies.nbtapi.NBTType;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.managers.IslandManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerCraftListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerCraft(PrepareItemCraftEvent event) {
        for (ItemStack item : event.getInventory().getMatrix()) {
            if (IridiumSkyblock.getInstance().getIslandManager().isIslandCrystal(item)) {
                event.getInventory().setResult(null);
                return;
            }
        }
    }
}
