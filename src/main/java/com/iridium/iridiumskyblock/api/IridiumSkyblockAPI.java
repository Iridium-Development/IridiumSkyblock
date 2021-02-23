package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Configuration;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * General api for IridiumSkyblock.
 * It is accessible via {@link IridiumSkyblockAPI#getInstance()}.
 */
public class IridiumSkyblockAPI {

    private static IridiumSkyblockAPI instance;
    private final IridiumSkyblock iridiumSkyblock;

    /**
     * Constructor for api initialization.
     *
     * @param iridiumSkyblock The instance of the {@link IridiumSkyblock} class
     */
    private IridiumSkyblockAPI(IridiumSkyblock iridiumSkyblock) {
        this.iridiumSkyblock = iridiumSkyblock;
    }

    /**
     * Gets a {@link User}'s info. Creates one if he doesn't exist.
     *
     * @param offlinePlayer The player who's data should be fetched
     * @return The user data
     * @since 3.0.0
     */
    public @NotNull User getUser(@NotNull OfflinePlayer offlinePlayer) {
        Optional<User> userOptional = iridiumSkyblock.getDatabaseManager().getUserByUUID(offlinePlayer.getUniqueId());
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            Optional<String> name = Optional.ofNullable(offlinePlayer.getName());
            User user = new User(offlinePlayer.getUniqueId(), name.orElse(""));
            iridiumSkyblock.getDatabaseManager().getUserList().add(user);
            return user;
        }
    }

    /**
     * Gets an {@link Island} from a location.
     *
     * @param location The location you are looking at
     * @return Optional of the island at the location, empty if there is none
     * @since 3.0.0
     */
    public @NotNull Optional<Island> getIslandViaLocation(@NotNull Location location) {
        return iridiumSkyblock.getDatabaseManager().getIslandList().stream().filter(island -> {
            Location pos1 = island.getPos1(location.getWorld());
            Location pos2 = island.getPos2(location.getWorld());
            return pos1.getX() <= location.getX() && pos2.getX() >= location.getX() && pos1.getX() <= location.getZ() && pos2.getZ() >= location.getZ();
        }).findFirst();
    }

    /**
     * Returns the overworld as specified in {@link Configuration#worldName}.
     *
     * @return The main skyblock {@link World}, might be null if some third-party plugin deleted it
     * @since 3.0.0
     */
    public @Nullable World getWorld() {
        return Bukkit.getWorld(iridiumSkyblock.getConfiguration().worldName);
    }

    /**
     * Returns the overworld as specified in {@link Configuration#netherWorldName}.
     *
     * @return The nether skyblock {@link World}, might be null if some third-party plugin deleted it
     * @since 3.0.0
     */
    public @Nullable World getNetherWorld() {
        return Bukkit.getWorld(iridiumSkyblock.getConfiguration().netherWorldName);
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
     * Initializes the api. For internal use only.
     *
     * @param iridiumSkyblock The {@link IridiumSkyblock} instance used by the plugin
     */
    public static synchronized void initializeAPI(IridiumSkyblock iridiumSkyblock) {
        if (instance == null) {
            instance = new IridiumSkyblockAPI(iridiumSkyblock);
        }
    }

}
