package com.iridium.iridiumskyblock.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Represents an islands booster
 */
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_booster")
public final class IslandBooster extends IslandData {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false, uniqueCombo = true)
    private int id;

    @DatabaseField(columnName = "booster", canBeNull = false, uniqueCombo = true)
    private String booster;

    @DatabaseField(columnName = "start_time", canBeNull = false)
    private long time;

    /**
     * The default constructor.
     *
     * @param island  The Island this reward belongs to
     * @param booster The upgrade name we are saving
     */
    public IslandBooster(@NotNull Island island, @NotNull String booster) {
        super(island);
        this.booster = booster;
        this.time = 0;
    }

    /**
     * Returns the time the booster was setoff.
     *
     * @return The time the booster was setoff
     */
    public LocalDateTime getTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

    /**
     * Sets the time or the booster
     *
     * @param time The time we are setting
     */
    public void setTime(LocalDateTime time) {
        this.time = ZonedDateTime.of(time, ZoneId.systemDefault()).toInstant().toEpochMilli();
        setChanged(true);
    }

    /**
     * Returns if the booster is currently active
     *
     * @return Returns if the booster is currently active
     */
    public boolean isActive() {
        return LocalDateTime.now().until(getTime(), ChronoUnit.SECONDS) > 0;
    }

    /**
     * Returns the remaining time of this booster.
     *
     * @return The remaining time of this booster
     */
    public long getRemainingTime() {
        return LocalDateTime.now().until(getTime(), ChronoUnit.SECONDS);
    }

    @Override
    public @NotNull String getUniqueKey() {
        return booster + "-" + getIslandId();
    }
}
