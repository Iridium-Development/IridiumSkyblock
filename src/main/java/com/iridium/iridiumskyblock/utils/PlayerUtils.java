package com.iridium.iridiumskyblock.utils;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.spawn.EssentialsSpawn;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PlayerUtils {
    public static void teleportSpawn(Player player) {
        World spawnWorld = Bukkit.getWorld(IridiumSkyblock.getInstance().getConfiguration().spawnWorldName);
        if (spawnWorld == null) {
            spawnWorld = Bukkit.getWorlds().get(0);
        }

        EssentialsSpawn essentialsSpawn = (EssentialsSpawn) Bukkit.getPluginManager().getPlugin("EssentialsSpawn");
        Essentials essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        if (essentialsSpawn != null && essentials != null) {
            player.teleport(essentialsSpawn.getSpawn(essentials.getUser(player).getGroup()));
        } else {
            player.teleport(spawnWorld.getSpawnLocation());
        }
    }

}
