package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer());

        IridiumSkyblock.getInstance().getTeamManager().sendIslandBorder(event.getPlayer());
    }

}
