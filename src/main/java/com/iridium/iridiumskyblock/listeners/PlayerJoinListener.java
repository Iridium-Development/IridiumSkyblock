package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        IridiumSkyblock.getInstance().getTeamManager().sendIslandBorder(event.getPlayer());
    }

    @EventHandler
    public void onServerJoin(PlayerJoinEvent event) {
        if(IridiumSkyblock.getInstance().getConfiguration().islandCreateOnJoin && !event.getPlayer().hasPlayedBefore())
            Bukkit.getServer().dispatchCommand(event.getPlayer(), "is create");
    }
}
