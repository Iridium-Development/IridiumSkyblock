package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents an Island of IridiumSkyblock.
 */
@Getter
@Setter
@NoArgsConstructor
@DatabaseTable(tableName = "islands")
public final class Island {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    @Setter(AccessLevel.PRIVATE)
    private int id;

    @DatabaseField(columnName = "name", canBeNull = false, unique = true)
    private @NotNull String name;

    /*
    The islands home relative to the island center as a string.
    Format: x,y,z,pitch,yaw
    */
    @DatabaseField(columnName = "home")
    private @NotNull String home;

    /**
     * The default constructor.
     *
     * @param name The name of this island
     */
    public Island(@NotNull String name, @NotNull Schematics.SchematicConfig schematicConfig) {
        this.name = name;
        this.home = schematicConfig.xHome + "," + schematicConfig.yHome + "," + schematicConfig.zHome + ",0,0";
    }

    /**
     * Gets a list of Island Members
     *
     * @return A list of all Users belonging to the island
     */
    public List<User> getMembers() {
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandMembers(this);
    }

    /**
     * The Location of the home of this island.
     *
     * @return The home location
     */
    public @NotNull Location getHome() {
        String[] params = home.split(",");
        World world = IridiumSkyblockAPI.getInstance().getWorld();
        return new Location(world, Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]), Float.parseFloat(params[3]), Float.parseFloat(params[4])).add(getCenter(world));
    }

    /**
     * Alters the spawn Location of this island.
     *
     * @param location The new home Location
     */
    public void setHome(@NotNull Location location) {
        Location homeLocation = getCenter(location.getWorld()).subtract(location);
        this.home = homeLocation.getX() + "," + homeLocation.getY() + "," + homeLocation.getZ() + "," + homeLocation.getPitch() + "," + homeLocation.getYaw();
    }

    /**
     * Finds the center of this island.
     * Function based of: https://stackoverflow.com/a/19287714.
     *
     * @param world The world where this island is in
     * @return The center Location of this island
     */
    public Location getCenter(World world) {
        if (id == 1) return new Location(world, 0, 0, 0);
        // In this algorithm id  0 will be where we want id 2 to be and 1 will be where 3 is ect
        int n = id - 2;

        int r = (int) (Math.floor((Math.sqrt(n + 1) - 1) / 2) + 1);
        // compute radius : inverse arithmetic sum of 8+16+24+...=

        int p = (8 * r * (r - 1)) / 2;
        // compute total point on radius -1 : arithmetic sum of 8+16+24+...

        int en = r * 2;
        // points by face

        int a = (1 + n - p) % (r * 8);
        // compute de position and shift it so the first is (-r,-r) but (-r+1,-r)
        // so square can connect

        Location location;

        switch (a / (r * 2)) {
            case 0:
                location = new Location(world, (a - r), 0, -r);
                break;
            case 1:
                location = new Location(world, r, 0, (a % en) - r);
                break;
            case 2:
                location = new Location(world, r - (a % en), 0, r);
                break;
            case 3:
                location = new Location(world, -r, 0, r - (a % en));
                break;
            default:
                throw new IllegalStateException("Could not find island location with ID: " + id);
        }

        return location.multiply(IridiumSkyblock.getInstance().getConfiguration().distance);
    }

    /**
     * Returns the first corner point Location of this island.
     * Is smaller than {@link Island#getPos2(World)}.
     *
     * @param world The world where this island is in
     * @return The Location of the first corner point
     */
    public Location getPos1(World world) {
        int size = IridiumSkyblock.getInstance().getConfiguration().distance - 1;
        return getCenter(world).subtract(new Location(world, size, 0, size));
    }

    /**
     * Returns the second corner point Location of this island.
     * Is greater than {@link Island#getPos1(World)}.
     *
     * @param world The world where this island is in
     * @return The Location of the second corner point
     */
    public Location getPos2(World world) {
        int size = IridiumSkyblock.getInstance().getConfiguration().distance - 1;
        return getCenter(world).add(new Location(world, size, 0, size));
    }

    /**
     * Returns if a location is inside the island or not
     *
     * @param location The location we are testing
     * @return if the location is inside the island
     */

    public boolean isInIsland(@NotNull Location location) {
        Location pos1 = getPos1(location.getWorld());
        Location pos2 = getPos2(location.getWorld());
        return pos1.getX() <= location.getX() && pos1.getZ() <= location.getZ() && pos2.getX() >= location.getX() && pos2.getZ() >= location.getZ();
    }

}
