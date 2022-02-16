package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;

import java.util.Optional;

public class PlayerBucketFillListener implements Listener {

    @EventHandler
    public void onMilkBucket(PlayerBucketFillEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(player.getWorld())) return;
        if (event.getItemStack() == null) return;
        Bukkit.getScheduler().scheduleSyncDelayedTask(IridiumSkyblock.getInstance(), () -> {
            Optional<Island> optionalIsland = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(player.getLocation());

            if (optionalIsland.isPresent()) {
                Island island = optionalIsland.get();
                IridiumSkyblock.getInstance().getMissionManager().handleMissionUpdates(island, "BUCKET", event.getItemStack().getType().name(), 1);
            }
        });
    }
}
