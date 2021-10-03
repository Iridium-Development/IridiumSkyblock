package com.iridium.iridiumskyblock;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IslandTime {
    DEFAULT(0, true), DAY(1000, false), NOON(6000, false), MIDNIGHT(18000, false), NIGHT(13000, false);

    private final int time;
    private final boolean relative;

    public static String getNext(String current) {
        boolean isCurrent = false;
        for (IslandTime islandTime : values()) {
            if (isCurrent) return islandTime.name();
            if (islandTime.name().equalsIgnoreCase(current)) isCurrent = true;
        }
        return values()[0].name();
    }

    public static String getPrevious(String current) {
        for (IslandTime islandTime : values()) {
            if (getNext(islandTime.name()).equalsIgnoreCase(current)) {
                return islandTime.name();
            }
        }
        return values()[0].name();
    }
}
