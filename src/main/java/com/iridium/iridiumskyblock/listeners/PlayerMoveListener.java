package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Only check if player actually moved blocks (not just head movement)
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        Player player = event.getPlayer();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);

        // Check if they're in a skyblock world
        if (!IridiumSkyblock.getInstance().getIslandManager().isInSkyblockWorld(event.getTo().getWorld())) {
            return;
        }

        Optional<Island> fromIsland = user.getCurrentIsland();
        Optional<Island> toIsland = user.getCurrentIsland(event.getTo());

        // If they moved to a different island (or from no island to an island, or vice versa)
        if (!fromIsland.equals(toIsland)) {
            IridiumSkyblock.getInstance().getIslandManager().sendIslandBorder(player);

            // Send title if configured
            toIsland.ifPresent(island ->
                    IridiumSkyblock.getInstance().getIslandManager().sendTeamTitle(player, island)
            );
        }
    }
}