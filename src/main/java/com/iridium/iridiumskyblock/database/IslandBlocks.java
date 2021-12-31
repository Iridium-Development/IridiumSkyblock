package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false, uniqueCombo = true)
    private int id;

    @DatabaseField(columnName = "block", canBeNull = false, uniqueCombo = true)
    private @NotNull XMaterial material;

    @DatabaseField(columnName = "amount", canBeNull = false)
    private int amount;

    @DatabaseField(columnName = "extra_amount", canBeNull = false)
    private int extraAmount;

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

    @Override
    public @NotNull String getUniqueKey() {
        return material.name() + "-" + getIslandId();
    }

    public void setAmount(int amount) {
        this.amount = amount;
        setChanged(true);
    }

    public void setExtraAmount(int extraAmount) {
        this.extraAmount = extraAmount;
        setChanged(true);
    }
}
