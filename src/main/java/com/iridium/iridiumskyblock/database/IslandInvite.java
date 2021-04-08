package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
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
import java.util.Optional;
import java.util.UUID;

/**
 * Represents an Invite to an Island.
 */
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_invites")
public final class IslandInvite {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "island_id")
    private int island;

    @DatabaseField(columnName = "user", canBeNull = false)
    private @NotNull UUID user;

    @DatabaseField(columnName = "inviter", canBeNull = false)
    private @NotNull UUID inviter;

    @DatabaseField(columnName = "time", canBeNull = false)
    private long time;

    /**
     * The default constructor.
     *
     * @param island  The Island this invite belongs to
     * @param user    The User who is invited
     * @param inviter The User who invited the invitee
     */
    public IslandInvite(@NotNull Island island, @NotNull User user, @NotNull User inviter) {
        this.island = island.getId();
        this.user = user.getUuid();
        this.inviter = inviter.getUuid();
        this.time = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * Returns the Island this invite belongs to.
     *
     * @return The Island of this invite
     */
    public @NotNull Optional<Island> getIsland() {
        return IridiumSkyblock.getInstance().getIslandManager().getIslandById(island);
    }

    /**
     * Returns the invitee.
     *
     * @return The invited User
     */
    public User getUser() {
        return IridiumSkyblockAPI.getInstance().getUser(Bukkit.getOfflinePlayer(user));
    }

    /**
     * Returns the inviter.
     *
     * @return The inviting User.
     */
    public User getInviter() {
        return IridiumSkyblockAPI.getInstance().getUser(Bukkit.getOfflinePlayer(inviter));
    }

    /**
     * Returns the time this invite was created.
     *
     * @return The time of invitation
     */
    public LocalDateTime getTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

}
