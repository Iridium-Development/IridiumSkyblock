package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.enhancements.VoidEnhancementData;
import com.iridium.iridiumteams.utils.LocationUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer());

        IridiumSkyblock.getInstance().getTeamManager().sendIslandBorder(event.getPlayer());

        user.getCurrentIsland().ifPresent(island -> {
            if (event.getPlayer().getLocation().getY() < LocationUtils.getMinHeight(event.getPlayer().getWorld())) {
                VoidEnhancementData voidEnhancementData = IridiumSkyblock.getInstance().getEnhancements().voidEnhancement.levels.get(IridiumSkyblock.getInstance().getTeamManager().getTeamEnhancement(island, "void").getLevel());
                if (voidEnhancementData != null && voidEnhancementData.enabled) {
                    event.getPlayer().teleport(island.getHome());
                    event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().VoidTeleport
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                    ));
                }
            }
        });
    }

}
