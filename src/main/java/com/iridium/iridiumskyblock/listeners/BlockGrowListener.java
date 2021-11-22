package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.IslandBooster;
import org.bukkit.Bukkit;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

import java.util.Arrays;
import java.util.List;

public class BlockGrowListener implements Listener {

    private List<XMaterial> instantGrowCrops = Arrays.asList(XMaterial.CACTUS, XMaterial.SUGAR_CANE, XMaterial.BAMBOO);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void monitorBlockGrow(BlockGrowEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getBlock().getWorld())) return;
        Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () ->
                IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
                    XMaterial material = XMaterial.matchXMaterial(event.getBlock().getType());
                    if (event.getNewState().getBlock().getBlockData() instanceof Ageable) {
                        IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island, "farming");
                        Ageable ageable = (Ageable) event.getNewState().getBlock().getBlockData();
                        if (islandBooster.isActive()) {
                            ageable.setAge(Math.min(ageable.getAge() + 1, ageable.getMaximumAge()));
                            event.getNewState().getBlock().setBlockData(ageable);
                        }
                        if (ageable.getAge() == ageable.getMaximumAge() || instantGrowCrops.contains(XMaterial.matchXMaterial(event.getNewState().getType()))) {
                            IridiumSkyblock.getInstance().getMissionManager().handleMissionUpdates(island, "GROW", material.name(), 1);
                        }
                    } else {
                        IridiumSkyblock.getInstance().getMissionManager().handleMissionUpdates(island, "GROW", material.name(), 1);
                    }
                })
        );
    }

}
