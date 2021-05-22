package com.iridium.iridiumskyblock.configs;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Booster;
import com.iridium.iridiumskyblock.Item;

import java.util.Arrays;

public class Boosters {
    public Booster islandFarmingBooster = new Booster(new Item(XMaterial.WHEAT, 10, 1, "&9&lFarming Booster", Arrays.asList(
            "&7Crops growing too slow? Buy this",
            "&7booster and double crop growth.",
            "",
            "&9&lInformation:",
            "&9&l * &7Time Remaining: &9%timeremaining_minutes% minutes and %timeremaining_seconds% seconds",
            "&9&l * &7Booster Cost: &9%crystalcost% Crystals and $%vaultcost%",
            "",
            "&9&l[!] &9Right Click to Purchase this Booster."
    )), 15, 10000, 3600, true);

    public Booster islandSpawnerBooster = new Booster(new Item(XMaterial.SPAWNER, 12, 1, "&9&lSpawner Booster", Arrays.asList(
            "&7Spawners too slow? Buy this",
            "&7booster and double your spawner speed.",
            "",
            "&9&lInformation:",
            "&9&l * &7Time Remaining: &9%timeremaining_minutes% minutes and %timeremaining_seconds% seconds",
            "&9&l * &7Booster Cost: &9%crystalcost% Crystals and $%vaultcost%",
            "",
            "&9&l[!] &9Right Click to Purchase this Booster."
    )), 15, 10000, 3600, true);

    public Booster islandExperienceBooster = new Booster(new Item(XMaterial.EXPERIENCE_BOTTLE, 14, 1, "&9&lExperience Booster", Arrays.asList(
            "&7Gaining Experience too slow? Buy this",
            "&7booster and double experience drops.",
            "",
            "&9&lInformation:",
            "&9&l * &7Time Remaining: &9%timeremaining_minutes% minutes and %timeremaining_seconds% seconds",
            "&9&l * &7Booster Cost: &9%crystalcost% Crystals and $%vaultcost%",
            "",
            "&9&l[!] &9Right Click to Purchase this Booster."
    )), 15, 10000, 3600, true);

    public Booster islandFlightBooster = new Booster(new Item(XMaterial.FEATHER, 16, 1, "&9&lFlight Booster", Arrays.asList(
            "&7Keep falling off your island? Buy this",
            "&7booster and Gain access to /is fly.",
            "",
            "&9&lInformation:",
            "&9&l * &7Time Remaining: &9%timeremaining_minutes% minutes and %timeremaining_seconds% seconds",
            "&9&l * &7Booster Cost: &9%crystalcost% Crystals and $%vaultcost%",
            "",
            "&9&l[!] &9Right Click to Purchase this Booster."
    )), 15, 10000, 3600, true);

}
