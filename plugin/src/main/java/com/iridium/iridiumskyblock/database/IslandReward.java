package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Persist;
import com.iridium.iridiumskyblock.Reward;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Reward for an Island.
 */
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_rewards")
public final class IslandReward extends IslandData {

    private static Persist persist = new Persist(Persist.PersistType.JSON, IridiumSkyblock.getInstance());

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "reward", canBeNull = false)
    private @NotNull String reward;

    /**
     * The default constructor.
     *
     * @param island The Island this reward belongs to
     * @param reward The Island's reward we are saving
     */
    public IslandReward(@NotNull Island island, @NotNull Reward reward) {
        super(island);
        this.reward = persist.toString(reward);
    }

    public Reward getReward() {
        return persist.load(Reward.class, reward);
    }

}
