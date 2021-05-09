package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.Mission;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a mission of an Island.
 */
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_mission")
public class IslandMission extends IslandData {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "mission_name")
    private String missionName;

    @DatabaseField(columnName = "mission_index")
    private int missionIndex;

    @DatabaseField(columnName = "progress")
    @Setter
    private int progress;

    @DatabaseField(columnName = "type")
    @Setter
    private Mission.MissionType type;

    /**
     * The default constructor.
     *
     * @param island       The Island that has this mission
     * @param mission      The mission that is represented in the database
     * @param missionKey   The key of the mission
     * @param missionIndex The index of the mission
     */
    public IslandMission(@NotNull Island island, @NotNull Mission mission, @NotNull String missionKey, int missionIndex) {
        super(island);
        this.missionName = missionKey;
        this.type = mission.getMissionType();
        this.missionIndex = missionIndex;
    }

}
