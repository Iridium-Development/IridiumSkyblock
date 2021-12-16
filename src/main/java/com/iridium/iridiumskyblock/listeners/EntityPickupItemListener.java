package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.Optional;

public class EntityPickupItemListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getItem().getLocation());
            if (island.isEmpty()) return;

            if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(player), PermissionType.PICKUP_ITEMS)) {
                event.setCancelled(true);
            }
        }
    }

}
