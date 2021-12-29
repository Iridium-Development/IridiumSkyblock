package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.*;
import com.iridium.iridiumskyblock.bank.BankItem;
import com.iridium.iridiumskyblock.commands.Command;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import com.iridium.iridiumskyblock.database.IslandUpgrade;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.managers.IslandManager;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * General api for IridiumSkyblock.
 * It is accessible via {@link IridiumSkyblockAPI#getInstance()}.
 */
public class IridiumSkyblockAPI {

    private static IridiumSkyblockAPI instance;
    private final IridiumSkyblock iridiumSkyblock;

    static {
        instance = new IridiumSkyblockAPI(IridiumSkyblock.getInstance());
    }

    /**
     * Constructor for api initialization.
     *
     * @param iridiumSkyblock The instance of the {@link IridiumSkyblock} class
     */
    private IridiumSkyblockAPI(IridiumSkyblock iridiumSkyblock) {
        this.iridiumSkyblock = iridiumSkyblock;
    }

    /**
     * Accesses the api instance.
     * Might be null if this method is called when {@link IridiumSkyblock}'s startup method is still being executed.
     *
     * @return The instance of this api
     * @since 3.0.0
     */
    public static IridiumSkyblockAPI getInstance() {
        return instance;
    }

    /**
     * Adds an Island BankItem
     *
     * @param bankItem The specified Bankitem
     */
    public void addBankItem(@NotNull BankItem bankItem) {
        iridiumSkyblock.getBankItemList().add(bankItem);
    }

    /**
     * Adds an Island Upgrade
     *
     * @param upgradeName The name of the Upgrade (Used for storage purposes)
     * @param upgrade     the upgrade item
     */
    public void addUpgrade(@NotNull String upgradeName, @NotNull Upgrade<?> upgrade) {
        iridiumSkyblock.getUpgradesList().put(upgradeName, upgrade);
    }

    /**
     * Adds an Island Booster
     *
     * @param boosterName The name of the booster (Used for storage purposes)
     * @param booster     The booster Item
     */
    public void addBooster(@NotNull String boosterName, @NotNull Booster booster) {
        iridiumSkyblock.getBoosterList().put(boosterName, booster);
    }

    /**
     * Adds an island permission
     *
     * @param permission The specified Permission
     * @param key        The unique key associated with this permission
     */
    public void addPermission(@NotNull Permission permission, @NotNull String key) {
        iridiumSkyblock.getPermissionList().put(key, permission);
    }

    /**
     * Adds an island command
     *
     * @param command The command we are adding
     */
    public void addCommand(@NotNull Command command) {
        iridiumSkyblock.getCommandManager().registerCommand(command);
    }

    /**
     * Gets a {@link User}'s info. Creates one if he doesn't exist.
     *
     * @param offlinePlayer The player who's data should be fetched
     * @return The user data
     */
    public @NotNull User getUser(@NotNull OfflinePlayer offlinePlayer) {
        return iridiumSkyblock.getUserManager().getUser(offlinePlayer);
    }

    /**
     * Finds an Island by its id.
     *
     * @param id The id of the island
     * @return An Optional with the Island, empty if there is none
     */
    public Optional<Island> getIslandById(int id) {
        return iridiumSkyblock.getIslandManager().getIslandById(id);
    }

    /**
     * Finds an Island by its name.
     *
     * @param name The name of the island
     * @return An Optional with the Island, empty if there is none
     */
    public Optional<Island> getIslandByName(String name) {
        return iridiumSkyblock.getIslandManager().getIslandByName(name);
    }

    /**
     * Gets an {@link Island} from a location.
     *
     * @param location The location you are looking at
     * @return Optional of the island at the location, empty if there is none
     */
    public @NotNull Optional<Island> getIslandViaLocation(@NotNull Location location) {
        return iridiumSkyblock.getIslandManager().getIslandViaLocation(location);
    }

    /**
     * Gets a permission object from name
     *
     * @param permissionKey The permission key
     * @return The the permission object
     */
    public Optional<Permission> getPermissions(@NotNull String permissionKey) {
        return Optional.ofNullable(iridiumSkyblock.getPermissionList().get(permissionKey));
    }

    /**
     * Gets a permission object from name
     *
     * @param permissionKey The permission key
     * @return The the permission object
     */
    public Optional<Permission> getPermissions(@NotNull PermissionType permissionKey) {
        return getPermissions(permissionKey.getPermissionKey());
    }

    /**
     * Gets whether a permission is allowed or denied.
     *
     * @param island     The specified Island
     * @param user       The Specified User
     * @param permission The Specified permission
     * @param key        The permission key
     * @return The the permission is allowed
     */
    public boolean getIslandPermission(@NotNull Island island, @NotNull User user, @NotNull Permission permission, @NotNull String key) {
        return iridiumSkyblock.getIslandManager().getIslandPermission(island, user, permission, key);
    }

    /**
     * Gets whether a permission is allowed or denied.
     *
     * @param island     The specified Island
     * @param user       The Specified User
     * @param permission The Specified permission type
     * @return The the permission is allowed
     */
    public boolean getIslandPermission(@NotNull Island island, @NotNull User user, @NotNull PermissionType permission) {
        return iridiumSkyblock.getIslandManager().getIslandPermission(island, user, permission);
    }

    /**
     * Gets an Island upgrade
     *
     * @param island  The specified Island
     * @param upgrade The specified Upgrade's name
     * @return The island Upgrade
     */
    public IslandUpgrade getIslandUpgrade(@NotNull Island island, @NotNull String upgrade) {
        return iridiumSkyblock.getIslandManager().getIslandUpgrade(island, upgrade);
    }

    /**
     * Gets time remaining on an island booster
     *
     * @param island  The specified Island
     * @param booster The name of the Booster
     * @return The time remaining
     */
    public IslandBooster getIslandBooster(@NotNull Island island, @NotNull String booster) {
        return iridiumSkyblock.getIslandManager().getIslandBooster(island, booster);
    }

    /**
     * Gets all entities on an island
     *
     * @param island The specified Island
     * @return A list of all entities on that island
     */
    public CompletableFuture<List<Entity>> getEntities(@NotNull Island island, @NotNull World... worlds) {
        return iridiumSkyblock.getIslandManager().getEntities(island, worlds);
    }

    /**
     * Gets a list of islands sorted by SortType
     *
     * @param sortType How we are sorting the islands
     * @return The sorted list of islands
     */
    public List<Island> getIslands(IslandManager.SortType sortType) {
        return iridiumSkyblock.getIslandManager().getIslands(sortType);
    }

    /**
     * Returns the overworld.
     *
     * @return The main skyblock {@link World}, might be null if some third-party plugin deleted it
     * @since 3.0.0
     */
    public World getWorld() {
        return iridiumSkyblock.getIslandManager().getWorld();
    }

    /**
     * Returns the NetherWorld
     *
     * @return The nether skyblock {@link World}, might be null if some third-party plugin deleted it
     * @since 3.0.0
     */
    public World getNetherWorld() {
        return iridiumSkyblock.getIslandManager().getNetherWorld();
    }

    /**
     * Returns the NetherWorld
     *
     * @return The nether skyblock {@link World}, might be null if some third-party plugin deleted it
     * @since 3.0.0
     */
    public World getEndWorld() {
        return iridiumSkyblock.getIslandManager().getEndWorld();
    }

    /**
     * Returns whether the specified world is from IridiumSkyblock.
     *
     * @param world Thw world that should be checked
     * @return true if it is a world used by IridiumSkyblock
     * @since 3.0.7
     */
    public boolean isIslandWorld(World world) {
        return Objects.equals(getWorld(), world) || Objects.equals(getNetherWorld(), world) || Objects.equals(getEndWorld(), world);
    }

    public boolean isIslandOverWorld(World world) {
        return iridiumSkyblock.getIslandManager().isIslandOverWorld(world);
    }

    public boolean isIslandNether(World world) {
        return iridiumSkyblock.getIslandManager().isIslandNether(world);
    }

    public boolean isIslandEnd(World world) {
        return iridiumSkyblock.getIslandManager().isIslandEnd(world);
    }

}
