package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IslandEnterEvent;
import com.iridium.iridiumskyblock.api.IslandLeaveEvent;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

public class PlayerMoveListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        if (event.getTo() != null && (event.getTo().getBlockX() != event.getFrom().getBlockX() || event.getTo().getBlockZ() != event.getFrom().getBlockZ())) {
            if (user.getTeleportingTask() != null) {
                user.getTeleportingTask().cancel();

                user.setTeleportingTask(null);
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportCanceled
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
                );
            }
            Optional<Island> fromIsland = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getFrom());
            Optional<Island> toIsland = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getTo());
            if ((!fromIsland.isPresent() && toIsland.isPresent()) || (fromIsland.isPresent() && toIsland.isPresent() && !fromIsland.get().equals(toIsland.get()))) {
                Bukkit.getPluginManager().callEvent(new IslandEnterEvent(toIsland.get(), user));
                toIsland.get().getPlayersOnIsland().add(player);
                if (IridiumSkyblock.getInstance().getDatabaseManager().getIslandBanTableManager().getEntries(toIsland.get()).stream().anyMatch(islandBan -> islandBan.getRestrictedUser().equals(IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer())) && !islandBan.isRevoked())) {
                    event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youHaveBeenBanned.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix).replace("%owner%", toIsland.get().getOwner().getName())));
                    event.setCancelled(true);
                    return;
                }
                //TODO add enter island message if we want, i'll remove below message  when pr finished
                Bukkit.broadcastMessage("§aWelcome to " + toIsland.get().getOwner().getName() + "'s island");
            } else if ((fromIsland.isPresent() && !fromIsland.get().isInIsland(event.getTo()))) {
                if (toIsland.isPresent() && IridiumSkyblock.getInstance().getDatabaseManager().getIslandBanTableManager().getEntries(toIsland.get()).stream().anyMatch(islandBan -> islandBan.getRestrictedUser().equals(IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer())) && !islandBan.isRevoked())) {
                    event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youHaveBeenBanned.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix).replace("%owner%", toIsland.get().getOwner().getName())));
                    event.setCancelled(true);
                    return;
                }
                Bukkit.getPluginManager().callEvent(new IslandLeaveEvent(fromIsland.get(), user));
                fromIsland.get().getPlayersOnIsland().remove(player);
                //TODO add enter island message if we want, i'll remove below message  when pr appovored
                Bukkit.broadcastMessage("§aYou have leave from " + fromIsland.get().getOwner().getName() + "'s island");
            }
            if (user.isFlying()) {
                if (fromIsland.isPresent()) {
                    IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(fromIsland.get(), "flight");
                    if (!islandBooster.isActive() && !player.hasPermission("iridiumskyblock.fly")) {
                        user.setFlying(false);
                        if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
                            player.setFlying(false);
                            player.setAllowFlight(false);
                            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().flightDisabled
                                    .replace("%player%", player.getName())
                                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
                            );
                        }
                    }
                }
            }
        }

        if (event.getTo().getY() < 0 & IridiumSkyblock.getInstance().getConfiguration().voidTeleport) {
            Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getFrom());
            island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().teleportHome(player, value, 0));
        }
    }

}
