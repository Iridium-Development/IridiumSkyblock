package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import org.bukkit.Bukkit;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockGrowEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BlockGrowListener implements Listener {

    private final List<XMaterial> instantGrowCrops = Arrays.asList(XMaterial.CACTUS, XMaterial.SUGAR_CANE, XMaterial.BAMBOO);

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void monitorBlockGrow(BlockGrowEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getBlock().getWorld())) return;
        Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () ->
                IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
                    XMaterial material = XMaterial.matchXMaterial(event.getBlock().getType());
                    if (event.getNewState().getBlock().getBlockData() instanceof Ageable ageable) {
                        IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island, "farming");
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

    @EventHandler
    public void onBlockFertilizeEvent(BlockFertilizeEvent event) {
        if (event.isCancelled()) return;
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getBlock().getWorld())) return;
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());
        if (island.isPresent()) {
            if (event.getPlayer() == null) return;
            if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer()), PermissionType.INTERACT)) {
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
        }
    }

}