package com.iridium.iridiumskyblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

public class SpawnerSpawnListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(SpawnerSpawnEvent event) {
//        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getLocation());
//        if (island.isPresent()) {
//            IslandUpgrade islandUpgrade = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(island.get(), "spawner");
//            double modifier = IridiumSkyblock.getInstance().getUpgrades().spawnerUpgrade.upgrades.get(islandUpgrade.getLevel()).modifier;
//            Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> event.getSpawner().setDelay((int) (event.getSpawner().getDelay() / modifier)));
//        }
    }
}
