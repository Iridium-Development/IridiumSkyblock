package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
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

    /**
     * The default constructor.
     */
    public IslandManager() {
    }

    /**
     * Creates a new world using the current skyblock generator.
     *
     * @param env  The world's Environment
     * @param name The World's Name
     */
    public void createWorld(World.Environment env, String name) {
        new WorldCreator(name)
                .generator(IridiumSkyblock.getInstance().getDefaultWorldGenerator(name, null))
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
        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> {
            final User user = IridiumSkyblockAPI.getInstance().getUser(player);
            final Island island = IridiumSkyblock.getInstance().getDatabaseManager().saveIsland(new Island(name, schematic));
            user.setIsland(island);

            // Paste schematic and then teleport the player (this needs to be done sync)
            Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () ->
                    IridiumSkyblock.getInstance().getSchematicManager()
                            .pasteSchematic(island, IridiumSkyblockAPI.getInstance().getWorld(), schematic.overworld.schematicID)
                            .thenRun(() -> completableFuture.complete(island))
            );
        });
        return completableFuture;
    }

    /**
     * Deletes the specified Island
     *
     * @param island The specified Island
     */
    public void deleteIsland(@NotNull Island island) {
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandMembers(island).forEach(user -> {
            Player player = Bukkit.getPlayer(user.getUuid());
            if (player != null) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandDeleted.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                if (island.isInIsland(player.getLocation())) {
                    PlayerUtils.teleportSpawn(player);
                }
            }
        });
        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> IridiumSkyblock.getInstance().getDatabaseManager().deleteIsland(island));
    }

}
