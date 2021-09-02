package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.j256.ormlite.field.DatabaseField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * A class used for database tables which have an island id assosiated with
 * Used for binary searching
 */
@NoArgsConstructor
@Getter
public class IslandData {

    @DatabaseField(columnName = "island_id", uniqueCombo = true)
    private int islandID;

    public IslandData(Island island) {
        this.islandID = island.getId();
    }

    /**
     * Returns the Island this block belongs to.
     *
     * @return The Island of this block
     */
    public @NotNull Optional<Island> getIsland() {
        return IridiumSkyblock.getInstance().getIslandManager().getIslandById(islandID);
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
