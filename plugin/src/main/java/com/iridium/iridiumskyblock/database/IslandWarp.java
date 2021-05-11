package com.iridium.iridiumskyblock.database;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_warps")
public final class IslandWarp extends IslandData {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "location")
    private @NotNull String location;

    @DatabaseField(columnName = "name")
    private @NotNull String name;

    @DatabaseField(columnName = "password")
    @Setter
    private String password;

    @DatabaseField(columnName = "description")
    @Setter
    private String description;

    @DatabaseField(columnName = "icon")
    @Setter
    private XMaterial icon;

    /**
     * The default constructor.
     *
     * @param island The Island this invite belongs to
     */
    public IslandWarp(@NotNull Island island, @NotNull Location location, @NotNull String name) {
        super(island);
        this.location =
                location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getPitch() +
                        "," + location.getYaw();
        this.name = name;
        this.icon = IridiumSkyblock.getInstance().getInventories().warpsGUI.item.material;
    }

    /**
     * The Location of this Warp.
     *
     * @return The warp location
     */
    public @NotNull Location getLocation() {
        String[] params = location.split(",");
        World world = Bukkit.getWorld(params[0]);
        return new Location(world, Double.parseDouble(params[1]), Double.parseDouble(params[2]), Double.parseDouble(params[3]),
                Float.parseFloat(params[5]), Float.parseFloat(params[4]));
    }
}
