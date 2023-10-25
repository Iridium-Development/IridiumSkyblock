package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Optional;

@AllArgsConstructor
public class EntityDamageListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (IridiumSkyblock.getInstance().getConfiguration().allowPvPOnIslands) return;
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getTeamViaLocation(event.getEntity().getLocation());
        if (!island.isPresent()) return;

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
            if (user.getTeamID() != island.get().getId()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (IridiumSkyblock.getInstance().getConfiguration().allowPvPOnIslands) return;
        if (event.getEntity().equals(event.getDamager())) return;

        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getTeamViaLocation(event.getEntity().getLocation());
        if (!island.isPresent()) return;

        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof Player) {
                event.setCancelled(true);
                event.getDamager().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotHurtPlayers
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                ));
                return;
            }
            if (event.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) event.getDamager();
                if (!(projectile.getShooter() instanceof Player)) return;
                event.setCancelled(true);
                event.getDamager().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotHurtPlayers
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                ));
                return;
            }
            return;
        }
    }
}
