package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_bans")
public class IslandBan extends IslandData{

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "revoked", canBeNull = false)
    @Setter
    private boolean revoked;

    @DatabaseField(columnName = "restricted", canBeNull = false)
    private UUID restricted;

    @DatabaseField(columnName = "restrictor", canBeNull = false)
    @Setter
    private UUID restrictor;

    @DatabaseField(columnName = "time", canBeNull = false)
    private long time;

    /**
     * The default constructor.
     *
     * @param island   Banned from which island
     * @param restrictor banned by the user
     * @param restricted the banned user
     */
    public IslandBan(Island island,  UUID restrictor, UUID restricted) {
        super(island);
        this.revoked = false;
        this.time = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.restrictor = restrictor;
        this.restricted = restricted;
    }

    /**
     * Returns the trusted user.
     *
     * @return The trusted user
     */
    public User getRestrictedUser() {
        return IridiumSkyblock.getInstance().getUserManager().getUser(Bukkit.getOfflinePlayer(restricted));
    }

    /**
     * Returns the user who trusted the player.
     *
     * @return The User who trusted the other user.
     */
    public User getRestrictorUser() {
        return IridiumSkyblock.getInstance().getUserManager().getUser(Bukkit.getOfflinePlayer(restrictor));
    }

    public LocalDateTime getBanTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(getTime()), ZoneId.systemDefault());
    }
}
