package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandMission;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Optional;

public class PotionBrewListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPotionBrewEventMonitor(BrewEvent event) {
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());
        if (island.isPresent()) {
            for (String key : IridiumSkyblock.getInstance().getMissionsList().keySet()) {
                Mission mission = IridiumSkyblock.getInstance().getMissionsList().get(key);
                for (int i = 1; i <= mission.getMissions().size(); i++) {
                    String[] conditions = mission.getMissions().get(i - 1).toUpperCase().split(":");
                    if (conditions[0].equals("BREW")) {
                        for (int j = 0; j < 3; j++) {
                            ItemStack itemStack = event.getContents().getItem(j);
                            if (itemStack != null && itemStack.getItemMeta() instanceof PotionMeta) {
                                PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
                                if ((conditions[1].equals(potionMeta.getBasePotionData().getType().name()) || conditions[1].equals("ANY")) && (Integer.parseInt(conditions[2]) == (potionMeta.getBasePotionData().isUpgraded() ? 1 : 2) || conditions[2].equals("ANY"))) {
                                    IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island.get(), mission, key, i);
                                    if (islandMission.getProgress() >= Integer.parseInt(conditions[3])) {
                                        //Check if Mission is completed
                                    } else {
                                        islandMission.setProgress(islandMission.getProgress() + 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
