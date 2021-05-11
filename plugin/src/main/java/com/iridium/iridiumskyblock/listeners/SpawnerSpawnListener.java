package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

import java.util.Optional;

public class SpawnerSpawnListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(SpawnerSpawnEvent event) {
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getLocation());
        if (island.isPresent()) {
            IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island.get(), "spawner");
            if (islandBooster.isActive()) {
                Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> event.getSpawner().setDelay(event.getSpawner().getDelay() / 2));
            }
        }
    }
}
