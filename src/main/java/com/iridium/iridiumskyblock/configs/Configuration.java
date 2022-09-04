package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import org.bukkit.World;

import java.util.Arrays;
import java.util.Map;

public class Configuration extends com.iridium.iridiumteams.configs.Configuration {


    public Configuration() {
        super("&9", "Island", "IridiumSkyblock");
    }

    public String defaultDescription = "Default island description :c";
    public String worldName = "IridiumSkyblock";
    public String spawnWorldName = "world";

    public int distance = 151;
    public Item islandCrystal = new Item(XMaterial.NETHER_STAR, 1, "&9*** &9&lIsland Crystal &9***", Arrays.asList(
            "",
            "&9%amount% Island Crystals",
            "&9&l[!] &9Right-Click to Redeem"
    ));

    public Map<World.Environment, Boolean> enabledWorlds = new ImmutableMap.Builder<World.Environment, Boolean>()
            .put(World.Environment.NORMAL, true)
            .put(World.Environment.NETHER, true)
            .put(World.Environment.THE_END, true)
            .build();

}
