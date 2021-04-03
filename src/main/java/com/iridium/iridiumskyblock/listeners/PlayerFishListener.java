package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandMission;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Optional;

public class PlayerFishListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerFishEventMonitor(PlayerFishEvent event) {
        if (event.getCaught() == null) return;
        Player player = event.getPlayer();
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = user.getIsland();
        if (island.isPresent()) {
            for (String key : IridiumSkyblock.getInstance().getMissionsList().keySet()) {
                Mission mission = IridiumSkyblock.getInstance().getMissionsList().get(key);
                for (int i = 1; i <= mission.getMissions().size(); i++) {
                    String[] conditions = mission.getMissions().get(i - 1).toUpperCase().split(":");
                    if (conditions[0].equals("FISH") && (conditions[1].equals(event.getCaught().getType().name()) || conditions[1].equals("ANY"))) {
                        IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island.get(), mission, key, i);
                        if (islandMission.getProgress() >= Integer.parseInt(conditions[2])) {
                            //Check if Mission is completed
                        } else {
                            islandMission.setProgress(islandMission.getProgress() + 1);
                        }
                    }
                }
            }
        }
    }
}
