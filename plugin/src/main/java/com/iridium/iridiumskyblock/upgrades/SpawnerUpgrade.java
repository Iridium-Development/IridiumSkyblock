package com.iridium.iridiumskyblock.upgrades;

public class SpawnerUpgrade extends UpgradeData {
    public double modifier;

    public SpawnerUpgrade(int money, int crystals, double modifier) {
        super(money, crystals);
        this.modifier = modifier;
    }
}
