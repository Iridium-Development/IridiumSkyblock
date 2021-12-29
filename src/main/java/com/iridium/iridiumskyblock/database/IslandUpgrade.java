package com.iridium.iridiumskyblock.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Reward for an Island.
 */
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_upgrade")
public final class IslandUpgrade extends IslandData {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false, uniqueCombo = true)
    private int id;

    @DatabaseField(columnName = "level", canBeNull = false)
    private int level;

    @DatabaseField(columnName = "upgrade", canBeNull = false, uniqueCombo = true)
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

    @Override
    public @NotNull String getUniqueKey() {
        return upgrade + "-" + getIslandId();
    }

    public void setLevel(int level) {
        this.level = level;
        setChanged(true);
    }
}
