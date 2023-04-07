package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.*;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.Permission;
import com.iridium.iridiumteams.PermissionType;
import com.iridium.iridiumteams.bank.BankItem;
import com.iridium.iridiumteams.commands.Command;
import com.iridium.iridiumteams.database.IridiumUser;
import com.iridium.iridiumteams.database.Team;
import com.iridium.iridiumteams.database.TeamEnhancement;
import com.iridium.iridiumteams.enhancements.Enhancement;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/*
    Events that need to be added:
    - Island Management:
        - Island Members (Add/Remove, Trust)
        - Island Bank (Deposit/Withdraw, Currency Type)
        - Island Level (Level, Value)
        - Island Size (Enhancements, Currents)
        - Island Biome
        - Island Missions (Complete, Redeem)
        - Island Creation
        - Island Warp (Create, Delete, Warp)
    - Player Interactions
        - Island Members (Kick, Ban, Mute, Unmute)
        - Shop Interactions (Buy, Sell, Bulk)
        - Island Chat (Send)
     */

/**
 * General api for IridiumSkyblock.
 * It is accessible via {@link IridiumSkyblockAPI#getInstance()}.
 */
public class IridiumSkyblockAPI <T extends Team, U extends IridiumUser<T>> {

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
    private IridiumSkyblockAPI(IridiumSkyblock iridiumSkyblock) {
        this.iridiumSkyblock = iridiumSkyblock;
    }

    /**
     * Accesses the api instance.
     * Might be null if this method is called when {@link IridiumSkyblock}'s startup method is still being executed.
     *
     * @return the instance of this api
     * @since 4.0.1
     */
    public static @NotNull IridiumSkyblockAPI getInstance() {
        return instance;
    }

    /**
     * Adds an Island Member
     *
     * @param player the specified Player
     * @param island the specified Island for the player to join
     */
    public void addPlayerToIsland(OfflinePlayer player, Island island) {
        IridiumSkyblock.getInstance().getUserManager().getUser(player).setTeam(island);
    }

    /**
     * Removes an Island Member
     *
     * @param player the specified Player
     */
    public void removePlayerFromIsland(OfflinePlayer player) {
        IridiumSkyblock.getInstance().getUserManager().getUser(player).setTeam(null);
    }

    /**
     * Adds an Island BankItem.
     *
     * @param bankItem The specified Bankitem
     * @since 4.0.1
     */
    public void addBankItem(@NotNull BankItem bankItem) {
        iridiumSkyblock.getBankItemList().add(bankItem);
    }

    /**
     * Deposits currency into the Island Bank
     *
     * @param player the specified Player
     * @param amount the amount of vault currency to be deposited
     */
    public void depositVault(Player player, int amount) {

        IridiumSkyblock.getInstance().getBankItems().moneyBankItem.deposit(
                player,
                amount,
                IridiumSkyblock.getInstance().getTeamManager().getTeamBank(
                        IridiumSkyblock.getInstance().getUserManager().getUser(player).getIsland().get(),
                        IridiumSkyblock.getInstance().getBankItems().moneyBankItem.toString()),
                IridiumSkyblock.getInstance());
    }

    /**
     * Withdraws currency from the Island Bank
     *
     * @param player the specified Player
     * @param amount the amount of vault currency to be withdrawn
     * @since 4.0.1
     */
    public void withdrawVault(Player player, int amount) {

        IridiumSkyblock.getInstance().getBankItems().moneyBankItem.withdraw(
                player,
                amount,
                IridiumSkyblock.getInstance().getTeamManager().getTeamBank(
                        IridiumSkyblock.getInstance().getUserManager().getUser(player).getIsland().get(),
                        IridiumSkyblock.getInstance().getBankItems().moneyBankItem.toString()),
                IridiumSkyblock.getInstance());
    }

    /**
     * Deposits crystals into the Island Bank
     *
     * @param player the specified Player
     * @param amount the amount of crystals to be deposited
     * @since 4.0.1
     */
    public void depositCrystals(Player player, int amount) {

        IridiumSkyblock.getInstance().getBankItems().crystalsBankItem.deposit(
                player,
                amount,
                IridiumSkyblock.getInstance().getTeamManager().getTeamBank(
                        IridiumSkyblock.getInstance().getUserManager().getUser(player).getIsland().get(),
                        IridiumSkyblock.getInstance().getBankItems().crystalsBankItem.toString()),
                IridiumSkyblock.getInstance());
    }

