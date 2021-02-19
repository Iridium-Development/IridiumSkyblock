package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Configuration;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
     * Gets an Island from a location
     *
     * @param location The location you are looking at
     * @return The island at the location
     */
    public Optional<Island> getIslandViaLocation(@NotNull Location location) {
        return iridiumSkyblock.getDatabaseManager().getIslandList().stream().filter(island -> {
            Location pos1 = island.getPos1(location.getWorld());
            Location pos2 = island.getPos2(location.getWorld());
            return pos1.getX() <= location.getX() && pos2.getX() >= location.getX() && pos1.getX() <= location.getZ() && pos2.getZ() >= location.getZ();
        }).findFirst();
    }

    /**
     * @return The plugins Configuration file
     */
    public Configuration getConfiguration() {
        return iridiumSkyblock.getConfiguration();
    }

    /**
     * @return The main skyblock world
     */
    public World getWorld() {
        return Bukkit.getWorld(iridiumSkyblock.getConfiguration().worldName);
    }

    /**
     * @return The nether skyblock world
     */
    public World getNetherWorld() {
        return Bukkit.getWorld(iridiumSkyblock.getConfiguration().netherWorldName);
    }

    public static IridiumSkyblockAPI getInstance() {
        return instance;
    }
}
