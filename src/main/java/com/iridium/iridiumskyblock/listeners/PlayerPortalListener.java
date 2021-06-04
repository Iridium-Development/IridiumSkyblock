package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Configuration;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.Objects;
import java.util.Optional;

public class PlayerPortalListener implements Listener {

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        IslandManager islandManager = IridiumSkyblock.getInstance().getIslandManager();
        Configuration config = IridiumSkyblock.getInstance().getConfiguration();
        if (!IridiumSkyblock.getInstance().getIslandManager().isIslandWorld(event.getFrom().getWorld())) return;
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getFrom());
        if (event.getCause().equals(TeleportCause.NETHER_PORTAL) && config.netherIslands) {
            if (island.isPresent()) {
                event.setSearchRadius(island.get().getSize() / 2);
                World world = Objects.equals(event.getFrom().getWorld(), islandManager.getNetherWorld()) ? islandManager.getWorld() : islandManager.getNetherWorld();
                event.setTo(island.get().getCenter(world));
            } else {
                event.setCancelled(true);
        } else if (event.getCause().equals(TeleportCause.END_PORTAL) && config.endIslands) {
            if (island.isPresent()) {
                event.setSearchRadius(island.get().getSize() / 2);
                World world = Objects.equals(event.getFrom().getWorld(), islandManager.getEndWorld()) ? islandManager.getWorld() : islandManager.getEndWorld();
                event.setTo(LocationUtils.getSafeLocation(island.get().getCenter(world), island.get()));
            } else {
                event.setCancelled(true);
            }
        }
    }
}
