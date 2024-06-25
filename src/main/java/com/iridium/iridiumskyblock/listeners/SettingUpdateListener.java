package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.api.SettingUpdateEvent;
import com.iridium.iridiumteams.database.TeamLog;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.LocalDateTime;

public class SettingUpdateListener implements Listener {

    @EventHandler
    public void onSettingUpdateEvent(SettingUpdateEvent<Island, User> event) {
        IridiumSkyblock.getInstance().getTeamManager().addTeamLog(new TeamLog(
                event.getTeam(),
                event.getUser().getPlayer().getUniqueId(),
                "setting_update",
                1,
                event.getUser().getPlayer().getLocation(),
                LocalDateTime.now(),
                event.getSetting() + ": " + event.getValue()
        ));
    }

}
