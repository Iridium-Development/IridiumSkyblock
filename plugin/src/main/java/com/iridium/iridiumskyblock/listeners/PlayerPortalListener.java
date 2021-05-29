package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.managers.IslandManager;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Objects;
import java.util.Optional;

public class PlayerPortalListener implements Listener {

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        IslandManager islandManager = IridiumSkyblock.getInstance().getIslandManager();
        if ((event.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) && IridiumSkyblock.getInstance().getConfiguration().netherIslands)
                || (event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL) && IridiumSkyblock.getInstance().getConfiguration().endIslands)) {
            Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getFrom());
            if (island.isPresent()) {
                event.setSearchRadius(island.get().getSize() / 2);
                if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
                    World world = Objects.equals(event.getFrom().getWorld(), islandManager.getNetherWorld()) ? islandManager.getWorld() : islandManager.getNetherWorld();
                    event.setTo(island.get().getCenter(world));
                }
                if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) {
                    event.setCancelled(true);
//                World world = Objects.equals(event.getFrom().getWorld(), islandManager.getEndWorld()) ? islandManager.getWorld() : islandManager.getEndWorld();
//                event.setTo(LocationUtils.getSafeLocation(island.getCenter(world), island));
                }
            } else {
                event.setCancelled(true);
            }
        }
    }
}
