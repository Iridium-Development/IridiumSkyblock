package com.iridium.iridiumskyblock.database;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.Color;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @DatabaseField(columnName = "visit")
    private boolean visitable;

    @DatabaseField(columnName = "create_time")
    private long time;

    @DatabaseField(columnName = "value")
    private double value;

    @DatabaseField(columnName = "experience")
    private int experience;

    @DatabaseField(columnName = "color", canBeNull = false)
    private @NotNull Color color;

    /**
     * The default constructor.
     *
     * @param name The name of this island
     */
    public Island(@NotNull String name, @NotNull Schematics.SchematicConfig schematicConfig) {
        this.name = name;
        this.visitable = IridiumSkyblock.getInstance().getConfiguration().defaultIslandPublic;
        this.home = schematicConfig.xHome + "," + schematicConfig.yHome + "," + schematicConfig.zHome + ",0,0";
        this.time = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.color = Color.BLUE;
    }

    /**
     * Used for comparing
     */
    public Island(int id) {
        this.id = id;
    }

    /**
     * Gets the island's level.
     * TODO: Change the equation
     *
     * @return The islands level
     */
    public int getLevel() {
        return (int) Math.cbrt(experience + 1);
    }

    /**
     * Gets a list of Island members as Users.
     *
     * @return A list of all Users belonging to the island
     */
    public List<User> getMembers() {
        return IridiumSkyblock.getInstance().getIslandManager().getIslandMembers(this);
    }

    /**
     * Gets the Islands owner.
     *
     * @return The owner of the Island
     */
    public User getOwner() {
        return IridiumSkyblock.getInstance().getIslandManager().getIslandMembers(this).stream().filter(user ->
                user.getIslandRank().equals(IslandRank.OWNER)
        ).findFirst().orElse(new User(UUID.randomUUID(), IridiumSkyblock.getInstance().getMessages().none));
    }

    /**
     * The Location of the home of this island.
     *
     * @return The home location
     */
    public @NotNull Location getHome() {
        String[] params = home.split(",");
        World world = IridiumSkyblockAPI.getInstance().getWorld();
        return new Location(world, Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]), Float.parseFloat(params[4]), Float.parseFloat(params[3])).add(getCenter(world));
    }

    /**
     * Alters the spawn Location of this island.
     *
     * @param location The new home Location
     */
    public void setHome(@NotNull Location location) {
        Location homeLocation = location.subtract(getCenter(location.getWorld()));
        this.home = homeLocation.getX() + "," + homeLocation.getY() + "," + homeLocation.getZ() + "," + homeLocation.getPitch() + "," + homeLocation.getYaw();
    }

    /**
     * The date this island was created.
     *
     * @return A LocalDateTime of this island was created
     */
    public LocalDateTime getCreateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(getTime()), ZoneId.systemDefault());
    }

    /**
     * Returns the Island's total value, based on the valuable blocks and spawners.
     * TODO: Actually account for spawners
     *
     * @return The Island value
     */
    public double getValue() {
        double value = 0;

        for (XMaterial material : IridiumSkyblock.getInstance().getBlockValues().blockValues.keySet()) {
            value += getValueOf(material);
        }

        return value;
    }

    /**
     * Returns the value of the provided material on this Island.
     *
     * @param material The material
     * @return The value of this block on the island, 0 if it isn't valuable
     */
    public double getValueOf(XMaterial material) {
        Optional<IslandBlocks> islandBlocks = IridiumSkyblock.getInstance().getIslandManager().getIslandBlock(this, material);
        return islandBlocks.map(blocks -> blocks.getAmount() * IridiumSkyblock.getInstance().getBlockValues().blockValues.get(material).value).orElse(0.0);
    }

    /**
     * Gets the Islands current size.
     * Must be lower than the distance between Islands.
     */
    public int getSize() {
        int sizeLevel = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(this, "size").getLevel();
        return IridiumSkyblock.getInstance().getUpgrades().sizeUpgrade.upgrades.get(sizeLevel).size;
    }

    /**
     * Finds the center of this Island.
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

        return location.multiply(IridiumSkyblock.getInstance().getConfiguration().distance).add(0.5, 0, 0.5);
    }

    /**
     * Returns the first corner point Location of this Island.
     * Is smaller than {@link Island#getPos2(World)}.
     *
     * @param world The world where this island is in
     * @return The Location of the first corner point
     */
    public Location getPos1(World world) {
        double size = getSize() / 2.00;
        return getCenter(world).subtract(new Location(world, size, 0, size));
    }

    /**
     * Returns the second corner point Location of this Island.
     * Is greater than {@link Island#getPos1(World)}.
     *
     * @param world The world where this island is in
     * @return The Location of the second corner point
     */
    public Location getPos2(World world) {
        double size = getSize() / 2.00;
        return getCenter(world).add(new Location(world, size, 0, size));
    }

    /**
     * Returns the rank of this Island in comparison to the other Islands.
     *
     * @return The islands rank
     */
    public int getRank() {
        return IridiumSkyblock.getInstance().getIslandManager().getIslands(IslandManager.SortType.VALUE).indexOf(this) + 1;
    }

    /**
     * Returns if a location is inside this Island or not.
     *
     * @param location The location we are testing
     * @return if the location is inside the island
     */
    public boolean isInIsland(@NotNull Location location) {
        return isInIsland(location.getBlockX(), location.getBlockZ());
    }

    /**
     * Returns if the provided x and z coordinates are inside this Island or not.
     *
     * @param x The x coordinates
     * @param z The z coordinates
     * @return Whether or not the coordinates are in this island
     */
    public boolean isInIsland(int x, int z) {
        Location pos1 = getPos1(null);
        Location pos2 = getPos2(null);

        return pos1.getX() <= x && pos1.getZ() <= z && pos2.getX() >= x && pos2.getZ() >= z;
    }

}
