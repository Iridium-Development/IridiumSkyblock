package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class IslandManager {
    private final IridiumSkyblock iridiumSkyblock;

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
        CompletableFuture<Island> completableFuture = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(iridiumSkyblock, () -> {
            final User user = IridiumSkyblockAPI.getInstance().getUser(player);
            final Island island = iridiumSkyblock.getDatabaseManager().saveIsland(new Island(name));
            user.setIsland(island);
            //Paste schematic and then teleport the player (this needs to be done sync)
            Bukkit.getScheduler().runTask(iridiumSkyblock, () ->
                    iridiumSkyblock.getSchematicManager().pasteSchematic(island, IridiumSkyblockAPI.getInstance().getWorld(), "test").thenRun(() ->
                            completableFuture.complete(island)
                    )
            );
        });
        return completableFuture;
    }
}
