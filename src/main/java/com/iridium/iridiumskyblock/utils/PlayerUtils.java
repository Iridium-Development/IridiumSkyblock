package com.iridium.iridiumskyblock.utils;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumteams.support.SpawnSupport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PlayerUtils {

    public static void teleportSpawn(Player player) {
        player.teleport(getSpawn(player));
    }

    public static Location getSpawn(Player player) {

        if(IridiumSkyblock.getInstance().getConfiguration().spawnOnIsland) {
            Optional<Island> island = IridiumSkyblock.getInstance().getTeamManager().getTeamViaNameOrPlayer(player.getName());
            if(island.isPresent()) {
                return island.get().getHome();
            }
        }

        for(SpawnSupport spawnSupport : IridiumSkyblock.getInstance().getSupportManager().getSpawnSupport()) {
            return spawnSupport.getSpawn(player);
        }

        World spawnWorld = Bukkit.getWorld(IridiumSkyblock.getInstance().getConfiguration().spawnWorldName);
        if (spawnWorld != null) {
            return spawnWorld.getSpawnLocation();
        }

        return Bukkit.getWorlds().get(0).getSpawnLocation();
    }
}