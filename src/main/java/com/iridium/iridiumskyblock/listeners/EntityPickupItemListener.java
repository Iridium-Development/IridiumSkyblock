package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class EntityPickupItemListener implements Listener {

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onEntityPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getItem().getLocation());

        island.ifPresent(value -> {
            if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(value, IridiumSkyblock.getInstance().getUserManager().getUser(player), IridiumSkyblock.getInstance().getPermissions().pickupItems, "pickupItems")) {
                event.setCancelled(true);
            }
        });
    }
}
