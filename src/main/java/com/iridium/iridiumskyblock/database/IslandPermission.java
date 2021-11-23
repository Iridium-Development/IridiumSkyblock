package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IslandRank;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_permissions")
public final class IslandPermission extends IslandData {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false, uniqueCombo = true)
    private int id;

    @DatabaseField(columnName = "permission", canBeNull = false, uniqueCombo = true)
    private @NotNull String permission;

    @DatabaseField(columnName = "rank", canBeNull = false)
    private @NotNull IslandRank rank;

    @DatabaseField(columnName = "allowed", canBeNull = false)
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
        super(island);
        this.permission = permission;
        this.rank = rank;
        this.allowed = allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
        setChanged(true);
    }
}
