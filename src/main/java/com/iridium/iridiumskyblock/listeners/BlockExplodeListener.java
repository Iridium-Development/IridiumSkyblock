package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class BlockExplodeListener implements Listener {

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation()).ifPresent(island ->
                event.blockList().removeIf(block -> !island.isInIsland(block.getLocation()))
        );
    }


}