    /**
     * Withdraws crystals from the Island Bank
     *
     * @param player the specified Player
     * @param amount the amount of crystals to be withdrawn
     * @since 4.0.1
     */
    public void withdrawCrystals(Player player, int amount) {

        IridiumSkyblock.getInstance().getBankItems().crystalsBankItem.deposit(
                player,
                amount,
                IridiumSkyblock.getInstance().getTeamManager().getTeamBank(
                        IridiumSkyblock.getInstance().getUserManager().getUser(player).getIsland().get(),
                        IridiumSkyblock.getInstance().getBankItems().crystalsBankItem.toString()),
                IridiumSkyblock.getInstance());
    }

    /**
     * Gets the value of an island.
     *
     * @param island the specified Player.
     * @since 4.0.1
     */
    public double getValue(Island island) {
        return IridiumSkyblock.getInstance().getIslandManager().getTeamValue(island);
    }

    /**
     * Gets the size of an island.
     *
     * @param island the Island specified
     * @since 4.0.1
     */
    public int getIslandSize(Island island) {
        return island.getSize();
    }

    /**
     * Adds an Island Enhancement.
     *
     * @param enhancementName The name of the enhancement (used for storage purposes)
     * @param enhancement     the enhancement
     * @since 4.0.1
     */
    public void addEnhancement(@NotNull String enhancementName, @NotNull Enhancement<?> enhancement) {
        iridiumSkyblock.getEnhancements().put(enhancementName, enhancement);
    }

    /**
     * Gets an Island Enhancement.
     *
     * @param island  The specified Island
     * @param enhancementName The specified enhancement's name
     * @return the Island enhancement
     * @since 4.0.1
     */
    public TeamEnhancement getIslandEnhancement(@NotNull Island island, @NotNull String enhancementName) {
        return iridiumSkyblock.getIslandManager().getTeamEnhancement(island, enhancementName);
    }

    /**
     * Adds an Island permission.
     *
     * @param permission The specified Permission
     * @param key        the unique key associated with this permission
     * @since 4.0.1
     */
    public void addPermission(@NotNull Permission permission, @NotNull String key) {
        iridiumSkyblock.getPermissionList().put(key, permission);
    }

    /**
     * Adds an IridiumSkyblock command.
     *
     * @param command The command that should be added
     * @since 4.0.1
     */
    public void addCommand(@NotNull Command<T, U> command) {
        iridiumSkyblock.getCommandManager().registerCommand((Command<Island, User>) command);
    }

    /**
     * Gets a {@link User}'s info. Creates one if they don't exist.
     *
     * @param offlinePlayer The player whose data should be fetched
     * @return the user data
     * @since 4.0.1
     */
    public @NotNull User getUser(@NotNull OfflinePlayer offlinePlayer) {
        return iridiumSkyblock.getUserManager().getUser(offlinePlayer);
    }

    /**
     * Gets a {@link User}'s info. Creates one if they don't exist.
     *
     * @param player The player whose data should be fetched
     * @return the user data
     * @since 4.0.1
     */
    public @NotNull User getUser(@NotNull Player player) {
        return iridiumSkyblock.getUserManager().getUser(player);
    }

    /**
     * Finds an Island by its id.
     *
     * @param id The id of the Island
     * @return Optional with the Island, empty if there is none
     * @since 4.0.1
     */
    public @NotNull Optional<Island> getIslandById(int id) {
        return iridiumSkyblock.getIslandManager().getTeamViaID(id);
    }

    /**
     * Finds an Island by its name.
     *
     * @param name The name of the Island
     * @return Optional with the Island, empty if there is none
     * @since 4.0.1
     */
    public @NotNull Optional<Island> getIslandByName(@NotNull String name) {
        return iridiumSkyblock.getIslandManager().getTeamViaName(name);
    }

    /**
     * Gets an {@link Island} from a location.
     *
     * @param location The location you are looking at
     * @return Optional of the Island at the location, empty if there is none
     * @since 4.0.1
     */
    public @NotNull Optional<Island> getIslandViaLocation(@NotNull Location location) {
        return iridiumSkyblock.getIslandManager().getTeamViaLocation(location);
    }

    /**
     * Gets a permission object from name.
     *
     * @param permissionKey The permission key
     * @return the permission
     * @since 4.0.1
     */
    public @NotNull Optional<Permission> getPermissions(@NotNull String permissionKey) {
        return Optional.ofNullable(iridiumSkyblock.getPermissionList().get(permissionKey));
    }

