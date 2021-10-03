package com.iridium.iridiumskyblock;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IslandTime {

    DEFAULT(0, true),
    DAY(1000, false),
    NOON(6000, false),
    MIDNIGHT(18000, false),
    NIGHT(13000, false);

    private final int time;
    private final boolean relative;

    public static String getNext(String current) {
        int nextOrdinal = valueOf(current.toUpperCase()).ordinal() + 1;
        int availableValues = values().length;
        if (nextOrdinal >= availableValues) return values()[0].name();
        return values()[nextOrdinal].name();
    }

    public static String getPrevious(String current) {
        int previousOrdinal = valueOf(current.toUpperCase()).ordinal() - 1;
        if (previousOrdinal < 0) return values()[0].name();
        return values()[previousOrdinal].name();
    }

}
