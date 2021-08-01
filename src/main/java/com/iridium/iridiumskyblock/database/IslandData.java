package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.j256.ormlite.field.DatabaseField;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * A class used for database tables which have an island id assosiated with
 * Used for binary searching
 */
@NoArgsConstructor
public class IslandData {

    @DatabaseField(columnName = "island_id")
    private int island;

    public IslandData(Island island) {
        this.island = island.getId();
    }

    /**
     * Returns the Island this block belongs to.
     *
     * @return The Island of this block
     */
    public @NotNull Optional<Island> getIsland() {
        return IridiumSkyblock.getInstance().getIslandManager().getIslandById(island);
    }

    /**
     * A unique Key that identifies this record, used for deleting duplicate entries which occured due to a bug
     *
     * @return A unique key
     */
    public @NotNull String getUniqueKey() {
        return "";
    }
}
