package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.Bukkit;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.Optional;

public class StructureGrowListener implements Listener {

    @EventHandler
    public void onStructureGrowEvent(StructureGrowEvent event) {
        if (event.isCancelled()) return;
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getWorld())) return;
        Bukkit.getScheduler().scheduleSyncDelayedTask(IridiumSkyblock.getInstance(), () -> {
            Optional<Island> optionalIsland = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getLocation());

            if (optionalIsland.isPresent()) {
                TreeType treeType = event.getSpecies();
                Island island = optionalIsland.get();
                IridiumSkyblock.getInstance().getMissionManager().handleMissionUpdates(island, "GROW", treeType.name(), 1);
            }
        });
    }

    @EventHandler
    public void onBlockSpreadEvent(BlockSpreadEvent event) {
        if (event.isCancelled()) return;
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getBlock().getWorld())) return;
        Bukkit.getScheduler().scheduleSyncDelayedTask(IridiumSkyblock.getInstance(), () -> {
            Optional<Island> optionalIsland = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());

            if (optionalIsland.isPresent()) {
                Island island = optionalIsland.get();
                IridiumSkyblock.getInstance().getMissionManager().handleMissionUpdates(island, "SPREAD", event.getNewState().getBlock().getType().name(), 1);
            }
        });
    }

    @EventHandler
    public void onBlockFertilizeEvent(BlockFertilizeEvent event) {
        if (event.isCancelled()) return;
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getBlock().getWorld())) return;
        Bukkit.getScheduler().scheduleSyncDelayedTask(IridiumSkyblock.getInstance(), () -> {
            Optional<Island> optionalIsland = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());

            if (optionalIsland.isPresent()) {
                Island island = optionalIsland.get();
                event.getBlocks().forEach(blockState -> {
                    IridiumSkyblock.getInstance().getMissionManager().handleMissionUpdates(island, "GROW", blockState.getBlockData().getMaterial().name(), 1);
                });
            }
        });

    }
}
