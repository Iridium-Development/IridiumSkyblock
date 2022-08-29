package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import org.bukkit.World;

import java.util.Map;

public class Configuration extends com.iridium.iridiumteams.configs.Configuration {

    public Configuration() {
        super("&9", "Island", "IridiumSkyblock");
    }

    public String dateTimeFormat = "EEEE, MMMM dd HH:mm:ss";
    public String defaultDescription = "Default island description :c";
    public String worldName = "IridiumSkyblock";

    public int distance = 151;

    public Map<World.Environment, Boolean> enabledWorlds = new ImmutableMap.Builder<World.Environment, Boolean>()
            .put(World.Environment.NORMAL, true)
            .put(World.Environment.NETHER, true)
            .put(World.Environment.THE_END, true)
            .build();

}
