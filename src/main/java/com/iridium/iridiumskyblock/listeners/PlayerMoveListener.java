package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

public class PlayerMoveListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        if (event.getTo().getBlockX() != event.getFrom().getBlockX() || event.getTo().getBlockZ() != event.getFrom().getBlockZ()) {
            if (user.getTeleportingTask() != null) {
                user.getTeleportingTask().cancel();
                user.setTeleportingTask(null);
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportCanceled.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }

            Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player);
            if (island.isPresent()) {
                handleIslandBan(event, player, user, island);
            }

            if (user.isFlying()) {
                handleFlyingUser(player, user, island);
            }
        }

        if (event.getTo().getY() < 0 && IridiumSkyblock.getInstance().getConfiguration().voidTeleport && IridiumSkyblockAPI.getInstance().isIslandWorld(player.getWorld())) {
            handleVoidTeleport(player, user);
        }
    }

    private void handleIslandBan(PlayerMoveEvent event, Player player, User user, Optional<Island> island) {
        if (IridiumSkyblock.getInstance().getIslandManager().isBannedOnIsland(island.get(), user)) {
            event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youHaveBeenBanned
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                    .replace("%owner%", island.get().getOwner().getName())
                    .replace("%name%", island.get().getName())
            ));

            PlayerUtils.teleportSpawn(player);
        }
    }

    private void handleFlyingUser(Player player, User user, Optional<Island> island) {
        if (island.isPresent()) {
            IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island.get(), "flight");
            if (!islandBooster.isActive() && !player.hasPermission("iridiumskyblock.fly")) {
                disableFlight(player, user);
            }
        } else if (!player.hasPermission("iridiumskyblock.fly")) {
            disableFlight(player, user);
        }
    }

    private void disableFlight(Player player, User user) {
        user.setFlying(false);
        if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
            player.setFlying(false);
            player.setAllowFlight(false);

            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().flightDisabled
                .replace("%player%", player.getName())
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));
        }
    }

    private void handleVoidTeleport(Player player, User user) {
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player);
        if (island.isPresent()) {
            IridiumSkyblock.getInstance().getIslandManager().teleportHome(player, island.get(), 0);
        } else {
            Optional<Island> userIsland = user.getIsland();
            if (userIsland.isPresent()) {
                IridiumSkyblock.getInstance().getIslandManager().teleportHome(player, userIsland.get(), 0);
            } else {
                PlayerUtils.teleportSpawn(player);
            }
        }
    }

}
