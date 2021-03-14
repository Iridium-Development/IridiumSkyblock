package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * The main configuration of IridiumSkyblock.
 * Is deserialized automatically on plugin startup and reload.
 */
public class Configuration {

    public String prefix = "<GRADIENT:09C6F9>&lIridiumSkyblock</GRADIENT:045DE9> &8»";
    public String worldName = "IridiumSkyblock";
    public String netherWorldName = "IridiumSkyblock_nether";
    public String islandCreateTitle = "&b&lIsland Created";
    public String islandCreateSubTitle = "&7IridiumSkyblock by Peaches_MLG";
    public String dateTimeFormat = "EEEE, MMMM dd HH:mm:ss";

    public boolean defaultIslandPublic = true;

    public int distance = 151;
    public int schematicPastingDelay = 1;
    public int islandRecalculateInterval = 10;

    public Map<Integer, Integer> islandTopSlots = ImmutableMap.<Integer, Integer>builder()
            .put(1, 4)
            .put(2, 12)
            .put(3, 14)
            .put(4, 19)
            .put(5, 20)
            .put(6, 21)
            .put(7, 22)
            .put(8, 23)
            .put(9, 24)
            .put(10, 25)
            .build();

}
