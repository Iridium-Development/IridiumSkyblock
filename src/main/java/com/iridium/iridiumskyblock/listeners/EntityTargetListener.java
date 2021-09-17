package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandTrusted;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.managers.CooldownProvider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class EntityTargetListener implements Listener {

    private final CooldownProvider<Entity> cooldownProvider = CooldownProvider.newInstance(Duration.ofMillis(500));

    @EventHandler
    public void onEntityTargetEntity(EntityTargetLivingEntityEvent event) {
        if (IridiumSkyblock.getInstance().getConfiguration().pvpSettings.mobsVisitorTargeting) return;

        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getEntity().getLocation());
        if (!island.isPresent()) return;

        if (event.getTarget() != null && event.getTarget() instanceof Player) {
            Player targetPlayer = (Player) event.getTarget();
            User targetUser = IridiumSkyblock.getInstance().getUserManager().getUser(targetPlayer);
            Optional<IslandTrusted> targetTrusted = IridiumSkyblock.getInstance().getIslandManager().getIslandTrusted(island.get(), targetUser);
            boolean targetIsMember = island.get().equals(targetUser.getIsland().orElse(null)) || targetTrusted.isPresent();

            if (targetIsMember) return;

            //Cancel event the player is not island member
            event.setCancelled(true);

            //if is not cooldown get next target
            if (!canNextTarget(event.getEntity())) {
                return;
            }

            //get next targetPlayer
            List<Player> nextTargets = IridiumSkyblock.getInstance().getIslandManager().getPlayersOnIsland(island.get()).stream()
                    .filter(user -> island.get().equals(user.getIsland().orElse(null)) || IridiumSkyblock.getInstance().getIslandManager().getIslandTrusted(island.get(), user).isPresent())
                    .map(User::toPlayer)
                    .filter(player -> player.hasLineOfSight(event.getEntity()))
                    .collect(Collectors.toList());

            //return if empty
            if (nextTargets.isEmpty()) {
                return;
            }

            //Set target of Living Entity to next target if present
            Player nextTarget = nextTargets.get(ThreadLocalRandom.current().nextInt(nextTargets.size()));
            event.setTarget(nextTarget);
        }

    }

    private boolean canNextTarget(Entity entity) {
        boolean cooldown = cooldownProvider.isOnCooldown(entity);
        cooldownProvider.applyCooldown(entity);
        return !cooldown;
    }

}
