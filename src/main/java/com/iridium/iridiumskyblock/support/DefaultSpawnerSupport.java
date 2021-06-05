package com.iridium.iridiumskyblock.support;

import org.bukkit.block.CreatureSpawner;

public class DefaultSpawnerSupport implements SpawnerSupport {
    @Override
    public int getSpawnerAmount(CreatureSpawner spawner) {
        return 1;
    }
}
