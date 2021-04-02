package com.iridium.iridiumskyblock;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Mission {
    private String name;
    private Item item;
    private String condition;
    private MissionType missionType;

    public Mission(String name, Item item, String condition, MissionType missionType) {
        this.name = name;
        this.item = item;
        this.condition = condition;
        this.missionType = missionType;
    }

    public enum MissionType {
        ONCE, DAILY
    }
}
