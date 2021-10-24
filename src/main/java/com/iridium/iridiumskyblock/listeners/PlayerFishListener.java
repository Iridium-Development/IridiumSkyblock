package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PlayerFishListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void monitorPlayerFish(PlayerFishEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getPlayer().getWorld())) return;

        Entity caughtEntity = event.getCaught();
        if (caughtEntity == null) return;

        User user = IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer());
        Optional<Island> island = user.getIsland();

        // Increment missions with the name of the caught item
        island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "FISH:" + caughtEntity.getType().name(), 1));

        // Increment missions with the ANY identifier
        island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "FISH:ANY", 1));

        // Checks all itemLists created in missions.yml
        for (Map.Entry<String, List<String>> itemList : IridiumSkyblock.getInstance().getItemLists().entrySet()) {
            // If the caught item matches one in the list
            // Increment missions with the name of the list as the identifier
            if (itemList.getValue().contains(caughtEntity.getType().name())) {
                island.ifPresent(value -> IridiumSkyblock.getInstance().getIslandManager().incrementMission(value, "FISH:" + itemList.getKey(), 1));
            }
        }
    }

}
