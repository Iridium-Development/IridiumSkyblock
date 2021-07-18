package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IslandEnterEvent;
import com.iridium.iridiumskyblock.api.IslandLeaveEvent;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        user.setBypass(false);

        // Update the internal username in case of name change
        user.setName(event.getPlayer().getName());

        // Send their island border and add player to cache
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(player.getLocation()).ifPresent(island -> {
                    Bukkit.getPluginManager().callEvent(new IslandEnterEvent(island, user));
                    island.getPlayersOnIsland().add(player);
                    PlayerUtils.sendBorder(player, island);
                }
        );

        if (player.isOp()) {
            Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () ->
                            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getConfiguration().prefix + " &7Thanks for using IridiumSkyblock, if you like the plugin, consider donating at &bwww.patreon.com/Peaches_MLG"))
                    , 5);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getPlayer().getLocation()).ifPresent(island -> {
                    User user = IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer());
                    Bukkit.getPluginManager().callEvent(new IslandLeaveEvent(island, user));
                    island.getPlayersOnIsland().remove(event.getPlayer());
                }
        );
    }

}
