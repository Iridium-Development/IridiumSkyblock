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

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EntityDeathListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void monitorEntityDeath(EntityDeathEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getEntity().getWorld())) return;

        Player player = event.getEntity().getKiller();
        if (player == null) return;

        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        island.ifPresent(value -> {
            // Increment missions with the name of the killed entity
            IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "KILL:" + event.getEntityType().name(), 1);

            // Increment missions with the ANY identifier
            IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "KILL:ANY", 1);

            // Checks all itemLists created in missions.yml
            for (Map.Entry<String, List<String>> itemList : IridiumSkyblock.getInstance().getItemLists().entrySet()) {
                // If the killed entity matches one in the list
                // Increment missions with the name of the list as the identifier
                if (itemList.getValue().contains(event.getEntityType().name())) {
                    IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "KILL:" + itemList.getKey(), 1);
                }
            }

            IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island.get(), "experience");
            if (islandBooster.isActive()) {
                event.setDroppedExp(event.getDroppedExp() * 2);
            }
        });
    }

}
