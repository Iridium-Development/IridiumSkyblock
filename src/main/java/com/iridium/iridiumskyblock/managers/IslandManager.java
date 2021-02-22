package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Class which handles islands and their worlds.
 */
public class IslandManager {

    private final IridiumSkyblock iridiumSkyblock;

    /**
     * The default constructor.
     *
     * @param iridiumSkyblock The instance of IridiumSkyblock used by this plugin
     */
    public IslandManager(IridiumSkyblock iridiumSkyblock) {
        this.iridiumSkyblock = iridiumSkyblock;
    }

    /**
     * Creates a new world using the current skyblock generator.
     *
     * @param env  The world's Environment
     * @param name The World's Name
     */
    public void createWorld(World.Environment env, String name) {
        new WorldCreator(name)
                .generator(iridiumSkyblock.getDefaultWorldGenerator(name, null))
                .environment(env)
                .createWorld();
    }

    /**
     * Creates an Island for the specified player with the provided name.
     *
     * @param player    The owner of the Island
     * @param name      The name of the Island
     * @param schematic The schematic of the Island
     * @return The island being created
     */
    public @NotNull CompletableFuture<Island> createIsland(@NotNull Player player, @NotNull String name, @NotNull Schematics.SchematicConfig schematic) {
        CompletableFuture<Island> completableFuture = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(iridiumSkyblock, () -> {
            final User user = IridiumSkyblockAPI.getInstance().getUser(player);
            final Island island = iridiumSkyblock.getDatabaseManager().saveIsland(new Island(name, schematic));
            user.setIsland(island);

            // Paste schematic and then teleport the player (this needs to be done sync)
            Bukkit.getScheduler().runTask(iridiumSkyblock, () ->
                    iridiumSkyblock.getSchematicManager()
                            .pasteSchematic(island, IridiumSkyblockAPI.getInstance().getWorld(), schematic.overworld.schematicID)
                            .thenRun(() -> completableFuture.complete(island))
            );
        });
        return completableFuture;
    }

}
