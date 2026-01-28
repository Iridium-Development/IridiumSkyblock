package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.managers.IslandRegionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PlayerMoveListener implements Listener {

    // Cooldown map to prevent message spam (player UUID -> last message time)
    private final Map<UUID, Long> messageCooldowns = new HashMap<>();
    private static final long MESSAGE_COOLDOWN_MS = 2000; // 2 seconds between messages

    @EventHandler(priority = EventPriority.HIGH)
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

        // Check for WorldGuard region exit if enabled
        IslandRegionManager regionManager = IridiumSkyblock.getInstance().getIslandRegionManager();
        if (regionManager != null && IridiumSkyblock.getInstance().getConfiguration().useWorldGuardRegions) {
            if (regionManager.isExitingIslandRegion(player, event.getFrom(), event.getTo())) {
                // Block the movement - teleport player back
                event.setTo(event.getFrom());

                // Send message with cooldown to prevent spam
                long currentTime = System.currentTimeMillis();
                Long lastMessage = messageCooldowns.get(player.getUniqueId());
                if (lastMessage == null || currentTime - lastMessage > MESSAGE_COOLDOWN_MS) {
                    player.sendMessage(StringUtils.color(
                            IridiumSkyblock.getInstance().getMessages().regionExitDenied
                                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                    ));
                    messageCooldowns.put(player.getUniqueId(), currentTime);
                }
                return;
            }
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