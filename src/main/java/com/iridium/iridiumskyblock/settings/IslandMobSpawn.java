package com.iridium.iridiumskyblock.settings;

public enum IslandMobSpawn {

    ALL, ANIMALS, MONSTERS, NOTHING;

    public IslandMobSpawn getNext() {
        return values().length > ordinal() + 1 ? values()[ordinal() + 1] : values()[0];
    }

    public IslandMobSpawn getPrevious() {
        return ordinal() - 1 != -1 ? values()[ordinal() - 1] : values()[3];
    }
}
