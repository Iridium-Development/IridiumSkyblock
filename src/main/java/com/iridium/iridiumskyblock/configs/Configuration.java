package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Item;
import org.bukkit.World;

import java.util.Map;

public class Configuration extends com.iridium.iridiumteams.configs.Configuration {

    public Item islandCrystal;

    public Configuration() {
        super("&9", "Island", "IridiumSkyblock");
    }
    public String defaultDescription = "Default island description :c";
    public String worldName = "IridiumSkyblock";

    public int distance = 151;

    public Map<World.Environment, Boolean> enabledWorlds = new ImmutableMap.Builder<World.Environment, Boolean>()
            .put(World.Environment.NORMAL, true)
            .put(World.Environment.NETHER, true)
            .put(World.Environment.THE_END, true)
            .build();

}
