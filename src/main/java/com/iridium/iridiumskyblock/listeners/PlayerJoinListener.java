package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        user.setBypass(false);
        //Update incase of namechange
        user.setName(event.getPlayer().getName());
        //Send their island border
        Optional<Island> optionalIsland = IridiumSkyblockAPI.getInstance().getIslandViaLocation(player.getLocation());
        optionalIsland.ifPresent(island -> PlayerUtils.sendBorder(player, island));
    }

}
