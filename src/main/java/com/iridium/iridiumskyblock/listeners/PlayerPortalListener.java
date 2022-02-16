package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.managers.CooldownProvider;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

public class PlayerPortalListener implements Listener {

    private final CooldownProvider<Player> cooldownProvider = CooldownProvider.newInstance(Duration.ofMillis(500));

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getPlayer().getWorld())) return;
        IslandManager islandManager = IridiumSkyblock.getInstance().getIslandManager();

        final Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getFrom());
        if (island.isEmpty())
            return;

        final User user = IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer());
        if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, PermissionType.PORTAL)) {
            if (hasNoCooldown(event.getPlayer())) {
                event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotUsePortal.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }

            event.setCancelled(true);
            return;
        }

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            if (IridiumSkyblock.getInstance().getConfiguration().netherIslands) {
                World world = Objects.equals(event.getFrom().getWorld(), islandManager.getNetherWorld()) ? islandManager.getWorld() : islandManager.getNetherWorld();
                event.setTo(island.get().getCenter(world));
                return;
            }

            event.setCancelled(true);
            if (hasNoCooldown(event.getPlayer())) {
                event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().netherIslandsDisabled.replace("%prefix", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
            return;
        }

        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) {
            event.setCancelled(true);

            if (IridiumSkyblock.getInstance().getConfiguration().endIslands) {
                World world = Objects.equals(event.getFrom().getWorld(), islandManager.getEndWorld()) ? islandManager.getWorld() : islandManager.getEndWorld();
                event.getPlayer().teleport(LocationUtils.getSafeLocation(island.get().getCenter(world), island.get()));
                return;
            }

            if (hasNoCooldown(event.getPlayer())) {
                event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().endIslandsDisabled.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        }
    }

    private boolean hasNoCooldown(Player player) {
        boolean cooldown = cooldownProvider.isOnCooldown(player);
        cooldownProvider.applyCooldown(player);
        return cooldown;
    }

}
