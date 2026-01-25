package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Optional;

public class PlayerPortalListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();

        if (!IridiumSkyblock.getInstance().getIslandManager().isInSkyblockWorld(from.getWorld())) {
            return;
        }

        // ALWAYS resolve island from LOCATION (never cache)
        Optional<Island> islandOptional =
                IridiumSkyblock.getInstance().getIslandManager().getTeamViaLocation(from);

        if (!islandOptional.isPresent()) {
            event.setCancelled(true);
            player.sendMessage("no");
            return;
        }

        Island island = islandOptional.get();
        World targetWorld = null;
        Location destination = null;

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            destination = handleNetherPortal(event, island);
        } else if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            destination = handleEndPortal(event, island);
        }

        if (destination == null) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);

        Location finalDestination = destination;
        Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> {
            if (!player.isOnline()) return;

            player.teleport(finalDestination);

            // Border update AFTER teleport
            Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> {
                if (player.isOnline()) {
                    IridiumSkyblock.getInstance().getIslandManager().sendIslandBorder(player);
                }
            }, 5L);
        }, 1L);
    }

    /* ==================== NETHER ==================== */

    private Location handleNetherPortal(PlayerPortalEvent event, Island island) {
        Location from = event.getFrom();
        World fromWorld = from.getWorld();

        String overworld = IridiumSkyblock.getInstance().getConfiguration().worldName;
        String nether = overworld + "_nether";

        if (!IridiumSkyblock.getInstance().getConfiguration()
                .enabledWorlds.getOrDefault(World.Environment.NETHER, true)) {
            event.getPlayer().sendMessage(
                    IridiumSkyblock.getInstance().getMessages().netherIslandsDisabled);
            return null;
        }

        if (island.getLevel() <
                IridiumSkyblock.getInstance().getConfiguration().netherUnlockLevel) {
            event.getPlayer().sendMessage(
                    IridiumSkyblock.getInstance().getMessages().netherLocked
                            .replace("%level%",
                                    String.valueOf(IridiumSkyblock.getInstance()
                                            .getConfiguration().netherUnlockLevel)));
            return null;
        }

        if (fromWorld.getName().equals(overworld)) {
            World target = Bukkit.getWorld(nether);
            return target == null ? null : calculateDestination(from, island, target);
        }

        if (fromWorld.getName().equals(nether)) {
            World target = Bukkit.getWorld(overworld);
            return target == null ? null : calculateDestination(from, island, target);
        }

        return null;
    }

    /* ==================== END ==================== */

    private Location handleEndPortal(PlayerPortalEvent event, Island island) {
        Location from = event.getFrom();
        World fromWorld = from.getWorld();

        String overworld = IridiumSkyblock.getInstance().getConfiguration().worldName;
        String end = overworld + "_the_end";

        if (!IridiumSkyblock.getInstance().getConfiguration()
                .enabledWorlds.getOrDefault(World.Environment.THE_END, true)) {
            event.getPlayer().sendMessage(
                    IridiumSkyblock.getInstance().getMessages().endIslandsDisabled);
            return null;
        }

        if (island.getLevel() <
                IridiumSkyblock.getInstance().getConfiguration().endUnlockLevel) {
            event.getPlayer().sendMessage(
                    IridiumSkyblock.getInstance().getMessages().endLocked
                            .replace("%level%",
                                    String.valueOf(IridiumSkyblock.getInstance()
                                            .getConfiguration().endUnlockLevel)));
            return null;
        }

        if (fromWorld.getName().equals(end)) {
            return island.getHome();
        }

        World target = Bukkit.getWorld(end);
        if (target == null) return null;

        Location loc = island.getCenter(target).clone();
        loc.setY(64);
        return loc;
    }

    /* ==================== DESTINATION ==================== */

    private Location calculateDestination(Location from, Island island, World targetWorld) {
        Location fromCenter = island.getCenter(from.getWorld());
        Location toCenter = island.getCenter(targetWorld);

        double dx = from.getX() - fromCenter.getX();
        double dz = from.getZ() - fromCenter.getZ();

        Location dest = new Location(
                targetWorld,
                toCenter.getX() + dx,
                64,
                toCenter.getZ() + dz
        );

        return findSafeLocation(dest, island);
    }

    private Location findSafeLocation(Location loc, Island island) {
        World w = loc.getWorld();
        int x = loc.getBlockX();
        int z = loc.getBlockZ();

        for (int y = w.getMaxHeight() - 1; y > w.getMinHeight(); y--) {
            if (!island.isInIsland(new Location(w, x, y, z))) continue;

            if (w.getBlockAt(x, y, z).getType().isSolid()
                    && !w.getBlockAt(x, y + 1, z).getType().isSolid()
                    && !w.getBlockAt(x, y + 2, z).getType().isSolid()) {
                return new Location(w, x + 0.5, y + 1, z + 0.5);
            }
        }

        return island.getHome();
    }
}
