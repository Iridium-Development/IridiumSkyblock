package com.iridium.iridiumskyblock.settings;

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

    public IslandTime getNext() {
        return values().length > ordinal() + 1 ? values()[ordinal() + 1] : values()[0];
    }

    public IslandTime getPrevious() {
        return ordinal() - 1 != -1 ? values()[ordinal() - 1] : values()[4];
    }

}
