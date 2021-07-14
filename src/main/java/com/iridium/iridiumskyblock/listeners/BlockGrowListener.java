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

        island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "GROW:" + material.name(), 1));
    }

}
