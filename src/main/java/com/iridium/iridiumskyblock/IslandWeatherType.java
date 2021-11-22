package com.iridium.iridiumskyblock;

public enum IslandWeatherType {

    DEFAULT,
    RAINING,
    CLEAR;

    public static String getNext(String current) {
        int nextOrdinal = valueOf(current.toUpperCase()).ordinal() + 1;
        int availableValues = values().length;
        if (nextOrdinal >= availableValues) return values()[0].name();
        return values()[nextOrdinal].name();
    }

    public static String getPrevious(String current) {
        int previousOrdinal = valueOf(current.toUpperCase()).ordinal() - 1;
        if (previousOrdinal < 0) return values()[values().length-1].name();
        return values()[previousOrdinal].name();
    }

}
