package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkLoadListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void monitorChunkLoad(ChunkLoadEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(new Location(event.getWorld(), event.getChunk().getX() << 4, 1, event.getChunk().getZ() << 4)).ifPresent(island -> {
            IridiumSkyblock.getInstance().getIslandManager().initializeIslandEntityCounter(island);
        });
    }

}
