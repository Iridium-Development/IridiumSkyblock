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
    private int exp;
    private List<String> commands;
    private List<String> message;

    public Mission(Item item, List<String> missions, MissionType missionType, int exp, List<String> commands, List<String> message) {
        this.item = item;
        this.missions = missions;
        this.missionType = missionType;
        this.completeSound = XSound.ENTITY_PLAYER_LEVELUP;
        this.exp = exp;
        this.commands = commands;
        this.message = message;
    }

    public enum MissionType {
        ONCE, DAILY
    }
}
