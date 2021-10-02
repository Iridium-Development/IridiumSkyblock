package com.iridium.iridiumskyblock;

public enum IslandWeatherType {
    DEFAULT, RAINING, CLEAR, THUNDERSTORM;

    public static String getNext(String current) {
        boolean isCurrent = false;
        for (IslandWeatherType islandWeatherType : values()) {
            if (isCurrent) return islandWeatherType.name();
            if (islandWeatherType.name().equalsIgnoreCase(current)) isCurrent = true;
        }
        return values()[0].name();
    }

    public static String getPrevious(String current) {
        for (IslandWeatherType islandWeatherType : values()) {
            if (getNext(islandWeatherType.name()).equalsIgnoreCase(current)) {
                return islandWeatherType.name();
            }
        }
        return values()[0].name();
    }
}
