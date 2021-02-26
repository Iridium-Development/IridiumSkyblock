package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @DatabaseField(columnName = "allowed", canBeNull = false)
    private boolean allowed;

    public @NotNull Optional<Island> getIsland() {
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandById(island);
    }

    public IslandPermission(@NotNull Island island, @NotNull String permission, boolean allowed) {
        this.island = island.getId();
        this.permission = permission;
        this.allowed = allowed;
    }

}
