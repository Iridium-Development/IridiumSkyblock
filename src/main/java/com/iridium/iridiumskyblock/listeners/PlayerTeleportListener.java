package com.iridium.iridiumskyblock.listeners;

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

        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getTo().getWorld())) {
            IridiumSkyblock.getInstance().getUserManager().disableFlight(user);
            return;
        }

        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getTo()).ifPresent(island ->
                Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () ->
                                PlayerUtils.sendBorder(player, island)
                        , 1)
        );
    }

}
