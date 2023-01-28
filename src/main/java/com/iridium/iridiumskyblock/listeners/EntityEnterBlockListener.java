package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEnterBlockEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class EntityEnterBlockListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityEnterBlock(EntityEnterBlockEvent event) {
        List<MetadataValue> list = event.getEntity().getMetadata("island_spawned");
        if (list.isEmpty()) return;
        int islandId = list.get(0).asInt();
        IridiumSkyblock.getInstance().getIslandManager().getIslandById(islandId).ifPresent(island -> IridiumSkyblock.getInstance().getIslandManager().onEntityRemoved(island, event.getEntity()));
    }

}
