package com.iridium.iridiumskyblock.support;

import com.songoda.ultimatestacker.UltimateStacker;
import org.bukkit.block.CreatureSpawner;

public class UltimateStackerSupport implements SpawnerSupport{

    public int getSpawnerAmount(CreatureSpawner spawner) {
        return UltimateStacker.getInstance().getSpawnerStackManager().getSpawner(spawner.getBlock()).getAmount();
    }

}
