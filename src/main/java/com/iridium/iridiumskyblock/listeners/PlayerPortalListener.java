package com.iridium.iridiumskyblock.listeners;

import com.cryptomorin.xseries.reflection.XReflection;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Objects;
import java.util.Optional;

public class PlayerPortalListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {

        Player player = event.getPlayer();

        Optional<Island> islandCheck = IridiumSkyblock.getInstance().getTeamManager().getTeamViaPlayerLocation(player);

        // We want to allow teleportation from a non-skyblock world into a skyblock world, or from no island to an island.
        // We don't care if the player is not within an island.
        if(!islandCheck.isPresent()) {
            return;
        }

        World worldFrom = event.getFrom().getWorld();
        World worldTo = event.getTo().getWorld();

        World overworld = IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.NORMAL);
        World nether = IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.NETHER);
        World end = IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.THE_END);

        // We don't care if the player travels within the same world or dimension.
        if((worldTo != null) && (worldFrom == worldTo || worldFrom.getEnvironment() == worldTo.getEnvironment())) {
            return;
        }

        Island island = islandCheck.get();
        int level = island.getLevel();
        Location playerLocation = player.getLocation();
        World destination = worldFrom;

        // ==================
        // |  NETHER CHECK  |
        // ==================

        if(event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            // We don't teleport if the Nether is disabled.
            if(worldTo == null && nether == null) {
                event.setCancelled(true);
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().netherIslandsDisabled
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                ));
                return;
            }

            // We only teleport if the island's level allows it.
            if(level < IridiumSkyblock.getInstance().getConfiguration().netherUnlockLevel) {
                event.setCancelled(true);
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().netherLocked
                        .replace("%level%", String.valueOf(IridiumSkyblock.getInstance().getConfiguration().netherUnlockLevel))
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                ));
                return;
            }

            // Set teleport location
            destination = Objects.equals(worldFrom, nether) ? overworld : nether;
        }

        // ===============
        // |  END CHECK  |
        // ===============

        if(event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            // We don't teleport if the End is disabled.
            if(worldTo == null && end == null) {
                event.setCancelled(true);
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().endIslandsDisabled
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                ));
                return;
            }

            // We only teleport if the island's level allows it.
            if(level < IridiumSkyblock.getInstance().getConfiguration().endUnlockLevel) {
                event.setCancelled(true);
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().endLocked
                        .replace("%level%", String.valueOf(IridiumSkyblock.getInstance().getConfiguration().endUnlockLevel))
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                ));
                return;
            }

            // Set teleport location
            destination = Objects.equals(worldFrom, end) ? overworld : end;
        }

        event.setTo(island.getCenter(destination));

        // Finalize the teleport
        Location location = LocationUtils.getSafeLocation(island.getCenter(destination), island);

        if (location == null) {
            event.setCancelled(true);
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noSafeLocation
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));
            return;
        }

        location.setY(location.getY() + 1);
        if (XReflection.supports(15)) event.setCanCreatePortal(false);
        event.setTo(location);
    }
}
