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
    private Reward reward;
    private List<String> message;

    public Mission(Item item, List<String> missions, MissionType missionType, Reward reward, List<String> message) {
        this.item = item;
        this.missions = missions;
        this.missionType = missionType;
        this.completeSound = XSound.ENTITY_PLAYER_LEVELUP;
        this.reward = reward;
        this.message = message;
    }

    public enum MissionType {
        ONCE, DAILY
    }
}
