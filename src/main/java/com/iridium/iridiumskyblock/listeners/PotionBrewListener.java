package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PotionBrewListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void monitorPotionBrew(BrewEvent event) {
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());

        for (int i = 0; i < 3; i++) {
            ItemStack itemStack = event.getContents().getItem(i);
            if (itemStack != null && itemStack.getItemMeta() instanceof PotionMeta) {
                PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();

                // Increment missions with the name of the brewed potion
                island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "BREW:" + potionMeta.getBasePotionData().getType().name() + ":" + (potionMeta.getBasePotionData().isUpgraded() ? 1 : 2), 1));

                // Increment missions with the ANY identifier
                island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "BREW:ANY", 1));

                // Checks all itemLists created in missions.yml
                for (Map.Entry<String, List<String>> itemList : IridiumSkyblock.getInstance().getItemLists().entrySet()) {
                    // If the brewed potion matches one in the list
                    // Increment missions with the name of the list as the identifier
                    if (itemList.getValue().contains(potionMeta.getBasePotionData().getType().name() + ":" + (potionMeta.getBasePotionData().isUpgraded() ? 1 : 2))) {
                        island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "BREW:" + itemList.getKey(), 1));
                    }
                }
            }
        }
    }

}
