package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IslandEnterEvent;
import com.iridium.iridiumskyblock.api.IslandLeaveEvent;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Optional;

public class PlayerTeleportListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getTo() == null) return;
        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> fromIsland = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getFrom());
        Optional<Island> toIsland = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getTo());
        if ((!fromIsland.isPresent() && toIsland.isPresent()) || (fromIsland.isPresent() && toIsland.isPresent() && !fromIsland.get().equals(toIsland.get()))) {
            if (IridiumSkyblock.getInstance().getIslandManager().isBannedOnIsland(toIsland.get(), user)) {
                event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youHaveBeenBanned.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix).replace("%owner%", toIsland.get().getOwner().getName())));
                event.setCancelled(true);
                return;
            }
            Bukkit.getPluginManager().callEvent(new IslandEnterEvent(toIsland.get(), user));
            toIsland.get().getPlayersOnIsland().add(player);
            //TODO add enter island message if we want, i'll remove below message  when pr finished
            //TODO (i think islands should have a specific welcome message set with /is description or /is welcome etc)
            Bukkit.broadcastMessage("§aWelcome to " + toIsland.get().getOwner().getName() + "'s island");
        } else if ((fromIsland.isPresent() && !fromIsland.get().isInIsland(event.getTo()))) {
            if (toIsland.isPresent() && IridiumSkyblock.getInstance().getIslandManager().isBannedOnIsland(toIsland.get(), user)) {
                event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youHaveBeenBanned.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix).replace("%owner%", toIsland.get().getOwner().getName())));
                event.setCancelled(true);
                return;
            }
            Bukkit.getPluginManager().callEvent(new IslandLeaveEvent(fromIsland.get(), user));
            fromIsland.get().getPlayersOnIsland().remove(player);
            //TODO add leave island message if we want, i'll remove below message when pr finished
            Bukkit.broadcastMessage("§aYou have leave from " + fromIsland.get().getOwner().getName() + "'s island");
        }
        toIsland.ifPresent(island ->
                Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () ->
                                PlayerUtils.sendBorder(event.getPlayer(), island)
                        , 1)
        );
    }

}
