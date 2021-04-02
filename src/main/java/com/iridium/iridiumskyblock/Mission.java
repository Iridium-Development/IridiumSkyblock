package com.iridium.iridiumskyblock;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class Mission {
    private Item item;
    private List<String> missions;
    private MissionType missionType;

    public Mission(Item item, List<String> missions, MissionType missionType) {
        this.item = item;
        this.missions = missions;
        this.missionType = missionType;
    }

    public enum MissionType {
        ONCE, DAILY
    }
}
