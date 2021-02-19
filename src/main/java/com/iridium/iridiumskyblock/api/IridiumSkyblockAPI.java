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

    @NotNull
    public User getUser(OfflinePlayer offlinePlayer) {
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

    public Configuration getConfiguration() {
        return iridiumSkyblock.getConfiguration();
    }

    public World getWorld() {
        return Bukkit.getWorld(iridiumSkyblock.getConfiguration().worldName);
    }

    public World getNetherWorld() {
        return Bukkit.getWorld(iridiumSkyblock.getConfiguration().netherWorldName);
    }

    public static IridiumSkyblockAPI getInstance() {
        return instance;
    }
}
