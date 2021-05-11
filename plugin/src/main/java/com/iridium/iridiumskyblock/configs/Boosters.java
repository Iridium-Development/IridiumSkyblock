package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Booster;
import com.iridium.iridiumskyblock.Item;

import java.util.Arrays;

public class Boosters {
    public Booster islandHopperBooster = new Booster(new Item(XMaterial.HOPPER, 10, 1, "&b&lHopper Upgrade", Arrays.asList(
            "&7Are your hoppers too slow? Buy this",
            "&7booster and increase hopper speed x2.",
            "",
            "&b&lInformation:",
            "&b&l * &7Time Remaining: &b%timeremaining_minutes% minutes and %timeremaining_seconds%seconds",
            "&b&l * &7Booster Cost: &b%crystalcost% Crystals and $%vaultcost%",
            "",
            "&b&l[!] &bRight Click to Purchase this Booster."
    )), 15, 10000, 3600);

    public Booster islandSpawnerBooster = new Booster(new Item(XMaterial.SPAWNER, 12, 1, "&b&lSpawner Upgrade", Arrays.asList(
            "&7Spawners too slow? Buy this",
            "&7booster and double your spawner speed.",
            "",
            "&b&lInformation:",
            "&b&l * &7Time Remaining: &b%timeremaining_minutes% minutes and %timeremaining_seconds%seconds",
            "&b&l * &7Booster Cost: &b%crystalcost% Crystals and $%vaultcost%",
            "",
            "&b&l[!] &bRight Click to Purchase this Booster."
    )), 15, 10000, 3600);

    public Booster islandExperienceBooster = new Booster(new Item(XMaterial.EXPERIENCE_BOTTLE, 14, 1, "&b&lExperience Upgrade", Arrays.asList(
            "&7Gaining Experience too slow? Buy this",
            "&7booster and double experience drops.",
            "",
            "&b&lInformation:",
            "&b&l * &7Time Remaining: &b%timeremaining_minutes% minutes and %timeremaining_seconds%seconds",
            "&b&l * &7Booster Cost: &b%crystalcost% Crystals and $%vaultcost%",
            "",
            "&b&l[!] &bRight Click to Purchase this Booster."
    )), 15, 10000, 3600);

    public Booster islandFlightBooster = new Booster(new Item(XMaterial.FEATHER, 16, 1, "&b&lFlight Upgrade", Arrays.asList(
            "&7Keep falling off your island? Buy this",
            "&7booster and Gain access to /is fly.",
            "",
            "&b&lInformation:",
            "&b&l * &7Time Remaining: &b%timeremaining_minutes% minutes and %timeremaining_seconds%seconds",
            "&b&l * &7Booster Cost: &b%crystalcost% Crystals and $%vaultcost%",
            "",
            "&b&l[!] &bRight Click to Purchase this Booster."
    )), 15, 10000, 3600);

}
