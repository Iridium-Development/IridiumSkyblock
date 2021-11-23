package com.iridium.iridiumskyblock.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_settings")
public final class IslandSetting extends IslandData {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false, uniqueCombo = true)
    private int id;

    @DatabaseField(columnName = "setting", canBeNull = false, uniqueCombo = true)
    private @NotNull String setting;

    @DatabaseField(columnName = "value", canBeNull = false)
    private String value;

    public boolean getBooleanValue() {
        return value.equalsIgnoreCase("true");
    }

    /**
     * The default constructor.
     *
     * @param island  The Island that has this permission
     * @param setting The name of the Island setting
     * @param value   The value of the setting
     */
    public IslandSetting(@NotNull Island island, @NotNull String setting, String value) {
        super(island);
        this.setting = setting;
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
        setChanged(true);
    }
}
