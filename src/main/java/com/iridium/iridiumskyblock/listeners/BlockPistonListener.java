package com.iridium.iridiumskyblock.listeners;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.Map;

public class BlockPistonListener implements Listener {

    private static final Map<BlockFace, int[]> offsets = ImmutableMap.<BlockFace, int[]>builder()
            .put(BlockFace.EAST, new int[]{1, 0, 0})
            .put(BlockFace.WEST, new int[]{-1, 0, 0})
            .put(BlockFace.UP, new int[]{0, 1, 0})
            .put(BlockFace.DOWN, new int[]{0, -1, 0})
            .put(BlockFace.SOUTH, new int[]{0, 0, 1})
            .put(BlockFace.NORTH, new int[]{0, 0, -1})
            .build();

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
            for (Block block : event.getBlocks()) {
                int[] offset = offsets.get(event.getDirection());
                if (!island.isInIsland(block.getLocation().add(offset[0], offset[1], offset[2]))) {
                    event.setCancelled(true);
                    return;
                }
            }
        });

    }

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island -> {
            for (Block block : event.getBlocks()) {
                if (!island.isInIsland(block.getLocation())) {
                    event.setCancelled(true);
                    return;
                }
            }
        });

    }

}
