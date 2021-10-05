package com.iridium.iridiumskyblock.configs;

import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.settings.IslandMobSpawn;
import com.iridium.iridiumskyblock.settings.IslandTime;
import com.iridium.iridiumskyblock.settings.IslandWeather;
import com.iridium.iridiumskyblock.settings.IslandSettingConfig;

import java.util.Arrays;

/**
 * The Island setting configuration used by IridiumSkyblock (settings.yml).
 * Is deserialized automatically on plugin startup and reload.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IslandSettings {

    public IslandSettingConfig mobSpawn = new IslandSettingConfig(new Item(XMaterial.ZOMBIE_HEAD, 10, 1, "&b&lMob Spawn", Arrays.asList("&7Change which mobs can spawn on your Island.", "", "&b&lValue", "&7%value%")), IslandMobSpawn.ALL.name());
    public IslandSettingConfig leafDecay = new IslandSettingConfig(new Item(XMaterial.OAK_LEAVES, 11, 1, "&b&lLeaf Decay", Arrays.asList("&7Allow leaves to decay on your Island.", "", "&b&lValue", "&7%value%")), "ALLOWED");
    public IslandSettingConfig weather = new IslandSettingConfig(new Item(XMaterial.SNOWBALL, 12, 1, "&b&lIsland Weather", Arrays.asList("&7Change the weather of your Island.", "", "&b&lValue", "&7%value%")), IslandWeather.DEFAULT.name());
    public IslandSettingConfig time = new IslandSettingConfig(new Item(XMaterial.CLOCK, 13, 1, "&b&lIsland Time", Arrays.asList("&7Change your Island time.", "", "&b&lValue", "&7%value%")), IslandTime.DEFAULT.name());
    public IslandSettingConfig endermanGrief = new IslandSettingConfig(new Item(XMaterial.ENDER_PEARL, 14, 1, "&b&lEnderman Grief", Arrays.asList("&7Allow enderman to grief your Island.", "", "&b&lValue", "&7%value%")), "ALLOWED");
    public IslandSettingConfig liquidFlow = new IslandSettingConfig(new Item(XMaterial.WATER_BUCKET, 15, 1, "&b&lLiquid Flow", Arrays.asList("&7Allow Water and Lava to flow on your Island.", "", "&b&lValue", "&7%value%")), "ALLOWED");
    public IslandSettingConfig tntDamage = new IslandSettingConfig(new Item(XMaterial.TNT, 16, 1, "&b&lTnT Damage", Arrays.asList("&7Allow TnT to explode on your Island.", "", "&b&lValue", "&7%value%")), "ALLOWED");
    public IslandSettingConfig fireSpread = new IslandSettingConfig(new Item(XMaterial.FLINT_AND_STEEL, 19, 1, "&b&lFire Spread", Arrays.asList("&7Allow fire to spread on your Island.", "", "&b&lValue", "&7%value%")), "ALLOWED");

}
