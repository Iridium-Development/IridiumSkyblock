package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        user.setBypassing(false);

        // Update the internal username in case of name change
        user.setName(event.getPlayer().getName());

        // Send their island border
        IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player).ifPresent(island ->
                PlayerUtils.sendBorder(player, island)
        );

        if (player.isOp() && IridiumSkyblock.getInstance().getConfiguration().patreonMessage) {
            Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () ->
                            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getConfiguration().prefix + " &7Thanks for using IridiumSkyblock, if you like the plugin, consider donating at &bwww.patreon.com/Peaches_MLG"))
                    , 5);
        }

        IridiumSkyblock.getInstance().saveDataPlayer(user).join(); // Docta - new save
    }
}
