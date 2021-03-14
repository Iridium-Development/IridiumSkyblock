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

    public @NotNull Optional<Island> getIsland() {
        return IridiumSkyblock.getInstance().getIslandManager().getIslandById(island);
    }

    public IslandBlocks(@NotNull Island island, @NotNull XMaterial material) {
        this.island = island.getId();
        this.material = material;
        this.amount = 0;
    }

}
