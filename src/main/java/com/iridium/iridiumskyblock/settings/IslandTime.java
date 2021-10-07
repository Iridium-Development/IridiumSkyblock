package com.iridium.iridiumskyblock.settings;

import com.iridium.iridiumcore.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class IslandTime extends IslandSettingImpl {

    @Getter
    @AllArgsConstructor
    public enum IslandTimeTypes {

        DEFAULT(0, true),
        DAY(1000, false),
        NOON(6000, false),
        MIDNIGHT(18000, false),
        NIGHT(13000, false);

        private final int time;
        private final boolean relative;
    }

    public IslandTime(String defaultValue, boolean enabled, boolean changeable, Item item) {
        super(defaultValue, enabled, changeable, item);
    }

    @Override
    public IslandTimeTypes getNext(String type) {
        IslandTimeTypes mobSpawnType = getByName(type);
        return IslandTimeTypes.values().length > mobSpawnType.ordinal() + 1 ? IslandTimeTypes.values()[mobSpawnType.ordinal() + 1] : IslandTimeTypes.values()[0];
    }

    @Override
    public IslandTimeTypes getPrevious(String type) {
        IslandTimeTypes mobSpawnType = getByName(type);
        return mobSpawnType.ordinal() - 1 != -1 ? IslandTimeTypes.values()[mobSpawnType.ordinal() - 1] : IslandTimeTypes.values()[3];
    }

    @Override
    public IslandTimeTypes getByName(String name) {
        try {
            return IslandTimeTypes.valueOf(name);
        } catch (UnsupportedOperationException ignored) {
            return IslandTimeTypes.DEFAULT;
        }
    }

}
