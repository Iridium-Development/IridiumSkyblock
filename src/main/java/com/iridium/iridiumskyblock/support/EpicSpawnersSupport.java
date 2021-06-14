package com.iridium.iridiumskyblock.support;

import com.songoda.epicspawners.EpicSpawners;
import org.bukkit.block.CreatureSpawner;

public class EpicSpawnersSupport implements SpawnerStackerSupport {

    public int getSpawnerAmount(CreatureSpawner spawner) {
        return EpicSpawners.getInstance().getSpawnerManager().getSpawnerFromWorld(spawner.getLocation()).getStackSize();
    }

}
