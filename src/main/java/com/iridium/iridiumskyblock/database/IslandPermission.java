package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_permissions")
public final class IslandPermission {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "island_id")
    private int island;

    @DatabaseField(columnName = "permission", canBeNull = false)
    private @NotNull String permission;

    @DatabaseField(columnName = "rank", canBeNull = false)
    private @NotNull IslandRank rank;

    @DatabaseField(columnName = "allowed", canBeNull = false)
    @Setter
    private boolean allowed;

    /**
     * The default constructor.
     *
     * @param island     The Island that has this permission
     * @param permission The permission that is represented in the database
     * @param rank       The rank which may or may not have this permission
     * @param allowed    Whether or not this permission has been granted for this Island rank.
     */
    public IslandPermission(@NotNull Island island, @NotNull String permission, @NotNull IslandRank rank, boolean allowed) {
        this.island = island.getId();
        this.permission = permission;
        this.rank = rank;
        this.allowed = allowed;
    }

    /**
     * Returns the Island this permission belongs to.
     *
     * @return The Island of this permission
     */
    public @NotNull Optional<Island> getIsland() {
        return IridiumSkyblock.getInstance().getIslandManager().getIslandById(island);
    }

}
