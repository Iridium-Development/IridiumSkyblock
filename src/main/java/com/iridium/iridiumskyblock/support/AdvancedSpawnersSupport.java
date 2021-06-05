package com.iridium.iridiumskyblock.support;

import gcspawners.ASAPI;
import org.bukkit.block.CreatureSpawner;

public class AdvancedSpawnersSupport implements SpawnerSupport {

    public int getSpawnerAmount(CreatureSpawner spawner) {
        return ASAPI.getSpawnerAmount(spawner.getBlock());
    }

}
