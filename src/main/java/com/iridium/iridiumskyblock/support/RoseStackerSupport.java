package com.iridium.iridiumskyblock.support;

import dev.rosewood.rosestacker.api.RoseStackerAPI;
import dev.rosewood.rosestacker.stack.StackedSpawner;
import org.bukkit.block.CreatureSpawner;

public class RoseStackerSupport implements SpawnerSupport {

    public int getSpawnerAmount(CreatureSpawner spawner) {
        StackedSpawner stackedSpawner = RoseStackerAPI.getInstance().getStackedSpawner(spawner.getBlock());
        if (stackedSpawner == null) return 1;
        return stackedSpawner.getStackSize();
    }

}
