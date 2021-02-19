package com.iridium.iridiumskyblock.api;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class IridiumSkyblockAPI {
    private static IridiumSkyblockAPI instance;
    private final IridiumSkyblock iridiumSkyblock;

    public IridiumSkyblockAPI(IridiumSkyblock iridiumSkyblock) {
        instance = this;
        this.iridiumSkyblock = iridiumSkyblock;
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
