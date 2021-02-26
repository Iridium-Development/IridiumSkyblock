package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandInvite;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class which handles islands and their worlds.
 */
public class IslandManager {
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

    public Optional<IslandInvite> getIslandInvite(@NotNull Island island, @NotNull User user) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandInviteList().stream().filter(islandInvite -> islandInvite.getUser().equals(user) && island.equals(islandInvite.getIsland().orElse(null))).findFirst();
    }

    /**
     * Teleports a player to the Island's home
     *
     * @param player The player we are teleporting
     * @param island The island we are teleporting them to
     */
    public void teleportHome(@NotNull Player player, @NotNull Island island) {
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportingHome.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        player.teleport(island.getHome());
    }

    /**
     * Creates an island for a specific Player and then teleports them to the island home
     *
     * @param player          The owner of the island
     * @param name            The name of  the island
     * @param schematicConfig The schematic of the island
     */
    public void makeIsland(Player player, String name, Schematics.SchematicConfig schematicConfig) {
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        if (user.getIsland().isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }
        if (IridiumSkyblock.getInstance().getDatabaseManager().getIslandByName(name).isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandWithNameAlreadyExists.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().creatingIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        createIsland(player, name, schematicConfig).thenAccept(island -> {
            player.teleport(island.getHome());
            IridiumSkyblock.getInstance().getNms().sendTitle(player, StringUtils.color(IridiumSkyblock.getInstance().getConfiguration().islandCreateTitle), 20, 40, 20);
            IridiumSkyblock.getInstance().getNms().sendSubTitle(player, StringUtils.color(IridiumSkyblock.getInstance().getConfiguration().islandCreateSubTitle), 20, 40, 20);
        });
    }

    /**
     * Creates an Island for the specified player with the provided name.
     *
     * @param player    The owner of the Island
     * @param name      The name of the Island
     * @param schematic The schematic of the Island
     * @return The island being created
     */
    private @NotNull CompletableFuture<Island> createIsland(@NotNull Player player, @NotNull String name, @NotNull Schematics.SchematicConfig schematic) {
        CompletableFuture<Island> completableFuture = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> {
            final User user = IridiumSkyblockAPI.getInstance().getUser(player);
            final Island island = IridiumSkyblock.getInstance().getDatabaseManager().registerIsland(new Island(name, schematic));
            user.setIsland(island);
            user.setIslandRank(IslandRank.OWNER);

            // Paste schematic and then teleport the player (this needs to be done sync)
            Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () ->
                    IridiumSkyblock.getInstance().getSchematicManager()
                            .pasteSchematic(island, IridiumSkyblockAPI.getInstance().getWorld(), schematic.overworld.schematicID, IridiumSkyblock.getInstance().getConfiguration().schematicPastingDelay)
                            .thenRun(() -> completableFuture.complete(island))
            );
        });
        return completableFuture;
    }

    /**
     * Deletes all blocks in the island and repastes the schematic
     *
     * @param island      The specified Island
     * @param schematicID The ID of the schematic we are pasting
     */
    public void regenerateIsland(@NotNull Island island, @NotNull String schematicID) {
        deleteIslandBlocks(island, IridiumSkyblockAPI.getInstance().getWorld(), 0).thenRun(() ->
                IridiumSkyblock.getInstance().getSchematicManager().pasteSchematic(island, IridiumSkyblockAPI.getInstance().getWorld(), schematicID, 0));
    }

    /**
     * Deletes all blocks in an island
     *
     * @param island The specified Island
     * @param world  The world we are deleting
     * @param delay  The delay between deleting each layer
     * @return A completableFuture for when its finished deleting the blocks
     */
    public CompletableFuture<Void> deleteIslandBlocks(@NotNull Island island, @NotNull World world, int delay) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        deleteIslandBlocks(island, world, world.getMaxHeight() - 1, completableFuture, delay);
        return completableFuture;
    }

    /**
     * Gets all chunks the island is in
     *
     * @param island The specified Island
     * @param world  The world
     * @return A list of Chunks the island is in
     */
    private List<Chunk> getIslandChunks(@NotNull Island island, @NotNull World world) {
        List<Chunk> chunks = new ArrayList<>();

        int minX = island.getPos1(world).getChunk().getX();
        int minZ = island.getPos1(world).getChunk().getZ();
        int maxX = island.getPos2(world).getChunk().getX();
        int maxZ = island.getPos2(world).getChunk().getZ();

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                chunks.add(world.getChunkAt(x, z));
            }
        }
        return chunks;
    }

    /**
     * Deletes all blocks in an Island.
     * Starts at the top and works down to y = 0
     *
     * @param island            The specified Island
     * @param world             The specified World
     * @param y                 The current y level
     * @param completableFuture The completable future to be completed when task is finished
     * @param delay             The delay in ticks between each layer
     */
    private void deleteIslandBlocks(@NotNull Island island, @NotNull World world, int y, CompletableFuture<Void> completableFuture, int delay) {
        Location pos1 = island.getPos1(world);
        Location pos2 = island.getPos2(world);
        for (int x = pos1.getBlockX(); x <= pos2.getBlockX(); x++) {
            for (int z = pos1.getBlockZ(); z <= pos2.getBlockZ(); z++) {
                if (world.getBlockAt(x, y, z).getType() != Material.AIR)
                    IridiumSkyblock.getInstance().getNms().setBlockFast(world, x, y, z, 0, (byte) 0, false);
            }
        }
        if (y == 0) {
            completableFuture.complete(null);
            getIslandChunks(island, world).forEach(chunk -> IridiumSkyblock.getInstance().getNms().sendChunk(world.getPlayers(), chunk));
        } else {
            if (delay < 1) {
                deleteIslandBlocks(island, world, y - 1, completableFuture, delay);
            } else {
                Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> deleteIslandBlocks(island, world, y - 1, completableFuture, delay), delay);
            }
        }
    }

    /**
     * Deletes the specified Island
     *
     * @param island The specified Island
     */
    public void deleteIsland(@NotNull Island island) {
        deleteIslandBlocks(island, IridiumSkyblockAPI.getInstance().getWorld(), 3);
        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> IridiumSkyblock.getInstance().getDatabaseManager().deleteIsland(island));
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandMembers(island).forEach(user -> {
            Player player = Bukkit.getPlayer(user.getUuid());
            if (player != null) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandDeleted.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                if (island.isInIsland(player.getLocation())) {
                    PlayerUtils.teleportSpawn(player);
                }
            }
        });
    }

}
