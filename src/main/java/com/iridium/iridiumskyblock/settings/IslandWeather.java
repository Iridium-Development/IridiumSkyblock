package com.iridium.iridiumskyblock.settings;

public enum IslandWeather {

    DEFAULT,
    RAINING,
    CLEAR;

    public IslandWeather getNext() {
        return values().length > ordinal() + 1 ? values()[ordinal() + 1] : values()[0];
    }

    public IslandWeather getPrevious() {
        return ordinal() - 1 != -1 ? values()[ordinal() - 1] : values()[2];
    }

}
