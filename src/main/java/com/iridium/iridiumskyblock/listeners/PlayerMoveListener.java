package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandTime;
import com.iridium.iridiumskyblock.IslandWeatherType;
import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import com.iridium.iridiumskyblock.database.IslandSetting;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

public class PlayerMoveListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        if (event.getTo() != null) {
            if (event.getTo().getBlockX() != event.getFrom().getBlockX() || event.getTo().getBlockZ() != event.getFrom().getBlockZ()) {
                if (user.getTeleportingTask() != null) {
                    user.getTeleportingTask().cancel();
                    user.setTeleportingTask(null);
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportCanceled
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
                    );
                }
            }
        }
    }

}
