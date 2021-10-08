package com.iridium.iridiumskyblock.settings;

import com.iridium.iridiumcore.Item;

public class IslandWeather extends IslandSettingImpl {

    public IslandWeather(Item item, String defaultValue, boolean enabled, boolean changeable) {
        super(item, defaultValue, enabled, changeable);
    }

    public enum IslandWeatherTypes {DEFAULT, RAINING, CLEAR}

    @Override
    public IslandWeatherTypes getNext(String type) {
        IslandWeatherTypes weatherType = getByName(type);
        return IslandWeatherTypes.values().length > weatherType.ordinal() + 1 ? IslandWeatherTypes.values()[weatherType.ordinal() + 1] : IslandWeatherTypes.values()[0];
    }

    @Override
    public IslandWeatherTypes getPrevious(String type) {
        IslandWeatherTypes weatherType = getByName(type);
        return weatherType.ordinal() - 1 != -1 ? IslandWeatherTypes.values()[weatherType.ordinal() - 1] : IslandWeatherTypes.values()[3];
    }

    @Override
    public IslandWeatherTypes getByName(String name) {
        try {
            return IslandWeatherTypes.valueOf(name);
        } catch (UnsupportedOperationException ignored) {
            return IslandWeatherTypes.DEFAULT;
        }
    }
}
