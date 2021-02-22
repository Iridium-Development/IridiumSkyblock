package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.Persist;
import com.iridium.iridiumskyblock.Schematic;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;

/**
 * Represents a User of IridiumSkyblock.
 */
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "schematics")
public final class SchematicData {

    @DatabaseField(columnName = "id", canBeNull = false, id = true)
    private @NotNull String id;

    @DatabaseField(columnName = "schematic", canBeNull = false)
    private @NotNull String schematic;

    public Schematic getSchematic() {
        return IridiumSkyblockAPI.getInstance().getPersist(Persist.PersistType.JSON).load(Schematic.class, new String(Base64.getDecoder().decode(schematic)));
    }

    public void setSchematic(Schematic schematic) {
        this.schematic = new String(Base64.getEncoder().encode(IridiumSkyblockAPI.getInstance().getPersist(Persist.PersistType.JSON).toString(schematic).getBytes()));
    }

    public SchematicData(final @NotNull String id, final @NotNull Schematic schematic) {
        this.id = id;
        setSchematic(schematic);
    }

}
