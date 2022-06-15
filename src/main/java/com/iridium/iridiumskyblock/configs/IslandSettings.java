package com.iridium.iridiumskyblock.configs;

import com.iridium.iridiumskyblock.support.material.Item;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;
import com.iridium.iridiumskyblock.support.material.IridiumMaterial;
import com.iridium.iridiumskyblock.IslandTime;
import com.iridium.iridiumskyblock.IslandWeatherType;
import com.iridium.iridiumskyblock.Setting;

import java.util.Arrays;

/**
 * The Island permission configuration used by IridiumSkyblock (permissions.yml).
 * Is deserialized automatically on plugin startup and reload.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IslandSettings {

    public Setting mobSpawn = new Setting(true, new Item(IridiumMaterial.ZOMBIE_HEAD, 10, 1, "&b&lMob Spawn", Arrays.asList("&7Allow mobs to spawn on your Island.", "", "&b&lValue", "&7%value%")), "true");
    public Setting leafDecay = new Setting(true, new Item(IridiumMaterial.OAK_LEAVES, 11, 1, "&b&lLeaf Decay", Arrays.asList("&7Allow leaves to decay on your Island.", "", "&b&lValue", "&7%value%")), "true");
    public Setting weather = new Setting(true, new Item(IridiumMaterial.SNOWBALL, 12, 1, "&b&lIsland Weather", Arrays.asList("&7Change the weather of your Island.", "", "&b&lValue", "&7%value%")), IslandWeatherType.DEFAULT.name());
    public Setting time = new Setting(true, new Item(IridiumMaterial.CLOCK, 13, 1, "&b&lIsland Time", Arrays.asList("&7Change your Island time.", "", "&b&lValue", "&7%value%")), IslandTime.DEFAULT.name());
    public Setting endermanGrief = new Setting(true, new Item(IridiumMaterial.ENDER_PEARL, 14, 1, "&b&lEnderman Grief", Arrays.asList("&7Allow enderman to grief your Island.", "", "&b&lValue", "&7%value%")), "true");
    public Setting liquidFlow = new Setting(true, new Item(IridiumMaterial.WATER_BUCKET, 15, 1, "&b&lLiquid Flow", Arrays.asList("&7Allow Water and Lava to flow on your Island.", "", "&b&lValue", "&7%value%")), "true");
    public Setting tntDamage = new Setting(true, new Item(IridiumMaterial.TNT, 16, 1, "&b&lTnT Damage", Arrays.asList("&7Allow TnT to explode on your Island.", "", "&b&lValue", "&7%value%")), "true");
    public Setting fireSpread = new Setting(true, new Item(IridiumMaterial.FLINT_AND_STEEL, 19, 1, "&b&lFire Spread", Arrays.asList("&7Allow fire to spread on your Island.", "", "&b&lValue", "&7%value%")), "true");

}
