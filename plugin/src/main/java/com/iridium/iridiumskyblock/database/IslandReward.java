package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Persist;
import com.iridium.iridiumskyblock.Reward;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Represents a Reward for an Island.
 */
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_rewards")
public final class IslandReward {

    private static Persist persist = new Persist(Persist.PersistType.JSON, IridiumSkyblock.getInstance());

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "island_id")
    private int island;

    @DatabaseField(columnName = "reward", canBeNull = false)
    private @NotNull String reward;

    /**
     * The default constructor.
     *
     * @param island The Island this reward belongs to
     * @param reward The Island's reward we are saving
     */
    public IslandReward(@NotNull Island island, @NotNull Reward reward) {
        this.island = island.getId();
        this.reward = persist.toString(reward);
    }

    /**
     * Returns the Island this invite belongs to.
     *
     * @return The Island of this invite
     */
    public @NotNull Optional<Island> getIsland() {
        return IridiumSkyblock.getInstance().getIslandManager().getIslandById(island);
    }

    public Reward getReward() {
        return persist.load(Reward.class, reward);
    }

}
