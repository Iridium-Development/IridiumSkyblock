package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getTo() == null) return;
        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);

        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getTo()).ifPresent(island -> {
                    if (IridiumSkyblock.getInstance().getIslandManager().isBannedOnIsland(island, user)) {
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youHaveBeenBanned
                                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                                .replace("%owner%", island.getOwner().getName())
                                .replace("%name%", island.getName())
                        ));
                        event.setCancelled(true);
                    } else if (!IridiumSkyblockAPI.getInstance().canVisitIsland(user, island)) {
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandIsPrivate.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                        event.setCancelled(true);
                    } else {
                        Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> PlayerUtils.sendBorder(player, island), 1);
                    }
                }
        );
    }

}
