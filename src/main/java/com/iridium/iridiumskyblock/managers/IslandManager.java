package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumcore.dependencies.paperlib.PaperLib;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.Permission;
import com.iridium.iridiumskyblock.api.IslandCreateEvent;
import com.iridium.iridiumskyblock.api.IslandDeleteEvent;
import com.iridium.iridiumskyblock.api.IslandRegenEvent;
import com.iridium.iridiumskyblock.bank.BankItem;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.*;
import com.iridium.iridiumskyblock.generators.OceanGenerator;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Class which handles islands and their worlds.
 */
public class IslandManager {

    /**
     * Creates a new world using the current skyblock generator.
     *
     * @param environment The world's Environment
     * @param name        The World's Name
     */
    public void createWorld(World.Environment environment, String name) {
        new WorldCreator(name)
                .generator(IridiumSkyblock.getInstance().getDefaultWorldGenerator(name, null))
                .environment(environment)
                .createWorld();
    }

    /**
     * Returns the invite for a User to an Island.
     * Empty if there is none.
     *
     * @param island The island to which the user might have been invited to
     * @param user   The user which might have been invited
     * @return The invite of the user to this island, might be empty
     */
    public Optional<IslandInvite> getIslandInvite(@NotNull Island island, @NotNull User user) {
        List<IslandInvite> islandInvites = IridiumSkyblock.getInstance().getDatabaseManager().getIslandInviteTableManager().getEntries(island);
        return islandInvites.stream().filter(islandInvite -> islandInvite.getUser().equals(user)).findFirst();
    }

