package com.iridium.iridiumskyblock.managers;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.dependencies.nbtapi.NBTCompound;
import com.iridium.iridiumcore.dependencies.nbtapi.NBTItem;
import com.iridium.iridiumcore.dependencies.paperlib.PaperLib;
import com.iridium.iridiumcore.dependencies.xseries.XBiome;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.*;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.api.IslandCreateEvent;
import com.iridium.iridiumskyblock.api.IslandDeleteEvent;
import com.iridium.iridiumskyblock.api.IslandRegenEvent;
import com.iridium.iridiumskyblock.bank.BankItem;
import com.iridium.iridiumskyblock.configs.Configuration.IslandRegenSettings;
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
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class which handles islands and their worlds.
 */
public class IslandManager {

    public final Cache<List<Island>> islandValueSortCache = new Cache<>(5000);
    public final Cache<List<Island>> islandLevelSortCache = new Cache<>(5000);

    public void clearIslandCache() {
        islandLevelSortCache.clearCache();
        islandValueSortCache.clearCache();
    }

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
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandInviteTableManager().getEntry(new IslandInvite(island, user, user));
    }

    /**
     * Sets an island's biome
     *
     * @param island The specified Island
     * @param xBiome The specified Biome
     */
    public void setIslandBiome(@NotNull Island island, @NotNull XBiome xBiome) {
        World.Environment environment = xBiome.getEnvironment();
        World world = switch (environment) {
            case NETHER -> getNetherWorld();
            case THE_END -> getEndWorld();
            default -> getWorld();
        };

        getIslandChunks(island, world).thenAccept(chunks -> {
            Location pos1 = island.getPos1(world);
            Location pos2 = island.getPos2(world);
            xBiome.setBiome(pos1, pos2).thenRun(() -> {
                for (Chunk chunk : chunks) {
                    chunk.getWorld().refreshChunk(chunk.getX(), chunk.getZ());
                }
            });
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    /**
     * Teleports a player to the Island's home
     *
     * @param player The player we are teleporting
     * @param island The island we are teleporting them to
     * @param delay  How long the player should stand still for before teleporting
     */
    public void teleportHome(@NotNull Player player, @NotNull Island island, int delay) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        if (isBannedOnIsland(island, user)) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youHaveBeenBanned
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                    .replace("%owner%", island.getOwner().getName())
                    .replace("%name%", island.getName())
            ));
            return;
        }

        boolean trusted = getIslandTrusted(island, user).isPresent();
        boolean inIsland = user.getIsland().map(Island::getId).orElse(0) == island.getId();
        // If the island is visitable, the user is in the island, the user is trusted or the user is bypassing teleport them
        if (island.isVisitable() || inIsland || trusted || user.isBypassing()) {
            if (inIsland) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportingHome.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            } else {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportingHomeOther.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix).replace("%owner%", island.getOwner().getName())));
            }
            if (delay < 1) {
                teleportHome(player, island);
                return;
            }
            BukkitTask bukkitTask = Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> {
                teleportHome(player, island);
                user.setTeleportingTask(null);
            }, 20L * delay);
            user.setTeleportingTask(bukkitTask);
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandIsPrivate.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
    }

    /**
     * Teleports a player to the Island's home
     *
     * @param player The player we are teleporting
     * @param island The island we are teleporting them to
     */
    private void teleportHome(@NotNull Player player, @NotNull Island island) {
        player.setFallDistance(0);
        PaperLib.teleportAsync(player, LocationUtils.getSafeLocation(island.getHome(), island), PlayerTeleportEvent.TeleportCause.PLUGIN);

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
        PaperLib.teleportAsync(player, LocationUtils.getSafeLocation(islandWarp.getLocation(), islandWarp.getIsland().orElse(null)), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    /**
     * Creates an island for a specific Player and then teleports them to the island home.
     *
     * @param player          The owner of the island
     * @param name            The name of  the island
     * @param schematicConfig The schematic of the island
     * @return True if the island has been created successfully
     */
    public boolean makeIsland(Player player, String name, Schematics.SchematicConfig schematicConfig) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        if (user.getIsland().isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (name != null && getIslandByName(name).isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandWithNameAlreadyExists.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        IslandCreateEvent islandCreateEvent = new IslandCreateEvent(user, user.getName(), schematicConfig);
        Bukkit.getPluginManager().callEvent(islandCreateEvent);
        if (islandCreateEvent.isCancelled()) return false;

        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().creatingIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        createIsland(player, user.getName(), islandCreateEvent.getSchematicConfig()).thenAccept(island -> {
            if (island == null) return;
            Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> {
                teleportHome(player, island);
                IridiumSkyblock.getInstance().getNms().sendTitle(player, IridiumSkyblock.getInstance().getConfiguration().islandCreateTitle, IridiumSkyblock.getInstance().getConfiguration().islandCreateSubTitle, 20, 40, 20);
            });
        });

        return true;
    }

    /**
     * Creates an Island for the specified player with the provided name.
     *
     * @param player    The owner of the Island
     * @param name      The name of the Island
     * @param schematic The schematic of the Island
     * @return The island being created
     */
    private @NotNull CompletableFuture<Island> createIsland(@NotNull Player player, String name, @NotNull Schematics.SchematicConfig schematic) {
        clearIslandCache();
        CompletableFuture<Island> completableFuture = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> {
            User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
            Island island = new Island(name, schematic); // ALTER TABLE islands DROP INDEX `name` / `sqlite_autoindex_islands_1`;

            IridiumSkyblock.getInstance().getDatabaseManager().registerIsland(island).join();
            // Add Logs Create
            IslandLog islandLog = new IslandLog(island, LogAction.CREATE_ISLAND, user, null, 0, "");
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().addEntry(islandLog);

            user.setIsland(island);
            user.setIslandRank(IslandRank.OWNER);

            Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> pasteSchematic(island, schematic).thenRun(() -> completableFuture.complete(island)));
            if (IridiumSkyblock.getInstance().getConfiguration().debug) {
                System.out.println("Player: " + user.getName() + "\n" +
                        "UUID: " + user.getUuid() + "\n" +
                        "Event: IslandManager#createIsland");
            }
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

        if (IridiumSkyblock.getInstance().getChunkGenerator() instanceof OceanGenerator oceanGenerator) {
            for (int x = island.getPos1(getWorld()).getBlockX(); x <= island.getPos2(getWorld()).getBlockX(); x++) {
                for (int z = island.getPos1(getWorld()).getBlockZ(); z <= island.getPos2(getWorld()).getBlockZ(); z++) {
                    oceanGenerator.generateWater(getWorld(), x, z);
                    oceanGenerator.generateWater(getNetherWorld(), x, z);
                    oceanGenerator.generateWater(getEndWorld(), x, z);
                }
            }
        } else {
            if (IridiumSkyblock.getInstance().getConfiguration().deleteIslandBlocksWhenIslandIsDelete) {
                deleteIslandBlocks(island, getWorld(), 0).join();
                deleteIslandBlocks(island, getNetherWorld(), 0).join();
                deleteIslandBlocks(island, getEndWorld(), 0).join();
            }
        }
        IslandRegenSettings regenSettings = IridiumSkyblock.getInstance().getConfiguration().regenSettings;
        getIslandMembers(island).stream().map(User::getPlayer).forEach(player -> {
            if (player != null) {
                if (regenSettings.clearInventories)
                    player.getInventory().clear();
                if (regenSettings.clearEnderChests)
                    player.getEnderChest().clear();
                if (regenSettings.resetVaultBalances)
                    IridiumSkyblock.getInstance().getEconomy().withdrawPlayer(player, IridiumSkyblock.getInstance().getEconomy().getBalance(player));
                if (regenSettings.kickMembers) {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youHaveBeenKicked.replace("%player%", user.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    User user1 = IridiumSkyblock.getInstance().getUserManager().getUser(player); // Docta Change variable
                    user1.setIsland(null);
                    if (IridiumSkyblock.getInstance().getConfiguration().debug) {
                        System.out.println("Player: " + user.getName() + "\n" +
                                "UUID: " + user.getUuid() + "\n" +
                                "Event: IslandManager#regenerateIsland");
                    }
                    if (false) IridiumSkyblock.getInstance().saveDataPlayer(user1).join(); // Docta new save
                }

                IslandLog log = new IslandLog(island, LogAction.REGEN_ISLAND, user, null, 0, "");
                IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().addEntry(log);
            }
        });

        if (regenSettings.resetIslandBank) {
            getIslandBank(island, IridiumSkyblock.getInstance().getBankItems().moneyBankItem).setNumber(0);
            getIslandBank(island, IridiumSkyblock.getInstance().getBankItems().crystalsBankItem).setNumber(0);
            getIslandBank(island, IridiumSkyblock.getInstance().getBankItems().experienceBankItem).setNumber(0);
        }

        if (regenSettings.resetBoosters) {
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandBoosterTableManager().getEntries(island).forEach(islandBooster -> islandBooster.setTime(LocalDateTime.now()));
        }

        if (regenSettings.resetMissions) {
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandMissionTableManager().getEntries(island).forEach(islandMission -> islandMission.setProgress(0));
        }

        if (regenSettings.resetUpgrades) {
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandUpgradeTableManager().getEntries(island).forEach(islandUpgrade -> islandUpgrade.setLevel(1));
        }

        if (regenSettings.clearWarps) {
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandWarpTableManager().getEntries(island).forEach(IridiumSkyblock.getInstance().getDatabaseManager().getIslandWarpTableManager()::delete);
        }

        if (regenSettings.resetPermissions) {
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandPermissionTableManager().getEntries(island).forEach(IridiumSkyblock.getInstance().getDatabaseManager().getIslandPermissionTableManager()::delete);
        }

        if (regenSettings.unbanAll) {
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandBanTableManager().getEntries(island).forEach(IridiumSkyblock.getInstance().getDatabaseManager().getIslandBanTableManager()::delete);
        }

        if (regenSettings.giveUpInvites) {
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandInviteTableManager().getEntries(island).forEach(IridiumSkyblock.getInstance().getDatabaseManager().getIslandInviteTableManager()::delete);
        }

        if (regenSettings.resetBorderColour) {
            island.setColor(IridiumSkyblock.getInstance().getBorder().defaultColor);
        }

        if (regenSettings.makeIslandPrivate) {
            island.setVisitable(false);
        }

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
        setIslandBiome(island, schematicConfig.overworld.biome);
        setIslandBiome(island, schematicConfig.nether.biome);
        setIslandBiome(island, schematicConfig.end.biome);
        return IridiumSkyblock.getInstance().getSchematicManager().pasteSchematic(island, ImmutableMap.<World, Schematics.SchematicWorld>builder()
                .put(getWorld(), schematicConfig.overworld)
                .put(getNetherWorld(), schematicConfig.nether)
                .put(getEndWorld(), schematicConfig.end)
                .build()
        );
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
     * @param worlds The worlds
     * @return A list of Chunks the island is in
     */
    public CompletableFuture<List<Chunk>> getIslandChunks(@NotNull Island island, @NotNull World... worlds) {
        return CompletableFuture.supplyAsync(() -> {
            List<CompletableFuture<Chunk>> chunks = new ArrayList<>();
            for (World world : worlds) {

                Location pos1 = island.getPos1(world);
                Location pos2 = island.getPos2(world);

                int minX = pos1.getBlockX() >> 4;
                int minZ = pos1.getBlockZ() >> 4;
                int maxX = pos2.getBlockX() >> 4;
                int maxZ = pos2.getBlockZ() >> 4;

                for (int x = minX; x <= maxX; x++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        chunks.add(IridiumSkyblock.getInstance().getMultiVersion().getChunkAt(world, x, z));
                    }
                }
            }
            return chunks.stream().map(CompletableFuture::join).collect(Collectors.toList());
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return Collections.emptyList();
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
     * Gets a list of all Members on an island
     *
     * @param island The Specified Island
     * @return A list of all members on this island
     */
    public @NotNull List<User> getPlayersOnIsland(@NotNull Island island) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> island.isInIsland(player.getLocation()))
                .map(IridiumSkyblock.getInstance().getUserManager()::getUser)
                .collect(Collectors.toList());
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
     * Finds an island by the player's location with cache
     *
     * @param player The specified Player
     * @return An optional of the island the player is in
     */
    public @NotNull Optional<Island> getIslandViaPlayerLocation(Player player) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(player.getWorld())) {
            return Optional.empty();
        }

        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        if (user.getCurrentIslandVisiting() != null) {
            if (user.getCurrentIslandVisiting().isInIsland(player.getLocation())) {
                return Optional.of(user.getCurrentIslandVisiting());
            }
        }

        Optional<Island> island = getIslandViaLocation(player.getLocation());
        island.ifPresent(user::setCurrentIslandVisiting);
        return island;
    }

    /**
     * Gets an {@link Island} from locations.
     *
     * @param location The locations the island is in
     * @return Optional of the island at the locations, empty if there is none
     */
    public @NotNull Optional<Island> getIslandViaLocation(@NotNull Location location) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(location.getWorld())) return Optional.empty();
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries().stream().filter(island -> island.isInIsland(location)).findFirst();
    }

    /**
     * Gets an IslandTrusted object from island and user
     *
     * @param island The specified island
     * @param user   The user who is trusted
     * @return An optional IslandTrusted Object
     */
    public Optional<IslandTrusted> getIslandTrusted(Island island, User user) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager().getEntry(new IslandTrusted(island, user, user));
    }

    /**
     * Gets an IslandBan object from island and user
     *
     * @param island The specified island
     * @param user   The banned user
     * @return an optional IslandBan object
     */
    public Optional<IslandBan> getIslandBan(Island island, User user) {
        IslandBan islandBan = new IslandBan(island, user, user);
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandBanTableManager().getEntry(islandBan);
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
        Optional<IslandPermission> islandPermission = IridiumSkyblock.getInstance().getDatabaseManager().getIslandPermissionTableManager().getEntry(new IslandPermission(island, key, islandRank, true));
        return islandPermission.map(IslandPermission::isAllowed).orElseGet(() -> islandRank.getLevel() >= permission.getDefaultRank().getLevel());
    }

    /**
     * Gets weather a permission is allowed or denied.
     *
     * @param island     The specified Island
     * @param user       The Specified User
     * @param permission The Specified permission
     * @param key        The permission Key
     * @return The the permission is allowed
     */
    public boolean getIslandPermission(@NotNull Island island, @NotNull User user, @NotNull Permission permission, @NotNull String key) {
        IslandRank islandRank = island.equals(user.getIsland().orElse(null)) ? user.getIslandRank() : IslandRank.VISITOR;
        if (getIslandTrusted(island, user).isPresent()) {
            islandRank = IslandRank.MEMBER;
        }
        return getIslandPermission(island, islandRank, permission, key) || user.isBypassing();
    }

    /**
     * Gets weather a permission is allowed or denied.
     *
     * @param island         The specified Island
     * @param user           The Specified User
     * @param permissionType The Specified permission type
     * @return The the permission is allowed
     */
    public boolean getIslandPermission(@NotNull Island island, @NotNull User user, @NotNull PermissionType permissionType) {
        return getIslandPermission(island, user, IridiumSkyblock.getInstance().getPermissionList().get(permissionType.getPermissionKey()), permissionType.getPermissionKey());
    }

    /**
     * Gets an Island's bank from BankItem.
     *
     * @param island   The specified Island
     * @param bankItem The BankItem we are getting
     * @return the IslandBank
     */
    public synchronized IslandBank getIslandBank(@NotNull Island island, @NotNull BankItem bankItem) {
        Optional<IslandBank> optionalIslandBank = IridiumSkyblock.getInstance().getDatabaseManager().getIslandBankTableManager().getEntry(new IslandBank(island, bankItem.getName(), 0));
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
    public synchronized IslandBlocks getIslandBlock(@NotNull Island island, @NotNull XMaterial material) {
        Optional<IslandBlocks> islandBlocksOptional = IridiumSkyblock.getInstance().getDatabaseManager().getIslandBlocksTableManager().getEntry(new IslandBlocks(island, material));
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
    public synchronized IslandSpawners getIslandSpawners(@NotNull Island island, @NotNull EntityType spawnerType) {
        Optional<IslandSpawners> islandSpawnersOptional = IridiumSkyblock.getInstance().getDatabaseManager().getIslandSpawnersTableManager().getEntry(new IslandSpawners(island, spawnerType));
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
     * @param allowed    If the permission is allowed
     */
    public synchronized void setIslandPermission(@NotNull Island island, @NotNull IslandRank islandRank, @NotNull String key, boolean allowed) {
        Optional<IslandPermission> islandPermission = IridiumSkyblock.getInstance().getDatabaseManager().getIslandPermissionTableManager().getEntry(new IslandPermission(island, key, islandRank, true));
        if (islandPermission.isPresent()) {
            islandPermission.get().setAllowed(allowed);
        } else {
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandPermissionTableManager().addEntry(new IslandPermission(island, key, islandRank, allowed));
        }
    }

    /**
     * Gets an IslandSetting from a specific Island
     *
     * @param island       The specified Island
     * @param settingName  The Setting Name
     * @param defaultValue The default value for this setting
     * @return The IslandSetting object
     */
    public synchronized IslandSetting getIslandSetting(@NotNull Island island, @NotNull String settingName, @NotNull String defaultValue) {
        IslandSetting islandSetting = new IslandSetting(island, settingName, defaultValue);
        Optional<IslandSetting> islandSettingOptional = IridiumSkyblock.getInstance().getDatabaseManager().getIslandSettingTableManager().getEntry(islandSetting);
        if (islandSettingOptional.isPresent()) {
            return islandSettingOptional.get();
        } else {
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandSettingTableManager().addEntry(islandSetting);
            return islandSetting;
        }
    }

    /**
     * Gets an IslandSetting from a specific Island
     *
     * @param island      The specified Island
     * @param settingType The specified Setting Type
     * @return The IslandSetting object
     */
    public synchronized IslandSetting getIslandSetting(@NotNull Island island, @NotNull SettingType settingType) {
        return getIslandSetting(island, settingType.getSettingName(), settingType.getDefaultValue());
    }

    /**
     * Deletes all blocks in an Island.
     * Start at the top and work your way down to the lowest layer.
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
                    if (block.getState() instanceof InventoryHolder) {
                        ((InventoryHolder) block.getState()).getInventory().clear();
                    }
                    block.setType(Material.AIR, false);
                }
            }
        }

        if (y == LocationUtils.getMinHeight(world)) {
            completableFuture.complete(null);
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
        if (user == null) return;
        IslandDeleteEvent islandDeleteEvent = new IslandDeleteEvent(island, user);
        Bukkit.getPluginManager().callEvent(islandDeleteEvent);
        if (islandDeleteEvent.isCancelled()) return;
        clearIslandCache();
        if (IridiumSkyblock.getInstance().getConfiguration().deleteIslandBlocksWhenIslandIsDelete) {
            deleteIslandBlocks(island, getWorld(), 3);
            deleteIslandBlocks(island, getNetherWorld(), 3);
            deleteIslandBlocks(island, getEndWorld(), 3);
        }
        deleteIslanDatabasedEntries(island, user);

        getIslandMembers(island).stream().map(User::getPlayer).forEach(player -> {
            if (player != null) {
                if (IridiumSkyblock.getInstance().getConfiguration().deleteSettings.clearInventories) {
                    player.getInventory().clear();
                }
                if (IridiumSkyblock.getInstance().getConfiguration().deleteSettings.clearEnderChests) {
                    player.getEnderChest().clear();
                }
                if (IridiumSkyblock.getInstance().getConfiguration().deleteSettings.resetVaultBalances) {
                    IridiumSkyblock.getInstance().getEconomy().withdrawPlayer(player, IridiumSkyblock.getInstance().getEconomy().getBalance(player));
                }
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
     * Delete all database entries of specific island
     *
     * @param island The specified Island
     */
    private void deleteIslanDatabasedEntries(@NotNull Island island, User user) {
        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> {
            IslandLog islandLogDelete = new IslandLog(island, LogAction.DELETE_ISLAND, user, null, 0, "Suppression de l'ile");
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().delete(island);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandBanTableManager().getEntries(island).forEach(IridiumSkyblock.getInstance().getDatabaseManager().getIslandBanTableManager()::delete);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandBankTableManager().getEntries(island).forEach(IridiumSkyblock.getInstance().getDatabaseManager().getIslandBankTableManager()::delete);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandBlocksTableManager().getEntries(island).forEach(IridiumSkyblock.getInstance().getDatabaseManager().getIslandBlocksTableManager()::delete);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandBoosterTableManager().getEntries(island).forEach(IridiumSkyblock.getInstance().getDatabaseManager().getIslandBoosterTableManager()::delete);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandInviteTableManager().getEntries(island).forEach(IridiumSkyblock.getInstance().getDatabaseManager().getIslandInviteTableManager()::delete);
            // IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().getEntries(island).forEach(IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager()::delete); -> Nous gardons les logs
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandMissionTableManager().getEntries(island).forEach(IridiumSkyblock.getInstance().getDatabaseManager().getIslandMissionTableManager()::delete);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandRewardTableManager().getEntries(island).forEach(IridiumSkyblock.getInstance().getDatabaseManager().getIslandRewardTableManager()::delete);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandSpawnersTableManager().getEntries(island).forEach(IridiumSkyblock.getInstance().getDatabaseManager().getIslandSpawnersTableManager()::delete);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager().getEntries(island).forEach(IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager()::delete);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandUpgradeTableManager().getEntries(island).forEach(IridiumSkyblock.getInstance().getDatabaseManager().getIslandUpgradeTableManager()::delete);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandWarpTableManager().getEntries(island).forEach(IridiumSkyblock.getInstance().getDatabaseManager().getIslandWarpTableManager()::delete);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().addEntry(islandLogDelete);
        });
    }

    /**
     * Gets an Island upgrade
     *
     * @param island The specified Island
     * @param user   The specified User
     * @return The a boolean the user is banned on this island
     */
    public boolean isBannedOnIsland(@NotNull Island island, User user) {
        return getIslandBan(island, user).isPresent() && !user.isBypassing();
    }

    /**
     * Gets an Island upgrade
     *
     * @param island  The specified Island
     * @param upgrade The specified Upgrade's name
     * @return The island Upgrade
     */
    public synchronized IslandUpgrade getIslandUpgrade(@NotNull Island island, @NotNull String upgrade) {
        Optional<IslandUpgrade> islandUpgrade = IridiumSkyblock.getInstance().getDatabaseManager().getIslandUpgradeTableManager().getEntry(new IslandUpgrade(island, upgrade));
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
    public synchronized IslandMission getIslandMission(@NotNull Island island, @NotNull Mission mission, @NotNull String missionKey, int missionIndex) {
        Optional<IslandMission> islandMissionOptional = IridiumSkyblock.getInstance().getDatabaseManager().getIslandMissionTableManager().getEntry(new IslandMission(island, mission, missionKey, missionIndex));
        if (islandMissionOptional.isPresent()) {
            return islandMissionOptional.get();
        } else {
            IslandMission islandMission = new IslandMission(island, mission, missionKey, missionIndex);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandMissionTableManager().addEntry(islandMission);
            return islandMission;
        }
    }

    private synchronized String getDailyIslandMission(@NotNull Island island, int index) {
        List<String> islandMissions = IridiumSkyblock.getInstance().getDatabaseManager().getIslandMissionTableManager().getEntries(island).stream()
                .filter(islandMission -> islandMission.getType() == Mission.MissionType.DAILY)
                .map(IslandMission::getMissionName)
                .distinct()
                .collect(Collectors.toList());

        if (islandMissions.size() > index) {
            return islandMissions.get(index);
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        List<String> availableMissions = IridiumSkyblock.getInstance().getMissionsList().keySet().stream()
                .filter(mission -> IridiumSkyblock.getInstance().getMissionsList().get(mission).getMissionType() == Mission.MissionType.DAILY)
                .filter(mission -> islandMissions.stream().noneMatch(m -> m.equals(mission)))
                .collect(Collectors.toList());

        String key = availableMissions.get(random.nextInt(availableMissions.size()));
        Mission mission = IridiumSkyblock.getInstance().getMissionsList().get(key);

        for (int i = 0; i < mission.getMissions().size(); i++) {
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandMissionTableManager().addEntry(new IslandMission(island, mission, key, i));
        }

        return key;
    }

    /**
     * Gets the Islands daily missions.
     *
     * @param island The specified Island
     * @return The daily missions
     */
    public synchronized Map<String, Mission> getDailyIslandMissions(@NotNull Island island) {
        Map<String, Mission> missions = new HashMap<>();

        IntStream.range(0, IridiumSkyblock.getInstance().getMissions().dailySlots.size())
                .boxed()
                .map(i -> getDailyIslandMission(island, i))
                .forEach(mission ->
                        missions.put(mission, IridiumSkyblock.getInstance().getMissionsList().get(mission))
                );

        return missions;
    }

    public int getIslandBlockAmount(Island island, XMaterial material) {
        return getIslandBlock(island, material).getAmount() + IridiumSkyblock.getInstance().getStackerSupport().getExtraBlocks(island, material);
    }

    public int getIslandSpawnerAmount(Island island, EntityType entityType) {
        return getIslandSpawners(island, entityType).getAmount() + IridiumSkyblock.getInstance().getStackerSupport().getExtraSpawners(island, entityType);
    }

    /**
     * Recalculates the island value of the specified island.
     *
     * @param island The specified Island
     */
    public void recalculateIsland(@NotNull Island island) {
        getIslandChunks(island, getWorld(), getNetherWorld(), getEndWorld()).thenAcceptAsync(chunks -> {
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandBlocksTableManager().getEntries(island).forEach(islandBlocks -> islandBlocks.setAmount(0));
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandSpawnersTableManager().getEntries(island).forEach(islandSpawners -> islandSpawners.setAmount(0));

            recalculateIsland(island, chunks);
        });
    }

    /**
     * Recalculates the island async with specified ChunkSnapshots.
     *
     * @param island The specified Island
     * @param chunks The Island's Chunks
     */
    private void recalculateIsland(@NotNull Island island, @NotNull List<Chunk> chunks) {
        chunks.stream().map(chunk -> chunk.getChunkSnapshot(true, false, false)).forEach(chunk -> {
            World world = Bukkit.getWorld(chunk.getWorldName());
            int maxHeight = world == null ? 255 : world.getMaxHeight() - 1;
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    if (island.isInIsland(x + (chunk.getX() * 16), z + (chunk.getZ() * 16))) {
                        final int maxy = Math.min(maxHeight, chunk.getHighestBlockYAt(x, z));
                        for (int y = LocationUtils.getMinHeight(world); y <= maxy; y++) {
                            XMaterial material = XMaterial.matchXMaterial(chunk.getBlockType(x, y, z));
                            if (material.equals(XMaterial.AIR)) continue;

                            IslandBlocks islandBlock = IridiumSkyblock.getInstance().getIslandManager().getIslandBlock(island, material);
                            islandBlock.setAmount(islandBlock.getAmount() + 1);
                        }
                    }
                }
            }
        });
        chunks.forEach(chunk -> {
            for (BlockState blockState : chunk.getTileEntities()) {
                if (!(blockState instanceof CreatureSpawner creatureSpawner)) continue;
                if (!island.isInIsland(blockState.getLocation())) continue;
                IslandSpawners islandSpawners = IridiumSkyblock.getInstance().getIslandManager().getIslandSpawners(island, creatureSpawner.getSpawnedType());
                islandSpawners.setAmount(islandSpawners.getAmount() + 1);
            }
        });
    }

    /**
     * Gets time remaining on an island booster
     *
     * @param island  The specified Island
     * @param booster The booster name
     * @return The time remaining
     */
    public synchronized IslandBooster getIslandBooster(@NotNull Island island, @NotNull String booster) {
        Optional<IslandBooster> islandBooster = IridiumSkyblock.getInstance().getDatabaseManager().getIslandBoosterTableManager().getEntry(new IslandBooster(island, booster));
        if (islandBooster.isPresent()) {
            return islandBooster.get();
        } else {
            IslandBooster newBooster = new IslandBooster(island, booster);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandBoosterTableManager().addEntry(newBooster);
            return newBooster;
        }
    }

    /**
     * Gets all entities on an island
     *
     * @param island The specified Island
     * @return A list of all entities on that island
     */
    public CompletableFuture<List<Entity>> getEntities(@NotNull Island island, @NotNull World... worlds) {
        CompletableFuture<List<Entity>> completableFuture = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> {
            List<Chunk> chunks = new ArrayList<>();
            for (World world : worlds) {
                chunks.addAll(getIslandChunks(island, world).join());
            }
            Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> {
                List<Entity> entities = new ArrayList<>();
                for (Chunk chunk : chunks) {
                    for (Entity entity : chunk.getEntities()) {
                        if (island.isInIsland(entity.getLocation())) {
                            entities.add(entity);
                        }
                    }
                }
                completableFuture.complete(entities);
            });
        });
        return completableFuture;
    }

    public synchronized void islandLevelUp(Island island, int newLevel) {

        for (User user : getIslandMembers(island)) {
            Player player = Bukkit.getPlayer(user.getUuid());
            if (player != null) {
                IridiumSkyblock.getInstance().getConfiguration().islandLevelUpSound.play(player);
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandLevelUp
                        .replace("%level%", String.valueOf(newLevel))
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        }

        Reward reward = null;
        List<Map.Entry<Integer, Reward>> entries = IridiumSkyblock.getInstance().getConfiguration().islandLevelRewards.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());
        for (Map.Entry<Integer, Reward> entry : entries) {
            if (newLevel % entry.getKey() == 0) {
                reward = entry.getValue();
            }
        }
        if (reward != null) {
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandRewardTableManager().addEntry(new IslandReward(island, reward));
        }
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
            return islandValueSortCache.getCache(() ->
                    IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries().stream().sorted(Comparator.comparing(Island::getValue).reversed()).collect(Collectors.toList())
            );
        }
        if (sortType == SortType.LEVEL) {
            return islandLevelSortCache.getCache(() ->
                    IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries().stream().sorted(Comparator.comparing(Island::getExperience).reversed()).collect(Collectors.toList())
            );
        }
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries();
    }

    /**
     * Represents a way of ordering Islands.
     */
    public enum SortType {
        VALUE, LEVEL
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
     * Returns the EndWorld
     *
     * @return The end skyblock {@link World}, might be null if some third-party plugin deleted it
     * @since 3.0.0
     */
    public World getEndWorld() {
        return Bukkit.getWorld(IridiumSkyblock.getInstance().getConfiguration().worldName + "_the_end");
    }

    public boolean isIslandOverWorld(World world) {
        return world.equals(getWorld());
    }

    public boolean isIslandNether(World world) {
        return world.equals(getNetherWorld());
    }

    public boolean isIslandEnd(World world) {
        return world.equals(getEndWorld());
    }

    public ItemStack getIslandCrystal(int amount) {
        ItemStack itemStack = ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getConfiguration().islandCrystal, Collections.singletonList(
                new Placeholder("amount", String.valueOf(amount))
        ));
        NBTItem nbtItem = new NBTItem(itemStack);
        NBTCompound nbtCompound = nbtItem.getOrCreateCompound("iridiumskyblock");
        nbtCompound.setInteger("islandCrystals", amount);
        return nbtItem.getItem();
    }

    public int getIslandCrystals(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return 0;
        NBTCompound nbtCompound = new NBTItem(itemStack).getOrCreateCompound("iridiumskyblock");
        if (nbtCompound.hasKey("islandCrystals")) {
            return nbtCompound.getInteger("islandCrystals");
        }
        return 0;
    }

}
