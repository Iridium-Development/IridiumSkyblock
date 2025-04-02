package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.api.EnhancementUpdateEvent;
import com.iridium.iridiumteams.database.TeamLog;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.LocalDateTime;

public class EnhancementUpdateListener implements Listener {

    @EventHandler
    public void onEnhancementUpdateEvent(EnhancementUpdateEvent<Island, User> event) {
        if (event.getEnhancement().equals("size")) {
            Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () ->
                    IridiumSkyblock.getInstance().getTeamManager().getMembersOnIsland(event.getTeam()).forEach(user -> IridiumSkyblock.getInstance().getTeamManager().sendIslandBorder(user.getPlayer()))
            );
        }

        IridiumSkyblock.getInstance().getTeamManager().addTeamLog(new TeamLog(
                event.getTeam(),
                event.getUser().getPlayer().getUniqueId(),
                "enhancement_update",
                1,
                event.getUser().getPlayer().getLocation(),
                LocalDateTime.now(),
                event.getEnhancement()
        ));
    }

}
