package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FurnaceSmeltListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void monitorFurnaceSmelt(FurnaceSmeltEvent event) {
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());
        XMaterial material = XMaterial.matchXMaterial(event.getSource().getType());

        // Increment missions with the name of the smelted item
        island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "SMELT:" + material.name(), 1));

        // Increment missions with the ANY identifier
        island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "SMELT:ANY", 1));

        // Checks all itemLists created in missions.yml
        for (Map.Entry<String, List<String>> itemList : IridiumSkyblock.getInstance().getItemLists().entrySet()) {
            // If the smelted item matches one in the list
            // Increment missions with the name of the list as the identifier
            if (itemList.getValue().contains(material.name())) {
                island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "SMELT:" + itemList.getKey(), 1));
            }
        }
    }

}
