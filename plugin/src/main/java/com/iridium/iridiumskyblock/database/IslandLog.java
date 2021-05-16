package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.LogAction;
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

@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_logs")
public final class IslandLog extends IslandData {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "time", canBeNull = false)
    private long time;

    @DatabaseField(columnName = "user", canBeNull = false)
    private @NotNull UUID user;

    @DatabaseField(columnName = "target")
    private UUID target;

    @DatabaseField(columnName = "crystals", canBeNull = false)
    private int crystals;

    @DatabaseField(columnName = "money", canBeNull = false)
    private double money;

    @DatabaseField(columnName = "experience", canBeNull = false)
    private int experience;

    @DatabaseField(columnName = "data")
    private String data;

    @DatabaseField(columnName = "action", canBeNull = false)
    private @NotNull LogAction logAction;

    /**
     * The default constructor.
     *
     * @param island The Island which has this valuable block
     * @param user   The user performing this action
     * @param target The user affected by this action
     */
    public IslandLog(@NotNull Island island, @NotNull LogAction logAction, @NotNull User user, User target, int crystals, double money, int experience, String data) {
        super(island);
        this.time = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.user = user.getUuid();
        if (target != null) this.target = target.getUuid();
        this.crystals = crystals;
        this.money = money;
        this.experience = experience;
        this.data = data;
        this.logAction = logAction;
    }

    /**
     * Returns the user performing this action.
     *
     * @return The user performing this action
     */
    public User getUser() {
        return IridiumSkyblock.getInstance().getUserManager().getUser(Bukkit.getOfflinePlayer(user));
    }

    /**
     * Returns the target of this action.
     *
     * @return The user who is targeted by this action
     */
    public User getTarget() {
        if (target == null) return null;
        return IridiumSkyblock.getInstance().getUserManager().getUser(Bukkit.getOfflinePlayer(target));
    }

    /**
     * Returns the time of this log.
     *
     * @return The time of this log
     */
    public LocalDateTime getTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }
}
