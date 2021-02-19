package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Configuration;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class IridiumSkyblockAPI {
    private static IridiumSkyblockAPI instance;
    private final IridiumSkyblock iridiumSkyblock;

    public IridiumSkyblockAPI(IridiumSkyblock iridiumSkyblock) {
        instance = this;
        this.iridiumSkyblock = iridiumSkyblock;
    }

    /**
     * Gets a User's info
     *
     * @param offlinePlayer The player who's data we are fetching
     * @return The user data or creates one if not found
     * @since 3.0.0
     */
    @NotNull
    public User getUser(@NotNull OfflinePlayer offlinePlayer) {
        Optional<User> userOptional = iridiumSkyblock.getDatabaseManager().getUserByUUID(offlinePlayer.getUniqueId());
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            Optional<String> name = Optional.ofNullable(offlinePlayer.getName());
            User user = new User(offlinePlayer.getUniqueId(), name.orElse(""));
            iridiumSkyblock.getDatabaseManager().getUsers().add(user);
            return user;
        }
    }

    /**
     * Gets the plugins Configuration file
     *
     * @return The plugins Configuration file
     */
    public Configuration getConfiguration() {
        return iridiumSkyblock.getConfiguration();
    }

    /**
     * Gets the main skyblock world
     *
     * @return The main skyblock world
     */
    public World getWorld() {
        return Bukkit.getWorld(iridiumSkyblock.getConfiguration().worldName);
    }

    /**
     * Gets the nether skyblock world
     *
     * @return The nether skyblock world
     */
    public World getNetherWorld() {
        return Bukkit.getWorld(iridiumSkyblock.getConfiguration().netherWorldName);
    }

    public static IridiumSkyblockAPI getInstance() {
        return instance;
    }
}
