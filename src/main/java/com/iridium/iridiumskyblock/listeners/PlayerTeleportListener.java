package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
            if (user.isFlying()) {
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
            return;
        }

        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getTo()).ifPresent(island ->
                Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () ->
                                PlayerUtils.sendBorder(event.getPlayer(), island)
                        , 1)
        );
    }

}
