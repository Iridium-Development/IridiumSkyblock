package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumcore.Color;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.Cache;
import com.iridium.iridiumskyblock.DatabaseObject;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.configs.BlockValues;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import redempt.crunch.CompiledExpression;
import redempt.crunch.Crunch;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an Island of IridiumSkyblock.
 */
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "islands")
public final class Island extends DatabaseObject {

    private final static CompiledExpression islandLevelEquation = Crunch.compileExpression(IridiumSkyblock.getInstance().getConfiguration().islandLevelEquation);

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "name", unique = true)
    private String name;

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

    @DatabaseField(columnName = "experience")
    private int experience;

    @DatabaseField(columnName = "extra_value")
    private double extraValue;

    @DatabaseField(columnName = "color", canBeNull = false)
    private @NotNull Color color;

    // Cache resets every 0.5 seconds
    private final Cache<Double> valueCache = new Cache<>(500);

    // Cache
    private Integer size;

    public Island(String name, int id) {
        this(name, IridiumSkyblock.getInstance().getSchematics().schematics.values().stream().findFirst().get());
        this.id = id;
    }

    /**
     * The default constructor.
     *
     * @param name            The name of this island
     * @param schematicConfig The schematic of the island
     */
    public Island(@NotNull String name, @NotNull Schematics.SchematicConfig schematicConfig) {
        this.name = name;
        this.visitable = IridiumSkyblock.getInstance().getConfiguration().defaultIslandPublic;
        this.home = schematicConfig.xHome + "," + schematicConfig.yHome + "," + schematicConfig.zHome + ",0," + schematicConfig.yawHome;
        this.time = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.color = IridiumSkyblock.getInstance().getBorder().defaultColor;
    }

    /**
     * Used for comparing
     */
    public Island(int id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? IridiumSkyblock.getInstance().getConfiguration().defaultIslandName.replace("%island_owner_name%", getOwner().getName()) : name;
    }

    /**
     * Gets the island's level.
     *
     * @return The islands level
     */
    public int getLevel() {
        return (int) islandLevelEquation.evaluate(experience);
    }

    /**
     * Returns the minimum experience required to reach this level
     * The inverse of getLevel
     *
     * @param level The level
     * @return The experience required to reach this level
     */
    private int getExperienceRequired(int level) {
        int lower = 0;
        int upper = Integer.MAX_VALUE;
        while (lower < upper) {
            int mid = (lower + upper) / 2;
            double experience = islandLevelEquation.evaluate(mid);
            if (experience == level) {
                return mid;
            } else if (experience < level) {
                lower = mid + 1;
            } else {
                upper = mid;
            }
        }
        return 0;
    }

    /**
     * Gets the players current experience (resets to 0 each levelup)
     *
     * @return Gets the players current experience (resets to 0 each levelup)
     */
    public int getExperience() {
        return getTotalExperience() - getExperienceRequired(getLevel());
    }

    /**
     * Gets the players total experience
     *
     * @return The players total experience
     */
    public int getTotalExperience() {
        return experience;
    }

    /**
     * Gets the required experience required to levelup
     *
     * @return the required experience required to levelup
     */
    public int getExperienceRequiredToLevelUp() {
        return getExperienceRequired(getLevel() + 1);
    }

    /**
     * Gets the remaining experience required to levelup
     *
     * @return the remaining experience required to levelup
     */
    public int getExperienceRemainingToLevelUp() {
        return getExperience() - getExperienceRequiredToLevelUp();
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
        World world = IridiumSkyblock.getInstance().getIslandManager().getWorld();
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
        setChanged(true);
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
     *
     * @return The Island value
     */
    public double getValue() {
        return valueCache.getCache(() -> {
            double value = extraValue;

            for (Map.Entry<XMaterial, BlockValues.ValuableBlock> valuableBlock : IridiumSkyblock.getInstance().getBlockValues().blockValues.entrySet()) {
                value += IridiumSkyblock.getInstance().getIslandManager().getIslandBlockAmount(this, valuableBlock.getKey()) * valuableBlock.getValue().value;
            }

            for (Map.Entry<EntityType, BlockValues.ValuableBlock> valuableSpawner : IridiumSkyblock.getInstance().getBlockValues().spawnerValues.entrySet()) {
                value += IridiumSkyblock.getInstance().getIslandManager().getIslandSpawnerAmount(this, valuableSpawner.getKey()) * valuableSpawner.getValue().value;
            }

            return value;
        });
    }

    /**
     * Returns the value of the provided material on this Island.
     *
     * @param material The material
     * @return The value of this block on the island, 0 if it isn't valuable
     */
    public double getValueOf(XMaterial material) {
        return IridiumSkyblock.getInstance().getBlockValues().blockValues.getOrDefault(material, new BlockValues.ValuableBlock(0, "", 0, 0)).value;
    }

    /**
     * Returns the value of the provided material on this Island.
     *
     * @param spawnerType The spawnerType
     * @return The value of this block on the island, 0 if it isn't valuable
     */
    public double getValueOf(EntityType spawnerType) {
        return IridiumSkyblock.getInstance().getBlockValues().spawnerValues.getOrDefault(spawnerType, new BlockValues.ValuableBlock(0, "", 0, 0)).value;
    }

    /**
     * Gets the Islands current size.
     * Must be lower than the distance between Islands.
     */
    public int getSize() {
        if (size == null) {
            int sizeLevel = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(this, "size").getLevel();
            size = IridiumSkyblock.getInstance().getUpgrades().sizeUpgrade.upgrades.get(sizeLevel).size;
        }
        return size;
    }

    public void setColor(@NotNull Color color) {
        this.color = color;
        IridiumSkyblock.getInstance().getIslandManager().sendIslandBorder(this);
        setChanged(true);
    }

    public void setExperience(int experience) {
        int currentLevel = getLevel();
        this.experience = experience;
        int newLevel = getLevel();
        if (newLevel > currentLevel) {
            IridiumSkyblock.getInstance().getIslandManager().islandLevelUp(this, newLevel);
        }
        setChanged(true);
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
        // In this algorithm position 2 is where id 1 is, position 3 is where id 2 is, ect.
        int position = id - 1;

        // The radius of the last completed square
        int radius = (int) (Math.floor((Math.sqrt(position) - 1) / 2) + 1);
        int diameter = radius * 2;
        int perimeter = diameter * 4;

        // The position the square was last completed at
        int lastCompletePosition = (perimeter * (radius - 1)) / 2;

        // The current index in the perimeter where 1 is first and 0 is the last index
        int currentIndexInPerimeter = (position - lastCompletePosition) % perimeter;

        Location location;

        switch (currentIndexInPerimeter / diameter) {
            case 0:
                location = new Location(world, (currentIndexInPerimeter - radius), 0, -radius);
                break;
            case 1:
                location = new Location(world, radius, 0, (currentIndexInPerimeter % diameter) - radius);
                break;
            case 2:
                location = new Location(world, radius - (currentIndexInPerimeter % diameter), 0, radius);
                break;
            case 3:
                location = new Location(world, -radius, 0, radius - (currentIndexInPerimeter % diameter));
                break;
            default:
                throw new IllegalStateException("Could not find island location with ID: " + id);
        }

        return location.multiply(IridiumSkyblock.getInstance().getConfiguration().distance);
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
        IslandManager islandManager = IridiumSkyblock.getInstance().getIslandManager();
        World world = location.getWorld();
        if (Objects.equals(world, islandManager.getWorld()) || Objects.equals(world, islandManager.getNetherWorld()) || Objects.equals(world, islandManager.getEndWorld())) {
            return isInIsland(location.getBlockX(), location.getBlockZ());
        } else {
            return false;
        }
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

    /**
     * Returns the money of this island that is in the Island bank.
     *
     * @return The money in the bank
     */
    public double getMoney() {
        return IridiumSkyblock.getInstance().getIslandManager()
                .getIslandBank(this, IridiumSkyblock.getInstance().getBankItems().moneyBankItem).getNumber();
    }

    /**
     * Returns the experience of this island that is in the Island bank.
     *
     * @return The experience in the bank
     */
    public long getBankExperience() {
        return (long) IridiumSkyblock.getInstance().getIslandManager()
                .getIslandBank(this, IridiumSkyblock.getInstance().getBankItems().experienceBankItem).getNumber();
    }

    /**
     * Returns the crystals of this island that are in the Island bank.
     *
     * @return The crystals in the bank
     */
    public int getCrystals() {
        return (int) IridiumSkyblock.getInstance().getIslandManager()
                .getIslandBank(this, IridiumSkyblock.getInstance().getBankItems().crystalsBankItem).getNumber();
    }

    /**
     * Formatted version of {@link Island#getValue()}.
     *
     * @return The formatted value
     */
    public String getFormattedValue() {
        return IridiumSkyblock.getInstance().getNumberFormatter().format(getValue());
    }

    /**
     * Formatted version of {@link Island#getExperience()}.
     *
     * @return The formatted experience
     */
    public String getFormattedExperience() {
        return IridiumSkyblock.getInstance().getNumberFormatter().format(getExperience());
    }

    /**
     * Formatted version of {@link Island#getMoney()}.
     *
     * @return The formatted money in the Island bank
     */
    public String getFormattedMoney() {
        return IridiumSkyblock.getInstance().getNumberFormatter().format(getMoney());
    }

    /**
     * Formatted version of {@link Island#getBankExperience()}.
     *
     * @return The formatted experience in the Island bank
     */
    public String getFormattedBankExperience() {
        return IridiumSkyblock.getInstance().getNumberFormatter().format(getBankExperience());
    }

    /**
     * Formatted version of {@link Island#getCrystals()}.
     *
     * @return The formatted crystals in the Island bank
     */
    public String getFormattedCrystals() {
        return IridiumSkyblock.getInstance().getNumberFormatter().format(getCrystals());
    }

    public void resetCache() {
        size = null;
    }

    public void setName(String name) {
        this.name = name;
        setChanged(true);
    }

    public void setVisitable(boolean visitable) {
        this.visitable = visitable;
        setChanged(true);
    }

    public void setTime(long time) {
        this.time = time;
        setChanged(true);
    }

    public void setExtraValue(double extraValue) {
        this.extraValue = extraValue;
        setChanged(true);
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
