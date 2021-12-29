package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import com.iridium.iridiumskyblock.managers.CooldownProvider;
import org.bukkit.Bukkit;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

import java.time.Duration;
import java.util.Optional;

public class SpawnerSpawnListener implements Listener {

    private final CooldownProvider<CreatureSpawner> cooldownProvider = CooldownProvider.newInstance(Duration.ofMillis(50));

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(SpawnerSpawnEvent event) {
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getLocation());
        if (island.isPresent()) {
            IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island.get(), "spawner");
            if (islandBooster.isActive()) {
                CreatureSpawner spawner = event.getSpawner();
                if (!cooldownProvider.isOnCooldown(spawner)) {
                    cooldownProvider.applyCooldown(spawner);
                    Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> {
                        spawner.setDelay(spawner.getDelay() / 2);
                        spawner.update();
                    });
                }
            }
        }
    }

}
