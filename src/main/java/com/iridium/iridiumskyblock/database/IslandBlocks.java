package com.iridium.iridiumskyblock.database;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents a valuable block in the database.
 *
 * @see com.iridium.iridiumskyblock.configs.BlockValues
 */
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_blocks")
public final class IslandBlocks {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "island_id")
    private int island;

    @DatabaseField(columnName = "block", canBeNull = false)
    private @NotNull XMaterial material;

    @DatabaseField(columnName = "amount", canBeNull = false)
    @Setter
    private int amount;

    /**
     * The default constructor.
     *
     * @param island   The Island which has this valuable block
     * @param material The material of this valuable block
     */
    public IslandBlocks(@NotNull Island island, @NotNull XMaterial material) {
        this.island = island.getId();
        this.material = material;
    }

    /**
     * Returns the Island this block belongs to.
     *
     * @return The Island of this block
     */
    public @NotNull Optional<Island> getIsland() {
        return IridiumSkyblock.getInstance().getIslandManager().getIslandById(island);
    }

}
