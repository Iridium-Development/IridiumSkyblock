package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplodeListener implements Listener {

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        int islandId = event.getEntity().getMetadata("island_spawned").get(0).asInt();
        IridiumSkyblock.getInstance().getIslandManager().getIslandById(islandId).ifPresent(island ->
                event.blockList().removeIf(block -> !island.isInIsland(block.getLocation()))
        );
    }

}
