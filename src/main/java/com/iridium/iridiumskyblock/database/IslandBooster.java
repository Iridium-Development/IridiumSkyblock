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

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "booster", canBeNull = false)
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
    }

    /**
     * Returns if the booster is currently active
     *
     * @return Returns if the booster is currently active
     */
    public boolean isActive() {
        return LocalDateTime.now().until(getTime(), ChronoUnit.SECONDS) > 0;
    }

    @Override
    public @NotNull String getUniqueKey() {
        return booster + "-" + getIsland().map(Island::getId).orElse(0);
    }
}
