package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_bans")
public class IslandBan extends IslandData {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false, uniqueCombo = true)
    private int id;

    @DatabaseField(columnName = "banner", canBeNull = false)
    private UUID banner;

    @DatabaseField(columnName = "bannedUser", canBeNull = false, uniqueCombo = true)
    private UUID bannedUser;

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
        this.bannedUser = bannedUser.getUuid();
        this.banner = banner.getUuid();
    }

    public @Nullable User getBannedUser() {
        return IridiumSkyblock.getInstance().getUserManager().getUserByUUID(bannedUser).orElse(null);
    }

    public @Nullable User getBanner() {
        return IridiumSkyblock.getInstance().getUserManager().getUserByUUID(banner).orElse(null);
    }

    public LocalDateTime getBanTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(getTime()), ZoneId.systemDefault());
    }
}
