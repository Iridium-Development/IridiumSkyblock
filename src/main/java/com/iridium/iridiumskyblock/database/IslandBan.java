package com.iridium.iridiumskyblock.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_bans")
public class IslandBan extends IslandData {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "restricted", canBeNull = false)
    private User banner;

    @DatabaseField(columnName = "restrictor", canBeNull = false)
    private User bannedUser;

    @DatabaseField(columnName = "time", canBeNull = false)
    private long time;

    /**
     * The default constructor.
     *
     * @param island     Banned from which island
     * @param bannedUser the banned user
     * @param banner     the user who banned the player
     */
    public IslandBan(Island island, User banner, User bannedUser) {
        super(island);
        this.time = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.bannedUser = bannedUser;
        this.banner = banner;
    }

    public LocalDateTime getBanTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(getTime()), ZoneId.systemDefault());
    }
}
