package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
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
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        user.setBypass(false);

        // Update the internal username in case of name change
        user.setName(event.getPlayer().getName());

        // Send their island border
        Optional<Island> optionalIsland = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(player.getLocation());
        optionalIsland.ifPresent(island -> PlayerUtils.sendBorder(player, island));
    }

}
