package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@NoArgsConstructor
@DatabaseTable(tableName = "islands")
public final class Island {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    @Setter(AccessLevel.PRIVATE)
    private int id;

    @DatabaseField(columnName = "name", canBeNull = false)
    @NotNull
    private String name;

    //Stores the islands home relative to the island center
    @DatabaseField(columnName = "home")
    @NotNull
    private String home;

    @ForeignCollectionField(eager = true)
    @Setter(AccessLevel.PRIVATE)
    private ForeignCollection<User> members;

    @NotNull
    public Location getHome() {
        String[] params = home.split(",");
        World world = IridiumSkyblockAPI.getInstance().getWorld();
        return new Location(world, Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]), Float.parseFloat(params[3]), Float.parseFloat(params[4])).add(getCenter(world));
    }

    public void setHome(@NotNull Location location) {
        location = getCenter(location.getWorld()).subtract(location);
        this.home = location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getPitch() + "," + location.getYaw();
    }

    //Function based off: https://stackoverflow.com/a/19287714
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

    public Location getPos1(World world) {
        int size = IridiumSkyblock.getInstance().getConfiguration().distance - 1;
        return getCenter(world).subtract(new Location(world, size, 0, size));
    }

    public Location getPos2(World world) {
        int size = IridiumSkyblock.getInstance().getConfiguration().distance - 1;
        return getCenter(world).add(new Location(world, size, 0, size));
    }

    public Island(@NotNull String name) {
        this.name = name;
    }
}
