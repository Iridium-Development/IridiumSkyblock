package com.iridium.iridiumskyblock.support;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import org.bukkit.block.CreatureSpawner;

public class WildStackerSupport implements SpawnerSupport {

    public int getSpawnerAmount(CreatureSpawner spawner) {
        return WildStackerAPI.getSpawnersAmount(spawner);
    }

}
