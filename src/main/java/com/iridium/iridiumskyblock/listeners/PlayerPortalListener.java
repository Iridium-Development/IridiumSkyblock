package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerPortalListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        World fromWorld = event.getFrom().getWorld();

        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(fromWorld)) return;

        IslandManager islandManager = IridiumSkyblock.getInstance().getIslandManager();
        Optional<Island> island = islandManager.getIslandViaLocation(event.getFrom());
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer());

        if (island.isPresent() && !IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, PermissionType.PORTAL)) {
            event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotUsePortal.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            event.setCancelled(true);

            if (island.get().isVisitable() || user.getIsland().map(Island::getId).orElse(0) == island.get().getId()) {
                event.getPlayer().teleport(island.get().getHome());
            } else {
                PlayerUtils.teleportSpawn(event.getPlayer());
            }

        } else if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL && IridiumSkyblock.getInstance().getConfiguration().netherIslands) {

            if (island.isPresent()) {
                World world = Objects.equals(fromWorld, islandManager.getNetherWorld()) ? islandManager.getWorld() : islandManager.getNetherWorld();
                event.setTo(island.get().getCenter(world));
            } else {
                event.setCancelled(true);
            }

        } else if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL) && IridiumSkyblock.getInstance().getConfiguration().endIslands) {

            if (island.isPresent()) {
                World world = Objects.equals(fromWorld, islandManager.getEndWorld()) ? islandManager.getWorld() : islandManager.getEndWorld();
                event.setTo(island.get().getCenter(world));
            } else {
                event.setCancelled(true);
            }

        }
    }

}

