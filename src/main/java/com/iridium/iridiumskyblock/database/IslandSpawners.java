package com.iridium.iridiumskyblock.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_spawners")
public final class IslandSpawners extends IslandData {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false, uniqueCombo = true)
    private int id;

    @DatabaseField(columnName = "spawner_type", canBeNull = false, uniqueCombo = true)
    private @NotNull EntityType spawnerType;

    @DatabaseField(columnName = "amount", canBeNull = false)
    @Setter
    private int amount;

    /**
     * The default constructor.
     *
     * @param island      The Island which has this valuable block
     * @param spawnerType The type of the spawner
     */
    public IslandSpawners(@NotNull Island island, @NotNull EntityType spawnerType) {
        super(island);
        this.spawnerType = spawnerType;
    }

    @Override
    public @NotNull String getUniqueKey() {
        return spawnerType.name() + "-" + getIslandId();
    }

}
