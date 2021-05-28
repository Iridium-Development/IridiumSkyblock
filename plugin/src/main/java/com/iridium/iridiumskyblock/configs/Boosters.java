package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Booster;
import com.iridium.iridiumskyblock.Item;

import java.util.Arrays;

public class Boosters {

    public Booster farmingBooster = new Booster(new Item(XMaterial.WHEAT, 10, 1, "&b&lFarming Booster", Arrays.asList(
            "&7Crops growing too slow? Buy this",
            "&7booster and double crop growth.",
            "",
            "&b&lInformation:",
            "&b&l * &7Time Remaining: &b%timeremaining_minutes% minutes and %timeremaining_seconds% seconds",
            "&b&l * &7Booster Cost: &b%crystalcost% Crystals and $%vaultcost%",
            "",
            "&b&l[!] &bRight Click to Purchase this Booster."
    )), 15, 10000, 3600, true);

    public Booster spawnerBooster = new Booster(new Item(XMaterial.SPAWNER, 12, 1, "&b&lSpawner Booster", Arrays.asList(
            "&7Spawners too slow? Buy this",
            "&7booster and double your spawner speed.",
            "",
            "&b&lInformation:",
            "&b&l * &7Time Remaining: &b%timeremaining_minutes% minutes and %timeremaining_seconds% seconds",
            "&b&l * &7Booster Cost: &b%crystalcost% Crystals and $%vaultcost%",
            "",
            "&b&l[!] &bRight Click to Purchase this Booster."
    )), 15, 10000, 3600, true);

    public Booster experienceBooster = new Booster(new Item(XMaterial.EXPERIENCE_BOTTLE, 14, 1, "&b&lExperience Booster", Arrays.asList(
            "&7Gaining Experience too slow? Buy this",
            "&7booster and double experience drops.",
            "",
            "&b&lInformation:",
            "&b&l * &7Time Remaining: &b%timeremaining_minutes% minutes and %timeremaining_seconds% seconds",
            "&b&l * &7Booster Cost: &b%crystalcost% Crystals and $%vaultcost%",
            "",
            "&b&l[!] &bRight Click to Purchase this Booster."
    )), 15, 10000, 3600, true);

    public Booster flightBooster = new Booster(new Item(XMaterial.FEATHER, 16, 1, "&b&lFlight Booster", Arrays.asList(
            "&7Keep falling off your island? Buy this",
            "&7booster and Gain access to /is fly.",
            "",
            "&b&lInformation:",
            "&b&l * &7Time Remaining: &b%timeremaining_minutes% minutes and %timeremaining_seconds% seconds",
            "&b&l * &7Booster Cost: &b%crystalcost% Crystals and $%vaultcost%",
            "",
            "&b&l[!] &bRight Click to Purchase this Booster."
    )), 15, 10000, 3600, true);

}
