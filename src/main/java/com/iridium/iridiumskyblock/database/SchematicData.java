package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Persist;
import com.iridium.iridiumskyblock.Schematic;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;

/**
 * Represents data of a schematic from IridiumSkyblock.
 */
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "schematics")
public final class SchematicData {

    @DatabaseField(columnName = "id", canBeNull = false, id = true)
    private @NotNull String id;

    @DatabaseField(columnName = "schematic", canBeNull = false)
    private @NotNull String schematic;

    /**
     * Constructs the Schematic object to this data.
     * Decodes the base64 schematic data.
     *
     * @return The newly loaded schematic object
     */
    public Schematic getSchematic() {
        return new Persist(Persist.PersistType.JSON, IridiumSkyblock.getInstance()).load(Schematic.class, new String(Base64.getDecoder().decode(schematic)));
    }

    /**
     * Copy the provided schematic and save its data.
     * Converts it into base64.
     *
     * @param schematic The schematic that should be saved
     */
    public void setSchematic(Schematic schematic) {
        this.schematic = new String(Base64.getEncoder().encode(new Persist(Persist.PersistType.JSON, IridiumSkyblock.getInstance()).toString(schematic).getBytes()));
    }

    /**
     * The default constructor.
     *
     * @param id        The ID of this schematic, used as a unique identifier
     * @param schematic The schematic whose data should be saved
     */
    public SchematicData(final @NotNull String id, final @NotNull Schematic schematic) {
        this.id = id;
        setSchematic(schematic);
    }

    /**
     * The default constructor.
     *
     * @param id        The ID of this schematic, used as a unique identifier
     * @param schematic The schematic whose data should be saved
     */
    public SchematicData(final @NotNull String id, final @NotNull String schematic) {
        this.id = id;
        this.schematic = schematic;
    }

}
