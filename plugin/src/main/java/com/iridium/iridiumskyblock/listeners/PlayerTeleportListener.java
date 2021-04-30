package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Optional;

public class PlayerTeleportListener implements Listener {

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Optional<Island> optionalIsland = IridiumSkyblockAPI.getInstance().getIslandViaLocation(event.getTo());
        optionalIsland.ifPresent(island -> PlayerUtils.sendBorder(player, island));
    }

}
