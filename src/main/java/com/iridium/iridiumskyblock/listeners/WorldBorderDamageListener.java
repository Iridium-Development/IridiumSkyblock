package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class WorldBorderDamageListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        // Cancel world border damage in skyblock worlds
        if (event.getCause() == EntityDamageEvent.DamageCause.WORLD_BORDER) {
            if (IridiumSkyblock.getInstance().getIslandManager().isInSkyblockWorld(player.getWorld())) {
                event.setCancelled(true);
            }
        }
    }
}