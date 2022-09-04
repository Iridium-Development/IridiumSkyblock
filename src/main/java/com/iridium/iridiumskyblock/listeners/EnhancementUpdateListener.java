package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.api.EnhancementUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EnhancementUpdateListener implements Listener {

    @EventHandler
    public void onEnhancementUpdateEvent(EnhancementUpdateEvent<Island, User> event) {
        if (event.getEnhancement().equals("size")) {
            Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () ->
                    IridiumSkyblock.getInstance().getTeamManager().getMembersOnIsland(event.getTeam()).forEach(user -> IridiumSkyblock.getInstance().getTeamManager().sendIslandBorder(user.getPlayer()))
            );
        }
    }

}
