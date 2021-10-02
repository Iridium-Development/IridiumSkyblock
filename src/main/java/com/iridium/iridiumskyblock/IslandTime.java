package com.iridium.iridiumskyblock;

public enum IslandTime {
    UNSET, DAY, NOON, MIDNIGHT, NIGHT;

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
