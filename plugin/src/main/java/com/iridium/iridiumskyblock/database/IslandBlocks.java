package com.iridium.iridiumskyblock.database;

import com.cryptomorin.xseries.XMaterial;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a valuable block in the database.
 *
 * @see com.iridium.iridiumskyblock.configs.BlockValues
 */
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_blocks")
public final class IslandBlocks extends IslandData {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

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
        super(island);
        this.material = material;
    }

}
