package com.iridium.iridiumskyblock;

import com.cryptomorin.xseries.XSound;
import com.iridium.iridiumcore.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * Represents an Island mission.
 * Serialized in the Configuration files.
 */
@NoArgsConstructor
@Getter
public class Mission {

    private Item item;
    private List<String> missions;
    private MissionType missionType;
    private XSound completeSound;
    private Reward reward;
    private List<String> message;

    /**
     * The default constructor.
     *
     * @param item        The item which represents this mission in the missions GUI
     * @param missions    A list of the conditions for this mission
     * @param missionType The type of this mission (amount of possible completions)
     * @param reward      The reward for this mission
     * @param message     The messages which should be sent after completing this mission
     */
    public Mission(Item item, List<String> missions, MissionType missionType, Reward reward, List<String> message) {
        this.item = item;
        this.missions = missions;
        this.missionType = missionType;
        this.completeSound = XSound.ENTITY_PLAYER_LEVELUP;
        this.reward = reward;
        this.message = message;
    }

    /**
     * Represents the type of missions.
     * Currently the amount of times a mission can be completed.
     */
    public enum MissionType {
        ONCE, DAILY;

        /**
         * Returns the mission type with the provided name, null if there is none.
         * Case insensitive.
         *
         * @param mission The mission name which should be parsed
         * @return The mission type, null if there is none
         */
        public static MissionType getMission(String mission) {
            return Arrays.stream(MissionType.values()).filter(type -> type.name().equalsIgnoreCase(mission)).findFirst().orElse(null);
        }
    }

}
