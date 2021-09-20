package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.configs.Configuration;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandTrusted;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.managers.CooldownProvider;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityDamageListener implements Listener {

    private static final List<EntityDamageEvent.DamageCause> membersExcludedDamages = new ArrayList<>();
    private static final List<EntityDamageEvent.DamageCause> visitorsExcludedDamages = new ArrayList<>();
    private final CooldownProvider<Player> cooldownProvider = CooldownProvider.newInstance(Duration.ofMillis(500));

    //Refresh cause damages
    public static void refreshDamageCauses() {
        Configuration configuration = IridiumSkyblock.getInstance().getConfiguration();
        membersExcludedDamages.clear();
        visitorsExcludedDamages.clear();

        configuration.pvpSettings.membersPreventedDamages.forEach(cause -> {
            try {
                EntityDamageEvent.DamageCause damageCause = EntityDamageEvent.DamageCause.valueOf(cause);
                membersExcludedDamages.add(damageCause);
            } catch (IllegalArgumentException e) {
                IridiumSkyblock.getInstance().getLogger().warning("No any DamageCause named " + cause + " was found");
            }
        });

        configuration.pvpSettings.visitorsPreventedDamages.forEach(cause -> {
            try {
                EntityDamageEvent.DamageCause damageCause = EntityDamageEvent.DamageCause.valueOf(cause);
                visitorsExcludedDamages.add(damageCause);
            } catch (IllegalArgumentException e) {
                IridiumSkyblock.getInstance().getLogger().warning("No any DamageCause named " + cause + " was found");
            }
        });
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) ||
                event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE) ||
                event.getCause().equals(EntityDamageEvent.DamageCause.MAGIC)) {
            return;
        }

        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getEntity().getLocation());
        if (!island.isPresent()) return;

        if (event.getEntity() instanceof Player) {
            handlePlayerDamage(event, island.get());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity().equals(event.getDamager())) return;

        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getEntity().getLocation());
        if (!island.isPresent()) return;

        if (event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();
            if (event.getDamager() instanceof Player) {
                handleDamageBetweenPlayers(event, (Player) event.getDamager(), victim, island.get());
            } else if (event.getDamager() instanceof Projectile) {
                Projectile attacker = (Projectile) event.getDamager();
                if (attacker.getShooter() instanceof Player) {
                    handleDamageBetweenPlayers(event, (Player) attacker.getShooter(), victim, island.get());
                } else {
                    handlePlayerDamage(event, island.get());
                }
            } else {
                handlePlayerDamage(event, island.get());
            }
        } else if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(player), PermissionType.KILL_MOBS)) {
                return;
            }
            event.setCancelled(true);
            if (!messageIsOnCooldown(player)) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotHurtMobs.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        } else if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                Player player = (Player) projectile.getShooter();
                if (IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(player), PermissionType.KILL_MOBS)) {
                    return;
                }
                event.setCancelled(true);
                projectile.remove();
                if (!messageIsOnCooldown(player)) {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotHurtMobs.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            }
        }
    }

    public void handleDamageBetweenPlayers(EntityDamageByEntityEvent event, Player attacker, Player victim, Island island) {
        Configuration configuration = IridiumSkyblock.getInstance().getConfiguration();

        if (!configuration.pvpSettings.pvpOnIslands) {
            if (!messageIsOnCooldown(attacker)) {
                attacker.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotHurtPlayer.replace("%prefix%", configuration.prefix)));
            }

            if (event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) event.getDamager().remove();
            event.setCancelled(true);
            return;
        }

        User attackerUser = IridiumSkyblock.getInstance().getUserManager().getUser(attacker);
        User victimUser = IridiumSkyblock.getInstance().getUserManager().getUser(victim);
        Optional<IslandTrusted> attackerTrusted = IridiumSkyblock.getInstance().getIslandManager().getIslandTrusted(island, attackerUser);
        Optional<IslandTrusted> victimTrusted = IridiumSkyblock.getInstance().getIslandManager().getIslandTrusted(island, victimUser);
        boolean attackerIsMember = island.equals(attackerUser.getIsland().orElse(null)) || attackerTrusted.isPresent();
        boolean victimIsMember = island.equals(victimUser.getIsland().orElse(null)) || victimTrusted.isPresent();

        if (!configuration.pvpSettings.pvpBetweenMembers && attackerIsMember && victimIsMember) {
            if (!messageIsOnCooldown(attacker)) {
                attacker.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotHurtMember.replace("%prefix%", configuration.prefix)));
            }
            if (event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) event.getDamager().remove();
            event.setCancelled(true);
        }
    }

    private void handlePlayerDamage(EntityDamageEvent event, Island island) {
        Player player = (Player) event.getEntity();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<IslandTrusted> trusted = IridiumSkyblock.getInstance().getIslandManager().getIslandTrusted(island, user);
        boolean isMember = island.equals(user.getIsland().orElse(null)) || trusted.isPresent();

        if ((isMember && membersExcludedDamages.contains(event.getCause())) ||
                (!isMember && visitorsExcludedDamages.contains(event.getCause()))) {
            event.setCancelled(true);
        }
    }

    private boolean messageIsOnCooldown(Player player) {
        boolean cooldown = cooldownProvider.isOnCooldown(player);
        cooldownProvider.applyCooldown(player);
        return cooldown;
    }

}
