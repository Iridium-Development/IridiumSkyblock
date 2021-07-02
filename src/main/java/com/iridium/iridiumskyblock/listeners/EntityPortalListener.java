package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

import java.util.Objects;

public class EntityPortalListener implements Listener {

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        if(event.getTo() != null && event.getTo().getWorld() != null) {
            IslandManager islandManager = IridiumSkyblock.getInstance().getIslandManager();
            IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getFrom()).ifPresent(island -> {
                World worldFrom = event.getFrom().getWorld();
                World worldTo = event.getTo().getWorld();
                if ((worldTo.getEnvironment() == World.Environment.NETHER && Objects.equals(worldFrom, islandManager.getWorld())) || (worldTo.getEnvironment() == World.Environment.NORMAL && Objects.equals(worldFrom, islandManager.getNetherWorld()))) {
                    if (IridiumSkyblock.getInstance().getConfiguration().netherIslands) {
                        World world = Objects.equals(worldFrom, islandManager.getNetherWorld()) ? islandManager.getWorld() : islandManager.getNetherWorld();
                        event.setTo(island.getCenter(world));
                    } else {
                        event.setCancelled(true);
                    }
                }
                if ((worldTo.getEnvironment() == World.Environment.THE_END && (Objects.equals(worldFrom, islandManager.getWorld()) || Objects.equals(worldFrom, islandManager.getNetherWorld()))) || (worldTo.getEnvironment() == World.Environment.NORMAL && Objects.equals(worldFrom, islandManager.getEndWorld()))) {
                    if (IridiumSkyblock.getInstance().getConfiguration().endIslands) {
                        World world = Objects.equals(worldFrom, islandManager.getEndWorld()) ? islandManager.getWorld() : islandManager.getEndWorld();
                        event.setCancelled(true);
                        event.getEntity().teleport(LocationUtils.getSafeLocation(island.getCenter(world), island));
                    } else {
                        event.setCancelled(true);
                    }
                }
            });
        }
    }

}
