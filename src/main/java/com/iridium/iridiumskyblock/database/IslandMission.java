package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.Mission;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_mission")
public class IslandMission {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "island_id")
    private int island;

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

    public IslandMission(@NotNull Island island, @NotNull Mission mission, @NotNull String missionKey, int missionIndex) {
        this.island = island.getId();
        this.missionName = missionKey;
        this.type = mission.getMissionType();
        this.missionIndex = missionIndex;
        this.progress = 0;
    }
}
