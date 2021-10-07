package com.iridium.iridiumskyblock.settings;

import com.iridium.iridiumcore.Item;

public class IslandWeather extends IslandSettingImpl {

    public IslandWeather(String defaultValue, boolean enabled, boolean changeable, Item item) {
        super(defaultValue, enabled, changeable, item);
    }

    public enum IslandWeatherTypes {DEFAULT, RAINING, CLEAR}

    @Override
    public IslandWeatherTypes getNext(String type) {
        IslandWeatherTypes mobSpawnType = getByName(type);
        return IslandWeatherTypes.values().length > mobSpawnType.ordinal() + 1 ? IslandWeatherTypes.values()[mobSpawnType.ordinal() + 1] : IslandWeatherTypes.values()[0];
    }

    @Override
    public IslandWeatherTypes getPrevious(String type) {
        IslandWeatherTypes mobSpawnType = getByName(type);
        return mobSpawnType.ordinal() - 1 != -1 ? IslandWeatherTypes.values()[mobSpawnType.ordinal() - 1] : IslandWeatherTypes.values()[3];
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
