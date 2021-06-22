package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a User of IridiumSkyblock.
 */
@Getter
@Setter
@NoArgsConstructor
@DatabaseTable(tableName = "users")
public final class User {

    @DatabaseField(columnName = "uuid", canBeNull = false, id = true)
    private @NotNull UUID uuid;

    @DatabaseField(columnName = "name", canBeNull = false)
    private @NotNull String name;

    @DatabaseField(columnName = "island_id")
    private @Nullable Integer island;

    @DatabaseField(columnName = "join_time")
    private long joinTime;

    @DatabaseField(columnName = "island_rank")
    private @NotNull IslandRank islandRank;

    private boolean bypass = false;

    private boolean flying = false;

    private boolean islandChat = false;

    private Location schematicPos1;
    private Location schematicPos2;

    private BukkitTask teleportingTask;

    /**
     * The default constructor.
     *
     * @param uuid The UUID of the {@link org.bukkit.entity.Player}
     * @param name The name of the Player
     */
    public User(final @NotNull UUID uuid, final @NotNull String name) {
        this.uuid = uuid;
        this.name = name;
        this.joinTime = 0L;
        this.islandRank = IslandRank.VISITOR;
    }

    /**
     * Constructor for binary searching
     *
     * @param island The user's island
     */
    public User(Island island) {
        this.island = island.getId();
    }


    /**
     * Returns the Island of this user.
     *
     * @return The user's Island
     */
    public @NotNull Optional<Island> getIsland() {
        if (island == null) return Optional.empty();
        return IridiumSkyblock.getInstance().getIslandManager().getIslandById(island);
    }

    /**
     * Alters the Island of this user.
     * Use null as a parameter to remove his association to the Island.
     *
     * @param island The new Island of this user, can be null
     */
    public void setIsland(@Nullable Island island) {
        this.island = island == null ? null : island.getId();
        setJoinTime(LocalDateTime.now());
        if (island != null) {
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager().getEntries(island).stream().filter(islandTrusted ->
                    islandTrusted.getUser().equals(this)
            ).findFirst().ifPresent(trusted ->
                    IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager().delete(trusted)
            );
        }
        IridiumSkyblock.getInstance().getDatabaseManager().getUserTableManager().resortIsland(this);
    }

    /**
     * Gets the time this user has first been created.
     *
     * @return The internal creation time
     */
    public LocalDateTime getJoinTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(joinTime), ZoneId.systemDefault());
    }

    /**
     * Alters this users creation time.
     *
     * @param joinTime The internal time this user has been created
     */
    public void setJoinTime(LocalDateTime joinTime) {
        this.joinTime = ZonedDateTime.of(joinTime, ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

}
