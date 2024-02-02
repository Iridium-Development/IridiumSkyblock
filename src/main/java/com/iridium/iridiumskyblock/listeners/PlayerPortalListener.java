package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import org.bukkit.Location;
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
                            .replace("%level%", String.valueOf(IridiumSkyblock.getInstance().getConfiguration().netherUnlockLevel))
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                    ));
                    event.setCancelled(true);
                    return;
                }
                World nether = IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.NETHER);
                if (nether == null) {
                    event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().netherIslandsDisabled
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                    ));
                    event.setCancelled(true);
                    return;
                }
                World world = Objects.equals(event.getFrom().getWorld(), nether) ? IridiumSkyblock.getInstance().getTeamManager().getWorld(World.Environment.NORMAL) : nether;
                event.setTo(island.getCenter(world));
            } else if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
                if (island.getLevel() < IridiumSkyblock.getInstance().getConfiguration().endUnlockLevel) {
                    event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().endLocked
                            .replace("%level%", String.valueOf(IridiumSkyblock.getInstance().getConfiguration().endUnlockLevel))
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                    ));
                    event.setCancelled(true);
                    return;
                }
                World end = IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.THE_END);
                if (end == null) {
                    event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().endIslandsDisabled
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                    ));
                    event.setCancelled(true);
                    return;
                }
                World world = Objects.equals(event.getFrom().getWorld(), end) ? IridiumSkyblock.getInstance().getTeamManager().getWorld(World.Environment.NORMAL) : end;
                Location location = LocationUtils.getSafeLocation(island.getCenter(world), island);
                if (location == null) {
                    event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noSafeLocation
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                    ));
                    event.setCancelled(true);
                    return;
                }
                location.setY(location.getY() + 1);
                if(XMaterial.supports(15)) event.setCanCreatePortal(false);
                event.setTo(location);
            }
        });
    }
}
