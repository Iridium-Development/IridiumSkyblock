package com.iridium.iridiumskyblock;

import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class IslandManager {
    private IridiumSkyblock iridiumSkyblock;

    public IslandManager(IridiumSkyblock iridiumSkyblock) {
        this.iridiumSkyblock = iridiumSkyblock;
    }

    /**
     * Creates a new world using the current skyblock generator
     *
     * @param env  The world's Environment
     * @param name The World's Name
     */

    public void makeWorld(World.Environment env, String name) {
        WorldCreator wc = new WorldCreator(name);
        wc.type(WorldType.FLAT);
        wc.generateStructures(false);
        wc.generator(iridiumSkyblock.getDefaultWorldGenerator(name, null));
        wc.environment(env);
        wc.createWorld();
    }

    /**
     * Creates an Island
     *
     * @param player The owner of the Island
     * @param name   The name of the Island
     * @return The island being created
     */

    public @NotNull CompletableFuture<Island> createIsland(@NotNull Player player, @NotNull String name) {
        return CompletableFuture.supplyAsync(() -> {
            User user = IridiumSkyblockAPI.getInstance().getUser(player);
            Island island = iridiumSkyblock.getDatabaseManager().saveIsland(new Island(name));
            user.setIsland(island);
            return island;
        });
    }
}
