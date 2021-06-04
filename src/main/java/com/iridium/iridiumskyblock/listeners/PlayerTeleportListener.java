package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getTo() != null) {
            IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getTo()).ifPresent(island ->
                    PlayerUtils.sendBorder(event.getPlayer(), island)
            );
        }
    }

}
