package com.iridium.iridiumskyblock.database;

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

    @DatabaseField(columnName = "mission")
    private String mission;

    @DatabaseField(columnName = "progress")
    @Setter
    private int progress;

    public IslandMission(@NotNull Island island, @NotNull String mission) {
        this.island = island.getId();
        this.mission = mission;
        this.progress = 0;
    }
}
