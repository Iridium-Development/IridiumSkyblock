package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Represents people who are trusted by the island.
 */
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_trusted")
public final class IslandTrusted extends IslandData {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "user", canBeNull = false)
    private @NotNull UUID user;

    @DatabaseField(columnName = "truster", canBeNull = false)
    private @NotNull UUID truster;

    @DatabaseField(columnName = "time", canBeNull = false)
    private long time;

    /**
     * The default constructor.
     *
     * @param island  The Island this invite belongs to
     * @param user    The User who is invited
     * @param truster The User who invited the invitee
     */
    public IslandTrusted(@NotNull Island island, @NotNull User user, @NotNull User truster) {
        super(island);
        this.user = user.getUuid();
        this.truster = truster.getUuid();
        this.time = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * Returns the trusted user.
     *
     * @return The trusted user
     */
    public User getUser() {
        return IridiumSkyblockAPI.getInstance().getUser(Bukkit.getOfflinePlayer(user));
    }

    /**
     * Returns the user who trusted the player.
     *
     * @return The User who trusted the other user.
     */
    public User getTruster() {
        return IridiumSkyblockAPI.getInstance().getUser(Bukkit.getOfflinePlayer(truster));
    }

    /**
     * Returns the time this trust was created.
     *
     * @return The time the trust was created
     */
    public LocalDateTime getTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

}