    /**
     * Gets a permission object from name.
     *
     * @param permissionType The permission key
     * @return the permission
     * @since 4.0.1
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
     * @return true if the permission is allowed
     * @since 4.0.1
     */
    public boolean getIslandPermission(@NotNull Island island, @NotNull User user, @NotNull Permission permission) {
        return iridiumSkyblock.getIslandManager().getTeamPermission(island, user, permission.toString());
    }

    /**
     * Gets whether a permission is allowed or denied.
     *
     * @param island     The specified Island
     * @param user       The specified user
     * @param permissionType The specified permission type
     * @return true if the permission is allowed
     * @since 4.0.1
     */
    public boolean getIslandPermission(@NotNull Island island, @NotNull User user, @NotNull PermissionType permissionType) {
        return iridiumSkyblock.getIslandManager().getTeamPermission(island, user, permissionType);
    }



    /**
     * Gets all entities on an Island.
     *
     * @param island The specified Island
     * @return all entities on that Island
     * @since 4.0.1
     */
    public @NotNull CompletableFuture<List<Entity>> getEntities(@NotNull Island island, @NotNull World... worlds) {
        return iridiumSkyblock.getIslandManager().getEntities(island, worlds);
    }

    /**
     * Gets a list of Islands sorted by SortType.
     *
     * @param sortType How we are sorting the Islands
     * @return sorted list of all Islands
     * @since 4.0.1
     */
    public @NotNull List<Island> getIslands(Comparator<? super Island> sortType) {
        List<Island> islands = iridiumSkyblock.getIslandManager().getTeams();
        islands.sort(sortType);
        return islands;
    }

    /**
     * Returns the overworld.
     *
     * @return the main Skyblock {@link World}, might be null if some third-party plugin deleted it
     * @since 4.0.1
     */
    public @Nullable World getWorld() {
        return iridiumSkyblock.getIslandManager().getWorld(World.Environment.NORMAL);
    }

    /**
     * Returns the nether world.
     *
     * @return the nether Skyblock {@link World}, might be null if some third-party plugin deleted it
     * @since 4.0.1
     */
    public @Nullable World getNetherWorld() {
        return iridiumSkyblock.getIslandManager().getWorld(World.Environment.NETHER);
    }

    /**
     * Returns the end world.
     *
     * @return the nether Skyblock {@link World}, might be null if some third-party plugin deleted it
     * @since 4.0.1
     */
    public @Nullable World getEndWorld() {
        return iridiumSkyblock.getIslandManager().getWorld(World.Environment.THE_END);
    }

    /**
     * Returns whether the specified world is from IridiumSkyblock.
     *
     * @param world The world that should be checked
     * @return true if it is a world used by IridiumSkyblock
     * @since 4.0.1
     */
    public boolean isIslandWorld(@NotNull World world) {
        return Objects.equals(IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.NORMAL), world)
                || Objects.equals(IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.NETHER), world)
                || Objects.equals(IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.THE_END), world);
    }

    /**
     * Returns if this is the overworld of IridiumSkyblock.
     *
     * @param world The world that should be checked
     * @return true if this is the overworld of IridiumSkyblock
     * @since 4.0.1
     */

    public boolean isIslandOverWorld(@NotNull World world) {
        return Objects.equals(IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.NORMAL), world);
    }

    /**
     * Returns if this is the nether world of IridiumSkyblock.
     *
     * @param world The world that should be checked
     * @return true if this is the nether world of IridiumSkyblock
     * @since 4.0.1
     */
    public boolean isIslandNether(@NotNull World world) {
        return Objects.equals(IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.NETHER), world);
    }

    /**
     * Returns if this is the end world of IridiumSkyblock.
     *
     * @param world The world that should be checked
     * @return true if this is the end world of IridiumSkyblock
     * @since 4.0.1
     */
    public boolean isIslandEnd(@NotNull World world) {
        return Objects.equals(IridiumSkyblock.getInstance().getIslandManager().getWorld(World.Environment.THE_END), world);
    }

    /**
     * Returns whether the specified player can visit the provided Island.<p>
     *
     * @param user the user
     * @param island the Island
     * @return true if the user can visit the Island
     * @since 4.0.1
     */
    /**
    public boolean canVisitIsland(@NotNull User user, @NotNull Island island) {
        if (IridiumSkyblock.getInstance().getIslandManager().isVisitable()) {
            return island.isVisitable() ||
                    user.isBypassing() ||
                    user.getPlayer().hasPermission("iridiumskyblock.visitbypass") ||
                    IridiumSkyblock.getInstance().getIslandManager().getTeamMembers(island).contains(user) ||
                    IridiumSkyblock.getInstance().getIslandManager().getTeamTrust(island, user).isPresent();
        } else return false;
    }*/
}
