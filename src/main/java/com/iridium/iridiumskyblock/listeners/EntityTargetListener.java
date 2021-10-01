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
        if (event.getTarget() == null || !(event.getTarget() instanceof Player)) return;

        Player targetPlayer = (Player) event.getTarget();
        User targetUser = IridiumSkyblock.getInstance().getUserManager().getUser(targetPlayer);
        Optional<IslandTrusted> targetTrusted = IridiumSkyblock.getInstance().getIslandManager().getIslandTrusted(island.get(), targetUser);
        if (island.get().equals(targetUser.getIsland().orElse(null)) || targetTrusted.isPresent()) return;

        //if is not cooldown get next target
        if (!canNextTarget(event.getEntity())) {
            return;
        }

        //get next targetPlayer
        List<Player> nextTargets = event.getEntity().getNearbyEntities(8, 8, 8).stream()
                .filter(entity -> entity instanceof Player)
                .filter(entity -> island.get().isInIsland(entity.getLocation()))
                .map(entity -> (Player) entity)
                .filter(player -> island.get().equals(IridiumSkyblock.getInstance().getUserManager().getUser(player).getIsland().orElse(null)))
                .filter(player -> IridiumSkyblock.getInstance().getIslandManager().getIslandTrusted(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(player)).isPresent())
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

    private boolean canNextTarget(Entity entity) {
        boolean cooldown = cooldownProvider.isOnCooldown(entity);
        cooldownProvider.applyCooldown(entity);
        return !cooldown;
    }

}
