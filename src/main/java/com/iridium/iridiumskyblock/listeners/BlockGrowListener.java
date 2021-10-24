package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import org.bukkit.CropState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.material.Crops;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BlockGrowListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void monitorBlockGrow(BlockGrowEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getBlock().getWorld())) return;

        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());
        XMaterial material = XMaterial.matchXMaterial(event.getNewState().getType());

        if (event.getNewState().getData() instanceof Crops) {
            Crops crops = (Crops) event.getNewState().getData();
            if (island.isPresent()) {
                IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island.get(), "farming");
                if (islandBooster.isActive()) {
                    CropState newState = CropState.getByData((byte) (crops.getState().getData() + 1));
                    if (newState != null) {
                        crops.setState(newState);
                    }
                }
            }
            if (!crops.getState().equals(CropState.RIPE)) return;
        }

        // Increment missions with the name of the grown item
        island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "GROW:" + material.name(), 1));

        // Increment missions with the ANY identifier
        island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "GROW:ANY", 1));

        // Checks all itemLists created in missions.yml
        for (Map.Entry<String, List<String>> itemList : IridiumSkyblock.getInstance().getItemLists().entrySet()) {
            // If the smelted item matches one in the list
            // Increment missions with the name of the list as the identifier
            if (itemList.getValue().contains(material.name())) {
                island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "GROW:" + itemList.getKey(), 1));
            }
        }
    }

}
