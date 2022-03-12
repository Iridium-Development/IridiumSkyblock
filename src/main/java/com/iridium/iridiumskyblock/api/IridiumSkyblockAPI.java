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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * General api for IridiumSkyblock.
 * It is accessible via {@link IridiumSkyblockAPI#getInstance()}.
 */
public class IridiumSkyblockAPI {

    private static final IridiumSkyblockAPI instance;
    private final IridiumSkyblock iridiumSkyblock;

    static {
        instance = new IridiumSkyblockAPI(IridiumSkyblock.getInstance());
    }

    /**
     * Constructor for api initialization.
     *
     * @param iridiumSkyblock The instance of the {@link IridiumSkyblock} class
     */
    private IridiumSkyblockAPI(@NotNull IridiumSkyblock iridiumSkyblock) {
        this.iridiumSkyblock = iridiumSkyblock;
    }

    /**
     * Accesses the api instance.
     * Might be null if this method is called when {@link IridiumSkyblock}'s startup method is still being executed.
     *
     * @return the instance of this api
     * @since 3.0.0
     */
    public static @NotNull IridiumSkyblockAPI getInstance() {
        return instance;
    }

    /**
     * Adds an Island BankItem.
     *
     * @param bankItem The specified Bankitem
     * @since 3.0.0
     */
    public void addBankItem(@NotNull BankItem bankItem) {
        iridiumSkyblock.getBankItemList().add(bankItem);
    }

    /**
     * Adds an Island upgrade.
     *
     * @param upgradeName The name of the upgrade (used for storage purposes)
     * @param upgrade     the upgrade item
     * @since 3.0.0
     */
    public void addUpgrade(@NotNull String upgradeName, @NotNull Upgrade<?> upgrade) {
        iridiumSkyblock.getUpgradesList().put(upgradeName, upgrade);
    }

    /**
     * Adds an Island booster.
     *
     * @param boosterName The name of the booster (used for storage purposes)
     * @param booster     The booster Item
     * @since 3.0.0
     */
    public void addBooster(@NotNull String boosterName, @NotNull Booster booster) {
        iridiumSkyblock.getBoosterList().put(boosterName, booster);
    }

    /**
     * Adds an Island permission.
     *
     * @param permission The specified Permission
     * @param key        the unique key associated with this permission
     * @since 3.0.0
     */
    public void addPermission(@NotNull Permission permission, @NotNull String key) {
        iridiumSkyblock.getPermissionList().put(key, permission);
    }

    /**
     * Adds an IridiumSkyblock command.
     *
     * @param command The command that should be added
     * @since 3.0.0
     */
    public void addCommand(@NotNull Command command) {
        iridiumSkyblock.getCommandManager().registerCommand(command);
    }

    /**
     * Gets a {@link User}'s info. Creates one if they don't exist.
     *
     * @param offlinePlayer The player who's data should be fetched
     * @return the user data
     * @since 3.0.0
     */
    public @NotNull User getUser(@NotNull OfflinePlayer offlinePlayer) {
        return iridiumSkyblock.getUserManager().getUser(offlinePlayer);
    }

    /**
     * Finds an Island by its id.
     *
     * @param id The id of the Island
     * @return Optional with the Island, empty if there is none
     * @since 3.0.0
     */
    public @NotNull Optional<Island> getIslandById(int id) {
        return iridiumSkyblock.getIslandManager().getIslandById(id);
    }

    /**
     * Finds an Island by its name.
     *
     * @param name The name of the Island
     * @return Optional with the Island, empty if there is none
     * @since 3.0.0
     */
    public @NotNull Optional<Island> getIslandByName(@NotNull String name) {
        return iridiumSkyblock.getIslandManager().getIslandByName(name);
    }

    /**
     * Gets an {@link Island} from a location.
     *
     * @param location The location you are looking at
     * @return Optional of the Island at the location, empty if there is none
     * @since 3.0.0
     */
    public @NotNull Optional<Island> getIslandViaLocation(@NotNull Location location) {
        return iridiumSkyblock.getIslandManager().getIslandViaLocation(location);
    }

    /**
     * Gets a permission object from name.
     *
     * @param permissionKey The permission key
     * @return the permission
     * @since 3.0.0
     */
    public @NotNull Optional<Permission> getPermissions(@NotNull String permissionKey) {
        return Optional.ofNullable(iridiumSkyblock.getPermissionList().get(permissionKey));
    }

    /**
     * Gets a permission object from name.
     *
     * @param permissionType The permission key
     * @return the permission
     * @since 3.0.4
     */
    public @NotNull Optional<Permission> getPermissions(@NotNull PermissionType permissionType) {
        return getPermissions(permissionType.getPermissionKey());
    }

    /**
     * Gets whether a permission is allowed or denied.
     *
     * @param island     The specified Island
     * @param user       The Specified user
     * @param permission The Specified permission
     * @param key        The permission key
     * @return true if the permission is allowed
     * @since 3.0.0
     */
    public boolean getIslandPermission(@NotNull Island island, @NotNull User user, @NotNull Permission permission, @NotNull String key) {
        return iridiumSkyblock.getIslandManager().getIslandPermission(island, user, permission, key);
    }

    /**
     * Gets whether a permission is allowed or denied.
     *
     * @param island     The specified Island
     * @param user       The specified user
     * @param permissionType The specified permission type
     * @return true if the permission is allowed
     * @since 3.0.4
     */
    public boolean getIslandPermission(@NotNull Island island, @NotNull User user, @NotNull PermissionType permissionType) {
        return iridiumSkyblock.getIslandManager().getIslandPermission(island, user, permissionType);
    }

    /**
     * Gets an Island upgrade.
     *
     * @param island  The specified Island
     * @param upgrade The specified upgrade's name
     * @return the Island upgrade
     * @since 3.0.0
     */
    public @NotNull IslandUpgrade getIslandUpgrade(@NotNull Island island, @NotNull String upgrade) {
        return iridiumSkyblock.getIslandManager().getIslandUpgrade(island, upgrade);
    }

    /**
     * Returns info about an Island booster.
     *
     * @param island  The specified Island
     * @param booster The name of the booster
     * @return the booster of this Island
     * @since 3.0.0
     */
    public @NotNull IslandBooster getIslandBooster(@NotNull Island island, @NotNull String booster) {
        return iridiumSkyblock.getIslandManager().getIslandBooster(island, booster);
    }

    /**
     * Gets all entities on an Island.
     *
     * @param island The specified Island
     * @return all entities on that Island
     * @since 3.0.0
     */
    public @NotNull CompletableFuture<List<Entity>> getEntities(@NotNull Island island, @NotNull World... worlds) {
        return iridiumSkyblock.getIslandManager().getEntities(island, worlds);
    }

    /**
     * Gets a list of Islands sorted by SortType.
     *
     * @param sortType How we are sorting the Islands
     * @return sorted list of all Islands
     * @since 3.0.0
     */
    public @NotNull List<Island> getIslands(@NotNull IslandManager.SortType sortType) {
        return iridiumSkyblock.getIslandManager().getIslands(sortType);
    }

    /**
     * Returns the overworld.
     *
     * @return the main Skyblock {@link World}, might be null if some third-party plugin deleted it
     * @since 3.0.0
     */
    public @Nullable World getWorld() {
        return iridiumSkyblock.getIslandManager().getWorld();
    }

    /**
     * Returns the nether world.
     *
     * @return the nether Skyblock {@link World}, might be null if some third-party plugin deleted it
     * @since 3.0.0
     */
    public @Nullable World getNetherWorld() {
        return iridiumSkyblock.getIslandManager().getNetherWorld();
    }

    /**
     * Returns the end world.
     *
     * @return the nether Skyblock {@link World}, might be null if some third-party plugin deleted it
     * @since 3.0.0
     */
    public @Nullable World getEndWorld() {
        return iridiumSkyblock.getIslandManager().getEndWorld();
    }

    /**
     * Returns whether the specified world is from IridiumSkyblock.
     *
     * @param world The world that should be checked
     * @return true if it is a world used by IridiumSkyblock
     * @since 3.0.7
     */
    public boolean isIslandWorld(@NotNull World world) {
        return Objects.equals(getWorld(), world) || Objects.equals(getNetherWorld(), world) || Objects.equals(getEndWorld(), world);
    }

    /**
     * Returns if this is the overworld of IridiumSkyblock.
     *
     * @param world The world that should be checked
     * @return true if this is the overworld of IridiumSkyblock
     * @since 3.1.3
     */
    public boolean isIslandOverWorld(@NotNull World world) {
        return iridiumSkyblock.getIslandManager().isIslandOverWorld(world);
    }

    /**
     * Returns if this is the nether world of IridiumSkyblock.
     *
     * @param world The world that should be checked
     * @return true if this is the nether world of IridiumSkyblock
     * @since 3.1.3
     */
    public boolean isIslandNether(@NotNull World world) {
        return iridiumSkyblock.getIslandManager().isIslandNether(world);
    }

    /**
     * Returns if this is the end world of IridiumSkyblock.
     *
     * @param world The world that should be checked
     * @return true if this is the end world of IridiumSkyblock
     * @since 3.1.3
     */
    public boolean isIslandEnd(@NotNull World world) {
        return iridiumSkyblock.getIslandManager().isIslandEnd(world);
    }

    /**
     * Returns whether the specified player can visit the provided Island.<p>
     *
     * @param user the user
     * @param island the Island
     * @return true if the user can visit the Island
     * @since 3.2.7
     */
    public boolean canVisitIsland(@NotNull User user, @NotNull Island island) {
        if (IridiumSkyblock.getInstance().getIslandManager().isBannedOnIsland(island, user)) {
            return false;
        }

        return island.isVisitable() || user.isBypassing() || user.getPlayer().hasPermission("iridiumskyblock.visitbypass") || island.getMembers().contains(user) || IridiumSkyblock.getInstance().getIslandManager().getIslandTrusted(island, user).isPresent();
    }

}
