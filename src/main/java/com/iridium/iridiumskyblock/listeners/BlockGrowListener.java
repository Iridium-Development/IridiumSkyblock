package com.iridium.iridiumskyblock.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandMission;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

import java.util.Optional;

public class BlockGrowListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockGrowEventMonitor(BlockGrowEvent event) {
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());
        XMaterial material = XMaterial.matchXMaterial(event.getBlock().getType());
        if (island.isPresent()) {
            for (String key : IridiumSkyblock.getInstance().getMissionsList().keySet()) {
                Mission mission = IridiumSkyblock.getInstance().getMissionsList().get(key);
                for (int i = 1; i <= mission.getMissions().size(); i++) {
                    String[] conditions = mission.getMissions().get(i - 1).toUpperCase().split(":");
                    if (conditions[0].equals("GROW") && (conditions[1].equals(material.name()) || conditions[1].equals("ANY"))) {
                        IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island.get(), mission, key, i);
                        if (islandMission.getProgress() >= Integer.parseInt(conditions[2])) {
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
