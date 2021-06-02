package com.iridium.iridiumskyblock.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Reward for an Island.
 */
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_upgrade")
public final class IslandUpgrade extends IslandData {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "level", canBeNull = false)
    @Setter
    private int level;

    @DatabaseField(columnName = "upgrade", canBeNull = false)
    private String upgrade;

    /**
     * The default constructor.
     *
     * @param island  The Island this reward belongs to
     * @param upgrade The upgrade name we are saving
     */
    public IslandUpgrade(@NotNull Island island, @NotNull String upgrade) {
        super(island);
        this.level = 1;
        this.upgrade = upgrade;
    }

}
