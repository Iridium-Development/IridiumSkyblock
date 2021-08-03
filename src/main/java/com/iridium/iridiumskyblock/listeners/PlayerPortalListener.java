package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Objects;
import java.util.Optional;

public class PlayerPortalListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        IslandManager islandManager = IridiumSkyblock.getInstance().getIslandManager();

        final Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getFrom());
        if (!island.isPresent())
            return;

        final User user = IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer());
        if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, PermissionType.PORTAL)) {
            event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotUsePortal.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            event.setCancelled(true);

            // Teleport them back to the island to prevent constant chat spam.
            if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {

                if (island.get().isVisitable()) {
                    event.getPlayer().teleport(island.get().getHome());
                } else {
                    PlayerUtils.teleportSpawn(event.getPlayer());
                }

            }
            return;
        }

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            if (IridiumSkyblock.getInstance().getConfiguration().netherIslands) {
                World world = Objects.equals(event.getFrom().getWorld(), islandManager.getNetherWorld()) ? islandManager.getWorld() : islandManager.getNetherWorld();
                event.setTo(island.get().getCenter(world));
                return;
            }

            event.setCancelled(true);
            event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().netherIslandsDisabled.replace("%prefix", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }

        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) {
            event.setCancelled(true);

            if (IridiumSkyblock.getInstance().getConfiguration().endIslands) {
                World world = Objects.equals(event.getFrom().getWorld(), islandManager.getEndWorld()) ? islandManager.getWorld() : islandManager.getEndWorld();
                event.getPlayer().teleport(LocationUtils.getSafeLocation(island.get().getCenter(world), island.get()));
                return;
            }

            event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().endIslandsDisabled.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
    }

}