    /**
     * Teleports a player to the Island's home
     *
     * @param player The player we are teleporting
     * @param island The island we are teleporting them to
     * @param delay  How long the player should stand still for before teleporting
     */
    public void teleportHome(@NotNull Player player, @NotNull Island island, int delay) {
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportingHome.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        if (delay < 1) {
            teleportHome(player, island);
            return;
        }
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> {
            teleportHome(player, island);
            IridiumSkyblock.getInstance().getUserManager().getUser(player).setTeleportingTask(null);
        }, 20L * delay);
        IridiumSkyblock.getInstance().getUserManager().getUser(player).setTeleportingTask(bukkitTask);
    }

    /**
     * Teleports a player to the Island's home
     *
     * @param player The player we are teleporting
     * @param island The island we are teleporting them to
     */
    private void teleportHome(@NotNull Player player, @NotNull Island island) {
        player.setFallDistance(0);
        PaperLib.teleportAsync(player, LocationUtils.getSafeLocation(island.getHome(), island));
    }

    /**
     * Teleports a player to an Island Warp
     *
     * @param player     The player we are teleporting
     * @param islandWarp The warp we are teleporting them to
     * @param delay      How long the player should stand still for before teleporting
     */
    public void teleportWarp(@NotNull Player player, @NotNull IslandWarp islandWarp, int delay) {
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportingWarp
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
                .replace("%name%", islandWarp.getName())
        );
        if (delay < 1) {
            teleportWarp(player, islandWarp);
            return;
        }
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> {
            teleportWarp(player, islandWarp);
            IridiumSkyblock.getInstance().getUserManager().getUser(player).setTeleportingTask(null);
        }, 20L * delay);
        IridiumSkyblock.getInstance().getUserManager().getUser(player).setTeleportingTask(bukkitTask);
    }

    /**
     * Teleports a player to an Island Warp
     *
     * @param player     The player we are teleporting
     * @param islandWarp The warp we are teleporting them to
     */
    private void teleportWarp(@NotNull Player player, @NotNull IslandWarp islandWarp) {
        player.setFallDistance(0);
        PaperLib.teleportAsync(player, LocationUtils.getSafeLocation(islandWarp.getLocation(), islandWarp.getIsland().orElse(null)));
    }

    /**
     * Creates an island for a specific Player and then teleports them to the island home.
     *
     * @param player          The owner of the island
     * @param name            The name of  the island
     * @param schematicConfig The schematic of the island
     */
    public void makeIsland(Player player, String name, Schematics.SchematicConfig schematicConfig) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        if (user.getIsland().isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }

        if (getIslandByName(name).isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandWithNameAlreadyExists.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }

        IslandCreateEvent islandCreateEvent = new IslandCreateEvent(user, name);
        Bukkit.getPluginManager().callEvent(islandCreateEvent);
        if (islandCreateEvent.isCancelled()) return;

        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().creatingIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        createIsland(player, name, schematicConfig).thenAccept(island ->
                PaperLib.teleportAsync(player, island.getHome()).thenRun(() -> {
                    IridiumSkyblock.getInstance().getNms().sendTitle(player, StringUtils.color(IridiumSkyblock.getInstance().getConfiguration().islandCreateTitle), 20, 40, 20);
                    IridiumSkyblock.getInstance().getNms().sendSubTitle(player, StringUtils.color(IridiumSkyblock.getInstance().getConfiguration().islandCreateSubTitle), 20, 40, 20);
                })
        );
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
            final User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
            final Island island = IridiumSkyblock.getInstance().getDatabaseManager().registerIsland(new Island(name, schematic));
            user.setIsland(island);
            user.setIslandRank(IslandRank.OWNER);

            // Paste schematic and then teleport the player (this needs to be done sync)
            Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () ->
                    pasteSchematic(island, schematic).thenRun(() -> {
                        teleportHome(player, island);
                        completableFuture.complete(island);
                    })
            );
        });
        return completableFuture;
    }

    /**
     * Deletes all blocks in the island and re-pastes the schematic.
     *
     * @param island          The specified Island
     * @param schematicConfig The schematic we are pasting
     */
    public void regenerateIsland(@NotNull Island island, User user, @NotNull Schematics.SchematicConfig schematicConfig) {
        IslandRegenEvent islandRegenEvent = new IslandRegenEvent(island, user, schematicConfig);
        Bukkit.getPluginManager().callEvent(islandRegenEvent);
        if (islandRegenEvent.isCancelled()) return;

        if (IridiumSkyblock.getInstance().getChunkGenerator() instanceof OceanGenerator) {
            OceanGenerator oceanGenerator = (OceanGenerator) IridiumSkyblock.getInstance().getChunkGenerator();
            for (int x = island.getPos1(getWorld()).getBlockX(); x <= island.getPos2(getWorld()).getBlockX(); x++) {
                for (int z = island.getPos1(getWorld()).getBlockZ(); z <= island.getPos2(getWorld()).getBlockZ(); z++) {
                    oceanGenerator.generateWater(getWorld(), x, z);
                    oceanGenerator.generateWater(getNetherWorld(), x, z);
                    oceanGenerator.generateWater(getEndWorld(), x, z);
                }
            }
        } else {
            deleteIslandBlocks(island, getWorld(), 0).join();
            deleteIslandBlocks(island, getNetherWorld(), 0).join();
            deleteIslandBlocks(island, getEndWorld(), 0).join();
        }

        getIslandMembers(island).forEach(u -> {
            Player player = Bukkit.getPlayer(u.getUuid());
            if (player != null) {
                if (IridiumSkyblock.getInstance().getConfiguration().clearInventoryOnRegen) player.getInventory().clear();
                if (IridiumSkyblock.getInstance().getConfiguration().clearEnderChestOnRegen) player.getEnderChest().clear();
            }
        });

        pasteSchematic(island, schematicConfig).thenRun(() -> {

            Location islandHome = island.getCenter(IridiumSkyblock.getInstance().getIslandManager().getWorld()).add(schematicConfig.xHome, schematicConfig.yHome, schematicConfig.zHome);
            islandHome.setYaw(schematicConfig.yawHome);
            island.setHome(islandHome);

            getEntities(island, getWorld(), getNetherWorld(), getEndWorld()).thenAccept(entities -> Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> {
                        for (Entity entity : entities) {
                            if (entity instanceof Player) {
                                teleportHome((Player) entity, island, 0);
                            } else {
                                entity.remove();
                            }
                        }
                    })
            );
        });
    }

    private CompletableFuture<Void> pasteSchematic(@NotNull Island island, @NotNull Schematics.SchematicConfig schematicConfig) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        IridiumSkyblock.getInstance().getSchematicManager().pasteSchematic(island, getWorld(), schematicConfig.overworld.schematicID, IridiumSkyblock.getInstance().getConfiguration().schematicPastingDelay).thenRun(() ->
                IridiumSkyblock.getInstance().getSchematicManager().pasteSchematic(island, getNetherWorld(), schematicConfig.nether.schematicID, IridiumSkyblock.getInstance().getConfiguration().schematicPastingDelay).thenRun(() ->
                        IridiumSkyblock.getInstance().getSchematicManager().pasteSchematic(island, getEndWorld(), schematicConfig.end.schematicID, IridiumSkyblock.getInstance().getConfiguration().schematicPastingDelay).thenRun(() ->
                                completableFuture.complete(null)
                        )
                )
        );
        return completableFuture;
    }

    /**
     * Deletes all blocks in an island.
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
     * Gets all chunks the island is in.
     *
     * @param island The specified Island
     * @param world  The world
     * @return A list of Chunks the island is in
     */
    private CompletableFuture<List<Chunk>> getIslandChunks(@NotNull Island island, @NotNull World world) {
        return CompletableFuture.supplyAsync(() -> {
            List<CompletableFuture<Chunk>> chunks = new ArrayList<>();

            int minX = island.getPos1(world).getChunk().getX();
            int minZ = island.getPos1(world).getChunk().getZ();
            int maxX = island.getPos2(world).getChunk().getX();
            int maxZ = island.getPos2(world).getChunk().getZ();

            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    chunks.add(PaperLib.getChunkAtAsync(world, x, z, true));
                }
            }
            return chunks.stream().map(CompletableFuture::join).collect(Collectors.toList());
        });
    }

    /**
     * Gets a list of Users from an island.
     *
     * @param island The specified Island
     * @return A list of users
     */
    public @NotNull List<User> getIslandMembers(@NotNull Island island) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getUserTableManager().getEntries(island);
    }

    /**
     * Finds an Island by its id.
     *
     * @param id The id of the island
     * @return An Optional with the Island, empty if there is none
     */
    public Optional<Island> getIslandById(int id) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getIsland(id);
    }

    /**
     * Finds an Island by its name.
     *
     * @param name The name of the island
     * @return An Optional with the Island, empty if there is none
     */
    public Optional<Island> getIslandByName(String name) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries().stream().filter(island -> island.getName().equalsIgnoreCase(name)).findFirst();
    }

    /**
     * Gets an {@link Island} from a location.
     *
     * @param location The location you are looking at
     * @return Optional of the island at the location, empty if there is none
     */
    public @NotNull Optional<Island> getIslandViaLocation(@NotNull Location location) {
        World world = location.getWorld();
        if (isIslandWorld(world)) {
            return IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries().stream().filter(island -> island.isInIsland(location)).findFirst();
        }
        return Optional.empty();
    }

    /**
     * Gets whether an IslandRank has the permission on the provided island.
     *
     * @param island     The specified Island
     * @param islandRank The specified Rank
     * @param permission The specified Permission
     * @return If the permission is allowed
     */
    public boolean getIslandPermission(@NotNull Island island, @NotNull IslandRank islandRank, @NotNull Permission permission, @NotNull String key) {
        List<IslandPermission> islandPermissions =
                IridiumSkyblock.getInstance().getDatabaseManager().getIslandPermissionTableManager().getEntries(island);

        Optional<IslandPermission> optionalIslandPermission =
                islandPermissions.stream().filter(isPermission -> isPermission.getPermission().equalsIgnoreCase(key) && isPermission.getRank().equals(islandRank)).findFirst();
        return optionalIslandPermission.map(IslandPermission::isAllowed).orElseGet(() -> islandRank.getLevel() >= permission.getDefaultRank().getLevel());
    }

    /**
     * Gets weather a permission is allowed or denied.
     *
     * @param island     The specified Island
     * @param user       The Specified User
     * @param permission The Specified permission
     * @return The the permission is allowed
     */
    public boolean getIslandPermission(@NotNull Island island, @NotNull User user, @NotNull Permission permission, @NotNull String key) {
        IslandRank islandRank = island.equals(user.getIsland().orElse(null)) ? user.getIslandRank() : IslandRank.VISITOR;
        if (IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager().getEntries(island).stream().anyMatch(islandTrusted ->
                islandTrusted.getUser().equals(user))
        ) {
            islandRank = IslandRank.MEMBER;
        }
        return getIslandPermission(island, islandRank, permission, key) || user.isBypass();
    }

    /**
     * Gets an Island's bank from BankItem.
     *
     * @param island   The specified Island
     * @param bankItem The BankItem we are getting
     * @return the IslandBank
     */
    public IslandBank getIslandBank(@NotNull Island island, @NotNull BankItem bankItem) {
        Optional<IslandBank> optionalIslandBank =
                IridiumSkyblock.getInstance().getDatabaseManager().getIslandBankTableManager().getEntries(island).stream().filter(islandBank ->
                        islandBank.getBankItem().equalsIgnoreCase(bankItem.getName())
                ).findFirst();
        if (optionalIslandBank.isPresent()) {
            return optionalIslandBank.get();
        } else {
            IslandBank islandBank = new IslandBank(island, bankItem.getName(), 0);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandBankTableManager().addEntry(islandBank);
            return islandBank;
        }
    }

    /**
     * Gets the IslandBlock for a specific island and material.
     *
     * @param island   The specified Island
     * @param material The specified Material
     * @return The IslandBlock
     */
    public IslandBlocks getIslandBlock(@NotNull Island island, @NotNull XMaterial material) {
        Optional<IslandBlocks> islandBlocksOptional = IridiumSkyblock.getInstance().getDatabaseManager().getIslandBlocksTableManager().getEntries(island).stream().filter(islandBlocks ->
                material.equals(islandBlocks.getMaterial())
        ).findFirst();
        if (islandBlocksOptional.isPresent()) {
            return islandBlocksOptional.get();
        }
        IslandBlocks islandBlocks = new IslandBlocks(island, material);
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandBlocksTableManager().addEntry(islandBlocks);
        return islandBlocks;
    }

    /**
     * Gets the IslandBlock for a specific island and material.
     *
     * @param island      The specified Island
     * @param spawnerType The specified spawner type
     * @return The IslandBlock
     */
    public IslandSpawners getIslandSpawners(@NotNull Island island, @NotNull EntityType spawnerType) {
        Optional<IslandSpawners> islandSpawnersOptional = IridiumSkyblock.getInstance().getDatabaseManager().getIslandSpawnersTableManager().getEntries(island).stream().filter(islandSpawners ->
                spawnerType.equals(islandSpawners.getSpawnerType())
        ).findFirst();
        if (islandSpawnersOptional.isPresent()) {
            return islandSpawnersOptional.get();
        }
        IslandSpawners islandSpawners = new IslandSpawners(island, spawnerType);
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandSpawnersTableManager().addEntry(islandSpawners);
        return islandSpawners;
    }

    /**
     * Sets whether a permission is allowed or denied for the specified IslandRank.
     *
     * @param island     The specified Island
     * @param islandRank The specified Rank
     * @param permission The specified Permission
     * @param allowed    If the permission is allowed
     */
    public void setIslandPermission(
            @NotNull Island island, @NotNull IslandRank islandRank, @NotNull Permission permission, @NotNull String key, boolean allowed) {
        Optional<IslandPermission> islandPermission =
                IridiumSkyblock.getInstance().getDatabaseManager().getIslandPermissionTableManager().getEntries(island).stream().filter(isPermission ->
                        isPermission.getPermission().equalsIgnoreCase(key) && isPermission.getRank().equals(islandRank)
                ).findFirst();
        if (islandPermission.isPresent()) {
            islandPermission.get().setAllowed(allowed);
        } else {
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandPermissionTableManager().addEntry(new IslandPermission(island, key, islandRank, allowed));
        }
    }

    /**
     * Deletes all blocks in an Island.
     * Starts at the top and works down to y = 0.
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
                Block block = world.getBlockAt(x, y, z);
                if (block.getType() != Material.AIR) {
                    block.setType(Material.AIR, false);
                }
            }
        }

        if (y == 0) {
            completableFuture.complete(null);
            getIslandChunks(island, world).thenAccept(chunks -> chunks.forEach(chunk -> IridiumSkyblock.getInstance().getNms().sendChunk(world.getPlayers(), chunk)));
        } else {
            if (delay < 1) {
                deleteIslandBlocks(island, world, y - 1, completableFuture, delay);
            } else {
                Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> deleteIslandBlocks(island, world, y - 1, completableFuture, delay), delay);
            }
        }
    }

    /**
     * Deletes the specified Island.
     *
     * @param island The Island which should be deleted
     * @param user   The user who deleted the island
     */
    public void deleteIsland(@NotNull Island island, @Nullable User user) {
        IslandDeleteEvent islandDeleteEvent = new IslandDeleteEvent(island, user);
        Bukkit.getPluginManager().callEvent(islandDeleteEvent);
        if (islandDeleteEvent.isCancelled()) return;

        deleteIslandBlocks(island, getWorld(), 3);

        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().delete(island));
        getIslandMembers(island).forEach(u -> {
            Player player = Bukkit.getPlayer(u.getUuid());
            if (player != null) {
                if (IridiumSkyblock.getInstance().getConfiguration().clearInventoryOnRegen) player.getInventory().clear();
                if (IridiumSkyblock.getInstance().getConfiguration().clearEnderChestOnRegen) player.getEnderChest().clear();
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandDeleted.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        });
        getEntities(island, getWorld(), getEndWorld(), getNetherWorld()).thenAccept(entities ->
                Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () ->
                        entities.stream()
                                .filter(entity -> entity instanceof Player)
                                .map(entity -> (Player) entity)
                                .forEach(PlayerUtils::teleportSpawn)
                )
        );
    }

    /**
     * Gets an Island upgrade
     *
     * @param island  The specified Island
     * @param upgrade The specified Upgrade's name
     * @return The island Upgrade
     */
    public IslandUpgrade getIslandUpgrade(@NotNull Island island, @NotNull String upgrade) {
        Optional<IslandUpgrade> islandUpgrade =
                IridiumSkyblock.getInstance().getDatabaseManager().getIslandUpgradeTableManager().getEntries(island).stream().filter(isUpgrade ->
                        isUpgrade.getUpgrade().equalsIgnoreCase(upgrade)
                ).findFirst();
        if (islandUpgrade.isPresent()) {
            return islandUpgrade.get();
        } else {
            IslandUpgrade isUpgrade = new IslandUpgrade(island, upgrade);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandUpgradeTableManager().addEntry(isUpgrade);
            return isUpgrade;
        }
    }

    /**
     * Gets all island missions and creates them if they don't exist.
     *
     * @param island The specified Island
     * @return A list of Island Missions
     */
    public IslandMission getIslandMission(
            @NotNull Island island, @NotNull Mission mission, @NotNull String missionKey, int missionIndex) {
        Optional<IslandMission> islandMissionOptional =
                IridiumSkyblock.getInstance().getDatabaseManager().getIslandMissionTableManager().getEntries(island).stream().filter(isMission ->
                        isMission.getMissionName().equalsIgnoreCase(missionKey) && isMission.getMissionIndex() == missionIndex - 1
                ).findFirst();
        if (islandMissionOptional.isPresent()) {
            return islandMissionOptional.get();
        } else {
            IslandMission islandMission = new IslandMission(island, mission, missionKey, missionIndex - 1);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandMissionTableManager().addEntry(islandMission);
            return islandMission;
        }
    }

    /**
     * Gets the Islands daily missions.
     *
     * @param island The specified Island
     * @return The daily missions
     */
    public HashMap<String, Mission> getDailyIslandMissions(@NotNull Island island) {
        HashMap<String, Mission> missions = new HashMap<>();
        List<IslandMission> islandMissions =
                IridiumSkyblock.getInstance().getDatabaseManager().getIslandMissionTableManager().getEntries(island).stream().filter(islandMission -> islandMission.getType() == Mission.MissionType.DAILY).collect(Collectors.toList());

        if (islandMissions.isEmpty()) {
            Random random = new Random();
            List<String> missionList = IridiumSkyblock.getInstance().getMissionsList().keySet().stream().filter(mission -> IridiumSkyblock.getInstance().getMissionsList().get(mission).getMissionType() == Mission.MissionType.DAILY).collect(Collectors.toList());
            for (int i = 0; i < IridiumSkyblock.getInstance().getMissions().dailySlots.size(); i++) {
                String key = missionList.get(random.nextInt(missionList.size()));
                Mission mission = IridiumSkyblock.getInstance().getMissionsList().get(key);
                missionList.remove(key);

                for (int j = 0; j < mission.getMissions().size(); j++) {
                    IridiumSkyblock.getInstance().getDatabaseManager().getIslandMissionTableManager().addEntry(new IslandMission(island, mission, key, j));
                }

                missions.put(key, mission);
            }
        } else {
            islandMissions.forEach(islandMission -> {
                Mission mission = IridiumSkyblock.getInstance().getMissionsList().get(islandMission.getMissionName());
                if (mission != null) {
                    missions.put(islandMission.getMissionName(), mission);
                }
            });
        }

        return missions;
    }

    /**
     * Recalculates the island value of the specified island.
     *
     * @param island The specified Island
     */
    public void recalculateIsland(@NotNull Island island) {
        // Reset their value
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandBlocksTableManager().getEntries(island).forEach(islandBlocks -> islandBlocks.setAmount(0));
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandSpawnersTableManager().getEntries(island).forEach(islandSpawners -> islandSpawners.setAmount(0));

        // Calculate and set their new value
        getIslandChunks(island, IridiumSkyblock.getInstance().getIslandManager().getWorld()).thenAccept(chunks ->
                recalculateIsland(island, chunks)
        );
        getIslandChunks(island, IridiumSkyblock.getInstance().getIslandManager().getNetherWorld()).thenAccept(chunks ->
                recalculateIsland(island, chunks)
        );
        getIslandChunks(island, IridiumSkyblock.getInstance().getIslandManager().getEndWorld()).thenAccept(chunks ->
                recalculateIsland(island, chunks)
        );
    }

    /**
     * Recalculates the island async with specified ChunkSnapshots.
     *
     * @param island The specified Island
     * @param chunks The Island's Chunks
     */
    private void recalculateIsland(@NotNull Island island, @NotNull List<Chunk> chunks) {
        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () ->
                chunks.stream().map(chunk -> chunk.getChunkSnapshot(true, false, false)).forEach(chunk -> {
                            World world = Bukkit.getWorld(chunk.getWorldName());
                            int maxHeight = world == null ? 255 : world.getMaxHeight();
                            for (int x = 0; x < 16; x++) {
                                for (int z = 0; z < 16; z++) {
                                    if (island.isInIsland(x + (chunk.getX() * 16), z + (chunk.getZ() * 16))) {
                                        final int maxy = Math.min(maxHeight, chunk.getHighestBlockYAt(x, z));
                                        for (int y = 0; y <= maxy; y++) {
                                            XMaterial material = IridiumSkyblock.getInstance().getMultiVersion().getMaterialAtPosition(chunk, x, y, z);
                                            if (material.equals(XMaterial.AIR)) continue;

                                            IslandBlocks IslandBlocks = IridiumSkyblock.getInstance().getIslandManager().getIslandBlock(island, material);
                                            IslandBlocks.setAmount(IslandBlocks.getAmount() + 1);
                                        }
                                    }
                                }
                            }
                        }
                )
        );
        chunks.forEach(chunk -> {
            for (BlockState blockState : chunk.getTileEntities()) {
                if (!(blockState instanceof CreatureSpawner)) continue;
                CreatureSpawner creatureSpawner = (CreatureSpawner) blockState;
                IslandSpawners islandSpawners = IridiumSkyblock.getInstance().getIslandManager().getIslandSpawners(island, creatureSpawner.getSpawnedType());
                islandSpawners.setAmount(islandSpawners.getAmount() + 1);
            }
        });
    }

    /**
     * Increments a mission's data based on requirements.
     *
     * @param island      The island
     * @param missionData The mission data e.g. BREAK:COBBLESTONE
     * @param increment   The amount we are incrementing by
     */
    public void incrementMission(@NotNull Island island, @NotNull String missionData, int increment) {
        String[] missionConditions = missionData.toUpperCase().split(":");

        for (Map.Entry<String, Mission> entry : IridiumSkyblock.getInstance().getMissionsList().entrySet()) {
            boolean completedBefore = true;

            for (int i = 1; i <= entry.getValue().getMissions().size(); i++) {
                String[] conditions = entry.getValue().getMissions().get(i - 1).toUpperCase().split(":");
                // If the conditions are the same length (+1 because missionConditions doesn't include amount)
                if (missionConditions.length + 1 != conditions.length) break;

                // Check if this is a mission we want to increment
                boolean matches = matchesMission(missionConditions, conditions);
                if (!matches) continue;

                IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island, entry.getValue(),
                        entry.getKey(), i);
                String number = conditions[missionData.split(":").length];

                // Validate the required number for this condition
                if (number.matches("^[0-9]+$")) {
                    int amount = Integer.parseInt(number);
                    if (islandMission.getProgress() >= amount) break;
                    completedBefore = false;
                    islandMission.setProgress(Math.min(islandMission.getProgress() + increment, amount));
                } else {
                    IridiumSkyblock.getInstance().getLogger().warning("Unknown format " + entry.getValue().getMissions().get(i - 1));
                    IridiumSkyblock.getInstance().getLogger().warning(number + " Is not a number");
                }
            }

            // Check if this mission is now completed
            if (!completedBefore && hasCompletedMission(island, entry.getValue(), entry.getKey())) {
                island.getMembers().stream().map(user -> Bukkit.getPlayer(user.getUuid())).filter(Objects::nonNull).forEach(player -> {
                    entry.getValue().getMessage().stream().map(string -> StringUtils.color(string.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))).forEach(player::sendMessage);
                    entry.getValue().getCompleteSound().play(player);
                    IridiumSkyblock.getInstance().getDatabaseManager().getIslandRewardTableManager().addEntry(new IslandReward(island, entry.getValue().getReward()));
                });
            }
        }
    }

    /**
     * Gets time remaining on an island booster
     *
     * @param island  The specified Island
     * @param booster The booster name
     * @return The time remaining
     */
    public IslandBooster getIslandBooster(@NotNull Island island, @NotNull String booster) {
        List<IslandBooster> islandBoosters = IridiumSkyblock.getInstance().getDatabaseManager().getIslandBoosterTableManager().getEntries(island);
        Optional<IslandBooster> islandBooster =
                islandBoosters.stream().filter(isBooster -> isBooster.getBooster().equalsIgnoreCase(booster)).findFirst();
        if (islandBooster.isPresent()) {
            return islandBooster.get();
        } else {
            IslandBooster newBooster = new IslandBooster(island, booster);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandBoosterTableManager().addEntry(newBooster);
            return newBooster;
        }
    }

    /**
     * Checks if the given conditions are a part of the provided mission conditions.
     *
     * @param missionConditions The mission conditions
     * @param conditions        The conditions that should be checked
     * @return Whether or not the conditions are a part of the mission conditions
     */
    private boolean matchesMission(String[] missionConditions, String[] conditions) {
        boolean matches = true;
        for (int j = 0; j < missionConditions.length; j++) {
            if (!(conditions[j].equals(missionConditions[j]) || missionConditions[j].equals("ANY"))) {
                matches = false;
                break;
            }
        }
        return matches;
    }

    /**
     * Checks whether or not the Island has completed the provided mission.
     *
     * @param island  The Island which should be checked
     * @param mission The mission which should be checked
     * @param key     The key of the mission
     * @return Whether or not this mission has been completed
     */
    private boolean hasCompletedMission(@NotNull Island island, @NotNull Mission mission, @NotNull String key) {
        for (int i = 1; i <= mission.getMissions().size(); i++) {
            IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island, mission, key, i);
            String[] data = mission.getMissions().get(i - 1).toUpperCase().split(":");
            String number = data[data.length - 1];

            // Validate the required number for this condition
            if (number.matches("^[0-9]+$")) {
                int requiredAmount = Integer.parseInt(number);
                if (islandMission.getProgress() < requiredAmount) {
                    return false;
                }
            } else {
                IridiumSkyblock.getInstance().getLogger().warning("Unknown format " + mission.getMissions().get(i - 1));
                IridiumSkyblock.getInstance().getLogger().warning(number + " is not a number");
            }
        }
        return true;
    }

    /**
     * Gets all entities on an island
     *
     * @param island The specified Island
     * @return A list of all entities on that island
     */
    public CompletableFuture<List<Entity>> getEntities(@NotNull Island island, @NotNull World... worlds) {
        return CompletableFuture.supplyAsync(() -> {
            List<Entity> entities = new ArrayList<>();
            for (World world : worlds) {
                List<Chunk> chunks = getIslandChunks(island, world).join();
                for (Chunk chunk : chunks) {
                    for (Entity entity : chunk.getEntities()) {
                        if (island.isInIsland(entity.getLocation())) {
                            entities.add(entity);
                        }
                    }
                }
            }
            return entities;
        });
    }

    /**
     * Sends the island border to all players on the island
     *
     * @param island The specified Island
     */
    public void sendIslandBorder(@NotNull Island island) {
        getEntities(island, getWorld(), getNetherWorld(), getEndWorld()).thenAccept(entities -> {
            for (Entity entity : entities) {
                if (entity instanceof Player) {
                    PlayerUtils.sendBorder((Player) entity, island);
                }
            }
        });
    }

    /**
     * Gets a list of islands sorted by SortType
     *
     * @param sortType How we are sorting the islands
     * @return The sorted list of islands
     */
    public List<Island> getIslands(SortType sortType) {
        if (sortType == SortType.VALUE) {
            return IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries().stream().sorted(Comparator.comparing(Island::getValue).reversed()).collect(Collectors.toList());
        }
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries();
    }

    /**
     * Represents a way of ordering Islands.
     */
    public enum SortType {
        VALUE
    }

    /**
     * Returns the overworld.
     *
     * @return The main skyblock {@link World}, might be null if some third-party plugin deleted it
     * @since 3.0.0
     */
    public World getWorld() {
        return Bukkit.getWorld(IridiumSkyblock.getInstance().getConfiguration().worldName);
    }

    /**
     * Returns the NetherWorld
     *
     * @return The nether skyblock {@link World}, might be null if some third-party plugin deleted it
     * @since 3.0.0
     */
    public World getNetherWorld() {
        return Bukkit.getWorld(IridiumSkyblock.getInstance().getConfiguration().worldName + "_nether");
    }

    /**
     * Returns the NetherWorld
     *
     * @return The nether skyblock {@link World}, might be null if some third-party plugin deleted it
     * @since 3.0.0
     */
    public World getEndWorld() {
        return Bukkit.getWorld(IridiumSkyblock.getInstance().getConfiguration().worldName + "_the_end");
    }

    public boolean isIslandWorld(String world) {
        return isIslandWorld(Bukkit.getWorld(world));
    }
    public boolean isIslandWorld(World world) {
        return Objects.equals(world, getWorld()) || Objects.equals(world, getNetherWorld()) || Objects.equals(world, getEndWorld());
    }

}
