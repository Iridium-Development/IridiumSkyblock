package com.iridium.iridiumskyblock;

import com.cryptomorin.xseries.XSound;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class Mission {
    private Item item;
    private List<String> missions;
    private MissionType missionType;
    private XSound completeSound;

    public Mission(Item item, List<String> missions, MissionType missionType) {
        this.item = item;
        this.missions = missions;
        this.missionType = missionType;
        this.completeSound = XSound.ENTITY_PLAYER_LEVELUP;
    }

    public enum MissionType {
        ONCE, DAILY
    }
}
