package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
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
 * Represents an Invite to an Island.
 */
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_invites")
public final class IslandInvite extends IslandData{

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false, unique = true, uniqueCombo = true)
    private int id;

    @DatabaseField(columnName = "user", canBeNull = false, unique = true, uniqueCombo = true)
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
        super(island);
        this.user = user.getUuid();
        this.inviter = inviter.getUuid();
        this.time = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * Returns the invitee.
     *
     * @return The invited User
     */
    public User getUser() {
        return IridiumSkyblock.getInstance().getUserManager().getUser(Bukkit.getOfflinePlayer(user));
    }

    /**
     * Returns the inviter.
     *
     * @return The inviting User.
     */
    public User getInviter() {
        return IridiumSkyblock.getInstance().getUserManager().getUser(Bukkit.getOfflinePlayer(inviter));
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
