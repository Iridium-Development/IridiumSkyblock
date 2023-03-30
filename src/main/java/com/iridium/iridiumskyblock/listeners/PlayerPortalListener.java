package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Objects;

public class PlayerPortalListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        IridiumSkyblock.getInstance().getTeamManager().getTeamViaLocation(event.getFrom()).ifPresent(island -> {
            if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
                if (island.getLevel() < IridiumSkyblock.getInstance().getConfiguration().netherUnlockLevel) {
                    event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().netherLocked
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                    ));
                    return;
                }
                World nether = IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.NETHER);
                if (nether == null) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().netherIslandsDisabled
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                    ));
                    return;
                }
                World world = Objects.equals(event.getFrom().getWorld(), nether) ? IridiumSkyblock.getInstance().getTeamManager().getWorld(World.Environment.NORMAL) : nether;
                event.setTo(island.getCenter(world));
            }
        });
    }

}
