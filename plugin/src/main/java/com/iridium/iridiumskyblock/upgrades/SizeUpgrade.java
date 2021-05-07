package com.iridium.iridiumskyblock.upgrades;

public class SizeUpgrade extends UpgradeData {
    public int size;

    public SizeUpgrade(int money, int crystals, int size) {
        super(money, crystals);
        this.size = size;
    }
}
