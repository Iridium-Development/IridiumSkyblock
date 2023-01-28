package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.Optional;

public class EntityDeathListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void monitorEntityDeath(EntityDeathEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getEntity().getWorld())) return;

        Player killer = event.getEntity().getKiller();
        if (killer != null) {
            User user = IridiumSkyblock.getInstance().getUserManager().getUser(killer);
            Optional<Island> island = user.getIsland();
            island.ifPresent(value -> {
                IridiumSkyblock.getInstance().getMissionManager().handleMissionUpdates(value, "KILL", event.getEntityType().name(), 1);

                IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island.get(), "experience");
                if (islandBooster.isActive()) {
                    event.setDroppedExp(event.getDroppedExp() * 2);
                }
            });
        }

        List<MetadataValue> list = event.getEntity().getMetadata("island_spawned");
        if (list.isEmpty()) return;
        int islandId = list.get(0).asInt();
        IridiumSkyblock.getInstance().getIslandManager().getIslandById(islandId).ifPresent(island -> IridiumSkyblock.getInstance().getIslandManager().onEntityRemoved(island, event.getEntity()));
    }

}
