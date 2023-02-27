package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.User;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

/**
 * Class which handles users.
 */
public class UserManager {

    /**
     * Gets a {@link User}'s info. Creates one if he doesn't exist.
     *
     * @param offlinePlayer The player who's data should be fetched
     * @return The user data
     */
    public @NotNull User getUser(@NotNull OfflinePlayer offlinePlayer) {
        Optional<User> userOptional = getUserByUUID(offlinePlayer.getUniqueId());
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            Optional<String> name = Optional.ofNullable(offlinePlayer.getName());
            User user = new User(offlinePlayer.getUniqueId(), name.orElse(""));
            IridiumSkyblock.getInstance().getDatabaseManager().getUserTableManager().addEntry(user);
            return user;
        }
    }

    /**
     * Gets a {@link User}'s info. Creates one if he doesn't exist.
     *
     * @param offlinePlayer The player who's data should be fetched
     * @return The user data
     */
    public @Nullable User getUser(@NotNull String name) {
        Optional<User> userOptional = getUserByName(name);
        if (userOptional.isPresent()) {
            return userOptional.orElse(null);
        } else {
            User user = new User(UUID.randomUUID(), name);
            Optional<org.bukkit.OfflinePlayer> maybePlayer = Arrays.stream(Bukkit.getOfflinePlayers()).filter(x->x.getName().equalsIgnoreCase(name)).findAny();
            org.bukkit.OfflinePlayer offlinePlayer =maybePlayer.orElseGet(()->{
                return org.bukkit.Bukkit.getOfflinePlayer(name);
            });
            if (offlinePlayer != null) {
                user = new User(offlinePlayer.getUniqueId(), name);
                IridiumSkyblock.getInstance().getDatabaseManager().getUserTableManager().addEntry(user);
            }
            return user;
        }
    }

    /**
     * Finds an User by his {@link UUID}.
     *
     * @param uuid The uuid of the onlyForPlayers
     * @return the User class of the onlyForPlayers
     */
    public Optional<User> getUserByUUID(@NotNull UUID uuid) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getUserTableManager().getUser(uuid);
    }

    /**
     * Finds an User by his {@link UUID}.
     *
     * @param uuid The uuid of the onlyForPlayers
     * @return the User class of the onlyForPlayers
     */
    public Optional<User> getUserByName(@NotNull String name) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getUserTableManager().getUser(name);
    }

}
