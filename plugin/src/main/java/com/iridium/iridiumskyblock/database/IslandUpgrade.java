package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents a Reward for an Island.
 */
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_upgrade")
public final class IslandUpgrade {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "island_id")
    private int island;

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
        this.island = island.getId();
        this.level = 1;
        this.upgrade = upgrade;
    }

    /**
     * Returns the Island this invite belongs to.
     *
     * @return The Island of this invite
     */
    public @NotNull Optional<Island> getIsland() {
        return IridiumSkyblock.getInstance().getIslandManager().getIslandById(island);
    }

}
