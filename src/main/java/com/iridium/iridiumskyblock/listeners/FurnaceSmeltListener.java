package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.support.material.IridiumMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

import java.util.Optional;

public class FurnaceSmeltListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void monitorFurnaceSmelt(FurnaceSmeltEvent event) {
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());
        IridiumMaterial material = IridiumMaterial.matchXMaterial(event.getSource().getType());

        island.ifPresent(value -> IridiumSkyblock.getInstance().getMissionManager().handleMissionUpdates(value, "SMELT", material.name(), 1));
    }

}
