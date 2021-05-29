package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import org.bukkit.Bukkit;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpawnerSpawnListener implements Listener {

    /**
     * Spawners spawn multiple entities per SpawnerSpawnEvent.
     * This list locks the spawner from getting a smaller delay multiple times per cycle.
     */
    private List<CreatureSpawner> lock = new ArrayList<>();

    @EventHandler
    public void onCreatureSpawn(SpawnerSpawnEvent event) {
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getLocation());
        if (island.isPresent()) {
            IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island.get(), "spawner");
            if (islandBooster.isActive()) {
                CreatureSpawner spawner = event.getSpawner();
                lock.add(spawner);
                Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> {
                    spawner.setDelay(event.getSpawner().getDelay() / 2);
                    spawner.update();
                    lock.remove(spawner);
                });
            }
        }
    }
}
