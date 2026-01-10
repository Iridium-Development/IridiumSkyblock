package com.iridium.iridiumskyblock.managers;

import com.cryptomorin.xseries.XBiome;
import com.cryptomorin.xseries.XEntityType;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.reflection.XReflection;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IslandCreateEvent;
import com.iridium.iridiumskyblock.api.IslandDeleteEvent;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.CreateGUI;
import com.iridium.iridiumskyblock.managers.tablemanagers.SqlTableManager;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import com.iridium.iridiumteams.LogType;
import com.iridium.iridiumteams.Rank;
import com.iridium.iridiumteams.Setting;
import com.iridium.iridiumteams.database.*;
import com.iridium.iridiumteams.managers.TeamManager;
import com.iridium.iridiumteams.missions.Mission;
import com.iridium.iridiumteams.missions.MissionData;
import com.iridium.iridiumteams.missions.MissionType;
import com.iridium.iridiumteams.support.SpawnSupport;
import com.iridium.iridiumteams.support.StackerSupport;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.MaskingExtent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.SideEffectSet;
import com.sk89q.worldedit.world.block.BlockTypes;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTFile;
import io.papermc.lib.PaperLib;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IslandManager extends TeamManager<Island, User> {

    public IslandManager() {
        super(IridiumSkyblock.getInstance());
    }

    public boolean deleteWorld(File path) {
        if (path.exists()) {
            File files[] = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    // Bukkit's createWorld method requires "/" no matter which platform we're on.
    // Which is why we need to code it like this, File.separator and co. aren't enough.
    public String getCacheWorldName(World world) {
        return "plugins/iridiumskyblock" + "/" + "regenWorlds" + "/" + world.getName() + "_regen";
    }

    public void createWorld(World.Environment environment, String name) {
        if (!IridiumSkyblock.getInstance().getConfiguration().enabledWorlds.getOrDefault(environment, true)) return;

        WorldCreator worldCreator = new WorldCreator(name)
                .generator(IridiumSkyblock.getInstance().getDefaultWorldGenerator(name, null))
                .environment(environment);

        World world = Bukkit.createWorld(worldCreator);

        createCacheWorld(world);

        if (world != null && world.getEnvironment() == World.Environment.THE_END) {
            Bukkit.unloadWorld(world.getName(), true);

            try {
                File file = new File(worldCreator.name() + File.separator + "level.dat");
                NBTFile worldFile = new NBTFile(file);

                NBTCompound compound = worldFile.getOrCreateCompound("Data").getOrCreateCompound("DragonFight");

                compound.setBoolean("PreviouslyKilled", true);
                compound.setBoolean("DragonKilled", true);
                compound.setBoolean("NeedsStateScanning", false);

                worldFile.save();
            } catch (Exception exception) {
                exception.printStackTrace();
                IridiumSkyblock.getInstance().getLogger().warning("Failed to delete dragon from world");
            }

            // Note this world is already created, we are just loading it here
            Bukkit.createWorld(worldCreator);
        }
    }

    // For the regenerateTerrain() method to work correctly, we need to access the cached world, which we create here.
    public void createCacheWorld(World world) {

        if (!IridiumSkyblock.getInstance().getConfiguration().generatorType.isTerrainGenerator()) return;

        if (Bukkit.getWorld(getCacheWorldName(world)) == null) {
            WorldCreator worldCreator = new WorldCreator(getCacheWorldName(world)).copy(world);
            worldCreator.createWorld();
        }

        if (Bukkit.getWorld(getCacheWorldName(world)).getSeed() != Bukkit.getWorld(world.getName()).getSeed()) {
            File cacheWorld = Bukkit.getWorld(getCacheWorldName(world)).getWorldFolder();
            Bukkit.unloadWorld(getCacheWorldName(world), false);
            deleteWorld(cacheWorld);
            createCacheWorld(world);
        }
    }

    public void setIslandBiome(@NotNull Island island, @NotNull XBiome biome) {
        World.Environment dimension = biome.getEnvironment().get();
        World world = getWorld(dimension);

        if (world == null) return;

        getIslandChunks(island).thenAccept(chunks -> {
            Location pos1 = island.getMaximumPosition1(world);
            Location pos2 = island.getMaximumPosition2(world);
            biome.setBiome(pos1, pos2).thenRun(() -> {
                for (Chunk chunk : chunks) {
                    chunk.getWorld().refreshChunk(chunk.getX(), chunk.getZ());
                }
            });
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    @Override
    public Optional<Island> getTeamViaID(int id) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getIsland(id);
    }

    @Override
    public Optional<Island> getTeamViaName(String name) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getIsland(name);
    }

    @Override
    public Optional<Island> getTeamViaLocation(Location location) {
        if (!isInSkyblockWorld(location.getWorld())) return Optional.empty();
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries().stream().filter(island ->
                island.isInIsland(location)
        ).findFirst();
    }

    @Override
    public Optional<Island> getTeamViaLocation(Location location, Island island) {
        if (island.isInIsland(location)) {
            return Optional.of(island);
        }
        return getTeamViaLocation(location);
    }

    @Override
    public Optional<Island> getTeamViaLocation(Location location, Optional<Island> island) {
        if (island.isPresent()) {
            return getTeamViaLocation(location, island.get());
        }
        return getTeamViaLocation(location);
    }

    @Override
    public Optional<Island> getTeamViaNameOrPlayer(String name) {
        if (name == null || name.equals("")) return Optional.empty();
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(name);
        Optional<Island> team = IridiumSkyblock.getInstance().getUserManager().getUser(targetPlayer).getIsland();
        if (!team.isPresent()) {
            return getTeamViaName(name);
        }
        return team;
    }

    @Override
    public Optional<Island> getTeamViaPlayerLocation(Player player) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        return user.getCurrentIsland();
    }

    @Override
    public Optional<Island> getTeamViaPlayerLocation(Player player, Location location) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        return user.getCurrentIsland(location);
    }

    @Override
    public void sendTeamTitle(Player player, Island island) {
        List<Placeholder> placeholders = IridiumSkyblock.getInstance().getTeamsPlaceholderBuilder().getPlaceholders(island);
        String top = StringUtils.processMultiplePlaceholders(IridiumSkyblock.getInstance().getConfiguration().islandTitleTop, placeholders);
        String bottom = StringUtils.processMultiplePlaceholders(IridiumSkyblock.getInstance().getConfiguration().islandTitleBottom, placeholders);
        IridiumSkyblock.getInstance().getNms().sendTitle(player, StringUtils.color(top), StringUtils.color(bottom), 20, 40, 20);
    }

    @Override
    public List<Island> getTeams() {
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries();
    }

    @Override
    public boolean isInTeam(Island island, Location location) {
        return island.isInIsland(location);
    }

    private CompletableFuture<String> getSchematic(Player player) {
        CompletableFuture<String> schematicNameCompletableFuture = new CompletableFuture<>();
        if (IridiumSkyblock.getInstance().getSchematics().schematics.entrySet().size() == 1) {
            for (Map.Entry<String, Schematics.SchematicConfig> entry : IridiumSkyblock.getInstance().getSchematics().schematics.entrySet()) {
                schematicNameCompletableFuture.complete(entry.getKey());
                return schematicNameCompletableFuture;
            }
        }

        Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> player.openInventory(new CreateGUI(player, schematicNameCompletableFuture).getInventory()));
        return schematicNameCompletableFuture;
    }

    @Override
    public CompletableFuture<Island> createTeam(@NotNull Player owner, String name) {
        return CompletableFuture.supplyAsync(() -> {
            String schematic = getSchematic(owner).join();
            if (schematic == null) return null;

            User user = IridiumSkyblock.getInstance().getUserManager().getUser(owner);
            Schematics.SchematicConfig schematicConfig = IridiumSkyblock.getInstance().getSchematics().schematics.get(schematic);

            if (IridiumSkyblock.getInstance().getConfiguration().islandCreationCost) {
                if (schematicConfig.regenCost.money != 0 || !schematicConfig.regenCost.bankItems.isEmpty()) {
                    if (!IridiumSkyblock.getInstance().getSchematicManager().buy(owner, schematicConfig)) {
                        return null;
                    }
                }
            }

            IslandCreateEvent islandCreateEvent = getIslandCreateEvent(user, name, schematicConfig).join();
            if (islandCreateEvent.isCancelled()) return null;

            owner.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().creatingIsland
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));

            Island island = new Island(islandCreateEvent.getIslandName());

            IridiumSkyblock.getInstance().getDatabaseManager().registerIsland(island).join();

            user.setTeam(island);
            user.setUserRank(Rank.OWNER.getId());

            generateIsland(island, islandCreateEvent.getSchematicConfig()).join();
            Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> {
                teleport(owner, island.getHome(), island);
                IridiumSkyblock.getInstance().getNms().sendTitle(owner, IridiumSkyblock.getInstance().getConfiguration().islandCreateTitle, IridiumSkyblock.getInstance().getConfiguration().islandCreateSubTitle, 20, 40, 20);
            });

            return island;
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    private CompletableFuture<IslandCreateEvent> getIslandCreateEvent(@NotNull User user, @Nullable String islandName, Schematics.@NotNull SchematicConfig schematicConfig) {
        CompletableFuture<IslandCreateEvent> completableFuture = new CompletableFuture<>();
        Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> {
            IslandCreateEvent islandCreateEvent = new IslandCreateEvent(user, islandName, schematicConfig);
            Bukkit.getPluginManager().callEvent(islandCreateEvent);
            completableFuture.complete(islandCreateEvent);

        });
        return completableFuture;
    }

    public CompletableFuture<Void> generateIsland(Island island, Schematics.SchematicConfig schematicConfig) {
        return CompletableFuture.runAsync(() -> {
            setHome(island, schematicConfig);
            clearEntities(island);
            removePlayers(island).join();
            deleteIslandBlocks(island).join();
            if (IridiumSkyblock.getInstance().getConfiguration().generatorType.isTerrainGenerator()) {
                regenerateTerrain(island).join();
            }
            IridiumSkyblock.getInstance().getSchematicManager().pasteSchematic(island, schematicConfig).join();
            setIslandBiome(island, schematicConfig);
        });
    }

    private void setHome(Island island, Schematics.SchematicConfig schematicConfig) {
        Location location = island.getCenter(getWorld(World.Environment.NORMAL)).add(schematicConfig.xHome, schematicConfig.yHome, schematicConfig.zHome);
        location.setYaw(schematicConfig.yawHome);
        island.setHome(location);
    }

    public void setIslandBiome(Island island, Schematics.SchematicConfig schematicConfig) {
        setIslandBiome(island, schematicConfig.overworld.biome);
        setIslandBiome(island, schematicConfig.nether.biome);
        setIslandBiome(island, schematicConfig.end.biome);
    }

    public CompletableFuture<Void> clearEntities(Island island) {
        return CompletableFuture.runAsync(() -> {
            List<CompletableFuture<Void>> completableFutures = Arrays.asList(
                    clearEntities(island, getWorld(World.Environment.NORMAL)),
                    clearEntities(island, getWorld(World.Environment.NETHER)),
                    clearEntities(island, getWorld(World.Environment.THE_END))
            );
            completableFutures.forEach(CompletableFuture::join);
        });
    }

    public CompletableFuture<Void> clearEntities(Island island, World world) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        if (world == null) {
            completableFuture.complete(null);
        } else {
            Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> clearEntities(island, world, completableFuture));
        }
        return completableFuture;
    }

    private void clearEntities(Island island, World world, CompletableFuture<Void> completableFuture) {
        if (world == null) return;

        world.getEntities().stream()
                .filter(entity -> island.isInIsland(entity.getLocation()))
                .filter(entity -> entity.getType() != EntityType.PLAYER)
                .forEach(Entity::remove);

        completableFuture.complete(null);
    }

    public CompletableFuture<Void> removePlayers(Island island) {

        return CompletableFuture.runAsync(() -> {
            List<CompletableFuture<Void>> completableFutures = Arrays.asList(
                    removePlayers(island, getWorld(World.Environment.NORMAL)),
                    removePlayers(island, getWorld(World.Environment.NETHER)),
                    removePlayers(island, getWorld(World.Environment.THE_END))
            );
            completableFutures.forEach(CompletableFuture::join);
        });
    }

    private CompletableFuture<Void> removePlayers(Island island, World world) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        if (world == null) {
            completableFuture.complete(null);
        } else {
            Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> removePlayers(island, world, completableFuture));
        }
        return completableFuture;
    }

    private void removePlayers(Island island, World world, CompletableFuture<Void> completableFuture) {

        if (world == null) return;

        Optional<SpawnSupport<Island>> spawnSupport = IridiumSkyblock.getInstance().getSupportManager().getSpawnSupport().stream().findFirst();

        world.getPlayers().forEach(player -> {

            if (island.isInIsland(player.getLocation())) {

                boolean bypasser = IridiumSkyblock.getInstance().getUserManager().getUser(player).isBypassing();

                // If a player is bypassing island restrictions, they're likely an admin.
                // We don't want to kick admins from an island if they're watching it.
                if (!bypasser || (bypasser && IridiumSkyblock.getInstance().getConfiguration().tpBypassersWithRegen)) { // if not bypassing OR bypassing and should be teleported
                    if (spawnSupport.isPresent()) {
                        player.teleport(spawnSupport.get().getSpawn(player));
                    } else {
                        player.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
                    }
                }
            }
        });

        completableFuture.complete(null);
    }

    public CompletableFuture<Void> deleteIslandBlocks(Island island) {
        return CompletableFuture.runAsync(() -> {
            List<CompletableFuture<Void>> completableFutures = Arrays.asList(
                    deleteIslandBlocks(island, getWorld(World.Environment.NORMAL)),
                    deleteIslandBlocks(island, getWorld(World.Environment.NETHER)),
                    deleteIslandBlocks(island, getWorld(World.Environment.THE_END))
            );
            completableFutures.forEach(CompletableFuture::join);
        });
    }

    private CompletableFuture<Void> deleteIslandBlocks(Island island, World world) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        if (world == null) {
            completableFuture.complete(null);
        } else {
            Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () ->
                    deleteIslandBlocks(island, world, completableFuture, IridiumSkyblock.getInstance().getConfiguration().pasterDelayInTick));
        }
        return completableFuture;
    }

    private void deleteIslandBlocks(Island island, World world, CompletableFuture<Void> completableFuture, int delay) {

        if (world == null) return;

        Location pos1 = island.getMaximumPosition1(world);
        Location pos2 = island.getMaximumPosition2(world);

        int minHeight = 0;
        if (XReflection.supports(18)) minHeight = world.getMinHeight();
        final int finalMinHeight = minHeight;
        int maxHeight = world.getMaxHeight();

        String paster = IridiumSkyblock.getInstance().getConfiguration().paster.toLowerCase();

        switch(paster) {

            case "fawe": {
                deleteFAWE(world, pos1, pos2, finalMinHeight, maxHeight, completableFuture, delay);
                break;
            }

            case "worldedit": {
                deleteWE(world, pos1, pos2, finalMinHeight, maxHeight, completableFuture, delay);
                break;
            }

            case "internalasync": {}
            default: {
                deleteIA(world, pos1, pos2, finalMinHeight, maxHeight, completableFuture, delay);
                 break;
            }
        }
    }

    public CompletableFuture<Void> regenerateTerrain(Island island) {
        return CompletableFuture.runAsync(() -> {
            List<CompletableFuture<Void>> completableFutures = Arrays.asList(
                    regenerateTerrain(island, getWorld(World.Environment.NORMAL)),
                    regenerateTerrain(island, getWorld(World.Environment.NETHER)),
                    regenerateTerrain(island, getWorld(World.Environment.THE_END))
            );
            completableFutures.forEach(CompletableFuture::join);
        });
    }

    private CompletableFuture<Void> regenerateTerrain(Island island, World world) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        if (world == null) {
            completableFuture.complete(null);
        } else {
            Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () ->
                    regenerateTerrain(island, world, completableFuture, IridiumSkyblock.getInstance().getConfiguration().pasterDelayInTick));
        }
        return completableFuture;
    }

    public void regenerateTerrain(Island island, World world, CompletableFuture<Void> completableFuture, int delay) {

        if (world == null) return;

        World regenWorld = Bukkit.getWorld(getCacheWorldName(world));

        Location pos1 = island.getMaximumPosition1(regenWorld);
        Location pos2 = island.getMaximumPosition2(regenWorld);
        Location pos3 = island.getMaximumPosition1(world);
        Location pos4 = island.getMaximumPosition2(world);

        int minHeight = 0;
        if (XReflection.supports(18)) minHeight = world.getMinHeight();
        final int finalMinHeight = minHeight;
        int maxHeight = world.getMaxHeight();

        // TODO: update this to use an enum
        String paster = IridiumSkyblock.getInstance().getConfiguration().paster.toLowerCase();

        switch(paster) {

            case "fawe": {
                regenFAWE(world, regenWorld, pos1, pos2, pos3, pos4, finalMinHeight, maxHeight, completableFuture, delay);
                break;
            }

            case "worldedit": {
                regenWE(world, regenWorld, pos1, pos2, pos3, pos4, finalMinHeight, maxHeight, completableFuture, delay);
                break;
            }

            case "internalasync": {}
            default: {
                regenIA(world, regenWorld, pos3, pos4, finalMinHeight, maxHeight, completableFuture, delay);
                break;
            }
        }
    }

    @Override
    public boolean deleteTeam(Island island, User user) {
        IslandDeleteEvent islandDeleteEvent = new IslandDeleteEvent(island, user);
        Bukkit.getPluginManager().callEvent(islandDeleteEvent);
        if (islandDeleteEvent.isCancelled()) return false;

        if (IridiumSkyblock.getInstance().getConfiguration().removeIslandBlocksOnDelete) {
            deleteIslandBlocks(island);
            if (IridiumSkyblock.getInstance().getConfiguration().generatorType.isTerrainGenerator())
                regenerateTerrain(island).join();
        }

        IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().delete(island);
        IridiumSkyblock.getInstance().getIslandManager().clearTeamInventory(island);

        getMembersOnIsland(island).forEach(member -> PlayerUtils.teleportSpawn(member.getPlayer()));

        return true;
    }

    @Override
    public synchronized boolean getTeamPermission(Island island, int rank, String permission) {
        if (rank == Rank.OWNER.getId()) return true;
        return IridiumSkyblock.getInstance().getDatabaseManager().getPermissionsTableManager().getEntry(new TeamPermission(island, permission, rank, true))
                .map(TeamPermission::isAllowed)
                .orElse(IridiumSkyblock.getInstance().getPermissionList().get(permission).getDefaultRank() <= rank);
    }

    @Override
    public synchronized void setTeamPermission(Island island, int rank, String permission, boolean allowed) {
        TeamPermission islandPermission = new TeamPermission(island, permission, rank, allowed);
        Optional<TeamPermission> teamPermission = IridiumSkyblock.getInstance().getDatabaseManager().getPermissionsTableManager().getEntry(islandPermission);
        if (teamPermission.isPresent()) {
            teamPermission.get().setAllowed(allowed);
        } else {
            IridiumSkyblock.getInstance().getDatabaseManager().getPermissionsTableManager().addEntry(islandPermission);
        }
    }

    @Override
    public Optional<TeamInvite> getTeamInvite(Island team, User user) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getInvitesTableManager().getEntry(new TeamInvite(team, user.getUuid(), user.getUuid()));
    }

    @Override
    public List<TeamInvite> getTeamInvites(Island team) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getInvitesTableManager().getEntries(team);
    }

    @Override
    public void createTeamInvite(Island team, User user, User invitee) {
        IridiumSkyblock.getInstance().getDatabaseManager().getInvitesTableManager().addEntry(new TeamInvite(team, user.getUuid(), invitee.getUuid()));
    }

    @Override
    public void deleteTeamInvite(TeamInvite teamInvite) {
        IridiumSkyblock.getInstance().getDatabaseManager().getInvitesTableManager().delete(teamInvite);
    }

    @Override
    public Optional<TeamTrust> getTeamTrust(Island island, User user) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getTrustTableManager().getEntry(new TeamTrust(island, user.getUuid(), user.getUuid()));
    }

    @Override
    public List<TeamTrust> getTeamTrusts(Island island) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getTrustTableManager().getEntries(island);
    }

    @Override
    public void createTeamTrust(Island island, User user, User invitee) {
        IridiumSkyblock.getInstance().getDatabaseManager().getTrustTableManager().addEntry(new TeamTrust(island, user.getUuid(), invitee.getUuid()));
    }

    @Override
    public void deleteTeamTrust(TeamTrust teamTrust) {
        IridiumSkyblock.getInstance().getDatabaseManager().getTrustTableManager().delete(teamTrust);
    }

    @Override
    public synchronized TeamBank getTeamBank(Island island, String bankItem) {
        Optional<TeamBank> teamBank = IridiumSkyblock.getInstance().getDatabaseManager().getBankTableManager().getEntry(new TeamBank(island, bankItem, 0));
        if (teamBank.isPresent()) {
            return teamBank.get();
        } else {
            TeamBank bank = new TeamBank(island, bankItem, 0);
            IridiumSkyblock.getInstance().getDatabaseManager().getBankTableManager().addEntry(bank);
            return bank;
        }
    }

    @Override
    public synchronized TeamSpawners getTeamSpawners(Island island, EntityType entityType) {
        Optional<TeamSpawners> teamSpawner = IridiumSkyblock.getInstance().getDatabaseManager().getTeamSpawnerTableManager().getEntry(new TeamSpawners(island, entityType, 0));
        if (teamSpawner.isPresent()) {
            return teamSpawner.get();
        } else {
            TeamSpawners spawner = new TeamSpawners(island, entityType, 0);
            IridiumSkyblock.getInstance().getDatabaseManager().getTeamSpawnerTableManager().addEntry(spawner);
            return spawner;
        }
    }

    @Override
    public TeamSpawners getTeamSpawners(Island island, XEntityType xEntityType) {
        Optional<TeamSpawners> teamSpawner = IridiumSkyblock.getInstance().getDatabaseManager().getTeamSpawnerTableManager().getEntry(new TeamSpawners(island, xEntityType, 0));
        if (teamSpawner.isPresent()) {
            return teamSpawner.get();
        } else {
            TeamSpawners spawner = new TeamSpawners(island, xEntityType, 0);
            IridiumSkyblock.getInstance().getDatabaseManager().getTeamSpawnerTableManager().addEntry(spawner);
            return spawner;
        }
    }

    @Override
    public synchronized TeamBlock getTeamBlock(Island island, XMaterial xMaterial) {
        Optional<TeamBlock> teamBlock = IridiumSkyblock.getInstance().getDatabaseManager().getTeamBlockTableManager().getEntry(new TeamBlock(island, xMaterial, 0));
        if (teamBlock.isPresent()) {
            return teamBlock.get();
        } else {
            TeamBlock block = new TeamBlock(island, xMaterial, 0);
            IridiumSkyblock.getInstance().getDatabaseManager().getTeamBlockTableManager().addEntry(block);
            return block;
        }
    }

    @Override
    public synchronized @Nullable TeamSetting getTeamSetting(Island island, String settingKey) {
        Setting settingConfig = IridiumSkyblock.getInstance().getSettingsList().get(settingKey);
        if (settingConfig == null) {
            return null;
        }
        String defaultValue = settingConfig.getDefaultValue();
        Optional<TeamSetting> teamSetting = IridiumSkyblock.getInstance().getDatabaseManager().getTeamSettingsTableManager().getEntry(new TeamSetting(island, settingKey, defaultValue));
        if (teamSetting.isPresent()) {
            return teamSetting.get();
        } else {
            TeamSetting setting = new TeamSetting(island, settingKey, defaultValue);
            IridiumSkyblock.getInstance().getDatabaseManager().getTeamSettingsTableManager().addEntry(setting);
            return setting;
        }
    }

    @Override
    public CompletableFuture<Void> saveTeamLog(TeamLog teamLog) {
        return CompletableFuture.runAsync(() -> IridiumSkyblock.getInstance().getDatabaseManager().getTeamLogsTableManager().save(teamLog));
    }

    @Override
    public CompletableFuture<List<TeamLog>> getTeamLogs(Island island, int limit, int page, String action, UUID user) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SqlTableManager<TeamLog, Integer> sqlTableManager = IridiumSkyblock.getInstance()
                        .getDatabaseManager()
                        .getTeamLogsTableManager();

                QueryBuilder<TeamLog, Integer> qb = sqlTableManager.getDao().queryBuilder();
                Where<TeamLog, Integer> where = qb.where();

                where.eq("team_id", island.getId());

                if (action != null) {
                    where.and();
                    where.eq("action", LogType.valueOf(action.toUpperCase()));
                }

                if (user != null) {
                    where.and();
                    where.or(
                            where.eq("user", user),
                            where.eq("other_user", user)
                    );
                }

                int offset = limit * (page - 1);

                qb.orderBy("time", false);

                qb.limit((long) limit);
                qb.offset((long) offset);

                return sqlTableManager.query(qb.prepare());

            } catch (Exception e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        });
    }

    @Override
    public CompletableFuture<Integer> getTeamLogsMaxPage(Island island, int pageSize, String action, UUID user) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SqlTableManager<TeamLog, Integer> sqlTableManager = IridiumSkyblock.getInstance()
                        .getDatabaseManager()
                        .getTeamLogsTableManager();

                QueryBuilder<TeamLog, Integer> qb = sqlTableManager.getDao().queryBuilder();
                qb.setCountOf(true);

                Where<TeamLog, Integer> where = qb.where();

                where.eq("team_id", island.getId());

                // Optional action
                if (action != null) {
                    where.and();
                    where.eq("action", LogType.valueOf(action.toUpperCase()));
                }

                // Optional user
                if (user != null) {
                    where.and();
                    where.or(
                            where.eq("user", user),
                            where.eq("other_user", user)
                    );
                }

                long rowCount = sqlTableManager.getDao().countOf(qb.prepare());

                return (int) Math.ceil((double) rowCount / (double) pageSize);

            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        });
    }

    @Override
    public synchronized TeamEnhancement getTeamEnhancement(Island island, String enhancementName) {
        Optional<TeamEnhancement> teamEnhancement = IridiumSkyblock.getInstance().getDatabaseManager().getEnhancementTableManager().getEntry(new TeamEnhancement(island, enhancementName, 0));
        if (teamEnhancement.isPresent()) {
            return teamEnhancement.get();
        } else {
            TeamEnhancement enhancement = new TeamEnhancement(island, enhancementName, 0);
            IridiumSkyblock.getInstance().getDatabaseManager().getEnhancementTableManager().addEntry(enhancement);
            return enhancement;
        }
    }

    private HashMap<XMaterial, Integer> getBlockStacks(Chunk chunk, Island island) {
        HashMap<XMaterial, Integer> hashMap = new HashMap<>();

        for (StackerSupport<Island> stackerSupport : IridiumSkyblock.getInstance().getSupportManager().getStackerSupport()) {
            stackerSupport.getBlocksStacked(chunk, island).forEach((key, value) -> hashMap.put(key, hashMap.getOrDefault(key, 0) + value));
        }

        return hashMap;
    }

    private CompletableFuture<Integer> getSpawnerStackAmount(CreatureSpawner creatureSpawner) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> {
            completableFuture.complete(IridiumSkyblock.getInstance().getSupportManager().getSpawnerSupport().stream()
                    .mapToInt(stackerSupport -> stackerSupport.getStackAmount(creatureSpawner))
                    .max()
                    .orElse(1));
        });
        return completableFuture;
    }

    @Override
    public CompletableFuture<Void> recalculateTeam(Island island) {
        Map<XMaterial, Integer> teamBlocks = new HashMap<>();
        Map<EntityType, Integer> teamSpawners = new HashMap<>();
        return CompletableFuture.runAsync(() -> {
            List<Chunk> chunks = getIslandChunks(island).join();
            for (Chunk chunk : chunks) {
                ChunkSnapshot chunkSnapshot = chunk.getChunkSnapshot(true, false, false);
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        final int maxy = chunkSnapshot.getHighestBlockYAt(x, z);
                        for (int y = 0; y <= maxy; y++) {
                            if (island.isInIsland(x + (chunkSnapshot.getX() * 16), z + (chunkSnapshot.getZ() * 16))) {
                                XMaterial material = XMaterial.matchXMaterial(chunkSnapshot.getBlockType(x, y, z));
                                teamBlocks.put(material, teamBlocks.getOrDefault(material, 0) + 1);
                            }
                        }
                    }
                }
                getBlockStacks(chunk, island).forEach((key, value) -> {
                    teamBlocks.put(key, teamBlocks.getOrDefault(key, 0) + value);
                });

                getSpawners(chunk, island).join().forEach(creatureSpawner -> {
                    int amount = getSpawnerStackAmount(creatureSpawner).join();
                    teamSpawners.put(creatureSpawner.getSpawnedType(), teamSpawners.getOrDefault(creatureSpawner.getSpawnedType(), 0) + amount);
                });
            }
        }).thenRun(() -> Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> {
            List<TeamBlock> blocks = IridiumSkyblock.getInstance().getDatabaseManager().getTeamBlockTableManager().getEntries(island);
            List<TeamSpawners> spawners = IridiumSkyblock.getInstance().getDatabaseManager().getTeamSpawnerTableManager().getEntries(island);
            for (TeamBlock teamBlock : blocks) {
                teamBlock.setAmount(teamBlocks.getOrDefault(teamBlock.getXMaterial(), 0));
            }
            for (TeamSpawners teamSpawner : spawners) {
                teamSpawner.setAmount(teamSpawners.getOrDefault(teamSpawner.getEntityType(), 0));
            }
        }));
    }

    public CompletableFuture<List<CreatureSpawner>> getSpawners(Chunk chunk, Island island) {
        CompletableFuture<List<CreatureSpawner>> completableFuture = new CompletableFuture<>();
        Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> {
            List<CreatureSpawner> creatureSpawners = new ArrayList<>();
            for (BlockState blockState : chunk.getTileEntities()) {
                if (!island.isInIsland(blockState.getLocation())) continue;
                if (!(blockState instanceof CreatureSpawner)) continue;
                creatureSpawners.add((CreatureSpawner) blockState);
            }
            completableFuture.complete(creatureSpawners);
        });
        return completableFuture;
    }

    public CompletableFuture<List<Chunk>> getIslandChunks(Island island) {
        List<World> worlds = Stream.of(getWorld(World.Environment.NORMAL), getWorld(World.Environment.NETHER), getWorld(World.Environment.THE_END))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return CompletableFuture.supplyAsync(() -> {
            List<CompletableFuture<Chunk>> chunks = new ArrayList<>();

            for (World world : worlds) {
                Location pos1 = island.getPosition1(world);
                Location pos2 = island.getPosition2(world);

                int minX = pos1.getBlockX() >> 4;
                int minZ = pos1.getBlockZ() >> 4;
                int maxX = pos2.getBlockX() >> 4;
                int maxZ = pos2.getBlockZ() >> 4;

                for (int x = minX; x <= maxX; x++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        chunks.add(IridiumSkyblock.getInstance().getMultiVersion().getChunkAt(pos1.getWorld(), x, z));
                    }
                }
            }
            return chunks.stream().map(CompletableFuture::join).collect(Collectors.toList());
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return Collections.emptyList();
        });
    }

    @Override
    public void createWarp(Island island, UUID creator, Location location, String name, String password) {
        IridiumSkyblock.getInstance().getDatabaseManager().getTeamWarpTableManager().addEntry(new TeamWarp(island, creator, location, name, password));
    }

    @Override
    public void deleteWarp(TeamWarp teamWarp) {
        IridiumSkyblock.getInstance().getDatabaseManager().getTeamWarpTableManager().delete(teamWarp);
    }

    @Override
    public List<TeamWarp> getTeamWarps(Island island) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getTeamWarpTableManager().getEntries(island);
    }

    @Override
    public Optional<TeamWarp> getTeamWarp(Island island, String name) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getTeamWarpTableManager().getEntry(new TeamWarp(island, UUID.randomUUID(), null, name));
    }

    @Override
    public List<TeamMission> getTeamMissions(Island island) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getTeamMissionTableManager().getEntries(island);
    }

    @Override
    public synchronized TeamMission getTeamMission(Island island, String missionName) {
        Mission mission = IridiumSkyblock.getInstance().getMissions().missions.get(missionName);
        LocalDateTime localDateTime = IridiumSkyblock.getInstance().getMissionManager().getExpirationTime(mission == null ? MissionType.ONCE : mission.getMissionType(), LocalDateTime.now());

        TeamMission newTeamMission = new TeamMission(island, missionName, localDateTime);
        Optional<TeamMission> teamMission = IridiumSkyblock.getInstance().getDatabaseManager().getTeamMissionTableManager().getEntry(newTeamMission);
        if (teamMission.isPresent()) {
            return teamMission.get();
        } else {
            //TODO need to consider reworking this, it could generate some lag
            IridiumSkyblock.getInstance().getDatabaseManager().getTeamMissionTableManager().save(newTeamMission);
            IridiumSkyblock.getInstance().getDatabaseManager().getTeamMissionTableManager().addEntry(newTeamMission);
            return newTeamMission;
        }
    }

    @Override
    public synchronized TeamMissionData getTeamMissionData(TeamMission teamMission, int missionIndex) {
        Optional<TeamMissionData> teamMissionData = IridiumSkyblock.getInstance().getDatabaseManager().getTeamMissionDataTableManager().getEntry(new TeamMissionData(teamMission, missionIndex));
        if (teamMissionData.isPresent()) {
            return teamMissionData.get();
        } else {
            TeamMissionData missionData = new TeamMissionData(teamMission, missionIndex);
            IridiumSkyblock.getInstance().getDatabaseManager().getTeamMissionDataTableManager().addEntry(missionData);
            return missionData;
        }
    }

    @Override
    public List<TeamMissionData> getTeamMissionData(TeamMission teamMission) {
        MissionData missionData = IridiumSkyblock.getInstance().getMissions().missions.get(teamMission.getMissionName()).getMissionData().get(teamMission.getMissionLevel());

        List<TeamMissionData> list = new ArrayList<>();
        for (int i = 0; i < missionData.getMissions().size(); i++) {
            list.add(getTeamMissionData(teamMission, i));
        }
        return list;
    }

    @Override
    public void deleteTeamMission(TeamMission teamMission) {
        IridiumSkyblock.getInstance().getDatabaseManager().getTeamMissionTableManager().delete(teamMission);
    }

    @Override
    public void deleteTeamMissionData(TeamMission teamMission) {
        MissionData missionData = IridiumSkyblock.getInstance().getMissions().missions.get(teamMission.getMissionName()).getMissionData().get(teamMission.getMissionLevel());

        for (int i = 0; i < missionData.getMissions().size(); i++) {
            Optional<TeamMissionData> data = IridiumSkyblock.getInstance().getDatabaseManager().getTeamMissionDataTableManager().getEntry(new TeamMissionData(teamMission, i));
            data.ifPresent(teamMissionData -> IridiumSkyblock.getInstance().getDatabaseManager().getTeamMissionDataTableManager().delete(teamMissionData));
        }
    }

    @Override
    public List<TeamReward> getTeamRewards(Island island) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getTeamRewardsTableManager().getEntries(island);
    }

    @Override
    public Optional<TeamReward> getTeamReward(int id) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getTeamRewardsTableManager().getEntry(new TeamReward(id));
    }

    @Override
    public void addTeamReward(TeamReward teamReward) {
        CompletableFuture.runAsync(() -> IridiumSkyblock.getInstance().getDatabaseManager().getTeamRewardsTableManager().save(teamReward)).thenRun(() ->
                IridiumSkyblock.getInstance().getDatabaseManager().getTeamRewardsTableManager().addEntry(teamReward)
        );
    }

    @Override
    public void deleteTeamReward(TeamReward teamReward) {
        IridiumSkyblock.getInstance().getDatabaseManager().getTeamRewardsTableManager().delete(teamReward);
    }

    public @Nullable World getWorld(World.Environment environment) {
        String worldName = getWorldName(environment);
        if (worldName == null) return null;
        return Bukkit.getWorld(worldName);
    }

    public @Nullable String getWorldName(World.Environment environment) {
        if (!IridiumSkyblock.getInstance().getConfiguration().enabledWorlds.getOrDefault(environment, true))
            return null;
        switch (environment) {
            case NORMAL:
                return IridiumSkyblock.getInstance().getConfiguration().worldName;
            case NETHER:
                return IridiumSkyblock.getInstance().getConfiguration().worldName + "_nether";
            case THE_END:
                return IridiumSkyblock.getInstance().getConfiguration().worldName + "_the_end";
        }
        return null;
    }

    public boolean isInSkyblockWorld(World world) {
        if (world == null) return false;
        return world.getName().equals(getWorldName(World.Environment.NORMAL)) || world.getName().equals(getWorldName(World.Environment.NETHER)) || world.getName().equals(getWorldName(World.Environment.THE_END));
    }

    public void sendIslandBorder(Player player) {
        getTeamViaPlayerLocation(player).ifPresent(island -> {
            final Location centre = island.getCenter(player.getWorld()).clone();

            Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> IridiumSkyblock.getInstance().getNms().sendWorldBorder(player, island.getColor(), island.getSize() + (island.getSize() % 2 == 0 ? 1 : 0), centre));
        });
    }

    public ItemStack getIslandCrystal(int amount) {
        ItemStack itemStack = ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getConfiguration().islandCrystal, Collections.singletonList(
                new Placeholder("amount", String.valueOf(amount))
        ));

        NBT.modify(itemStack, readWriteItemNBT -> {
            readWriteItemNBT.resolveOrCreateCompound("iridiumskyblock").setInteger("islandCrystals", amount);
        });

        return itemStack;
    }

    public int getIslandCrystals(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return 0;

        return NBT.get(itemStack, readableItemNBT -> {
            return readableItemNBT.resolveOrDefault("iridiumskyblock.islandCrystals", 0);
        });
    }

    public List<User> getMembersOnIsland(Island island) {
        return Bukkit.getServer().getOnlinePlayers().stream()
                .map(player -> IridiumSkyblock.getInstance().getUserManager().getUser(player))
                .filter(user -> user.getCurrentIsland().map(Island::getId).orElse(-1) == island.getId())
                .collect(Collectors.toList());
    }

    @Override
    public boolean teleport(Player player, Location location, Island team) {
        Location safeLocation = LocationUtils.getSafeLocation(location, team);
        if (safeLocation == null) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noSafeLocation
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));
            return false;
        }
        player.setFallDistance(0.0F);
        PaperLib.teleportAsync(player, safeLocation);
        return true;
    }

    @Override
    public void handleBlockBreakOutsideTerritory(BlockBreakEvent blockEvent) {
        if (isInSkyblockWorld(blockEvent.getBlock().getWorld())) {
            blockEvent.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotBreakBlocks
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));
            blockEvent.setCancelled(true);
        }
    }

    @Override
    public void handleBlockPlaceOutsideTerritory(BlockPlaceEvent blockEvent) {
        if (isInSkyblockWorld(blockEvent.getBlock().getWorld())) {
            blockEvent.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotPlaceBlocks
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));
            blockEvent.setCancelled(true);
        }
    }

    @Override
    public void handlePlayerInteractOutsideTerritory(PlayerInteractEvent interactEvent) {
        if (interactEvent.getClickedBlock() == null) {
            return;
        }
        if (isInSkyblockWorld(interactEvent.getClickedBlock().getWorld())) {
            interactEvent.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotInteract
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));
            interactEvent.setCancelled(true);
        }
    }


    public void clearTeamInventory(Island island) {

        List<User> onlinePlayers = new ArrayList<>();
        List<User> offlinePlayers = new ArrayList<>();

        for (User user : island.getMembers()) {

            if (user.getUserRank() == -1) continue;

            try {
                user.getPlayer();
                onlinePlayers.add(user);
            } catch (Exception e) {
                offlinePlayers.add(user);
            }
        }

        for (User user : onlinePlayers) {
            if (IridiumSkyblock.getInstance().getConfiguration().clearInventoryOnRegen)
                user.getPlayer().getInventory().clear();
            if (IridiumSkyblock.getInstance().getConfiguration().clearEnderChestOnRegen)
                user.getPlayer().getEnderChest().clear();
        }

        for (User user : offlinePlayers) {

            try {
                File file = new File(Bukkit.getWorlds().get(0).getWorldFolder().getPath() + File.pathSeparator + "playerdata" + File.pathSeparator + user.getUuid() + ".dat");
                NBTFile playerFile = new NBTFile(file);

                if (IridiumSkyblock.getInstance().getConfiguration().clearInventoryOnRegen) {
                    NBTCompound compound = playerFile.getCompound("").getCompound("Inventory");
                    compound.clearNBT();
                    playerFile.save();
                }

                if (IridiumSkyblock.getInstance().getConfiguration().clearEnderChestOnRegen) {
                    NBTCompound compound = playerFile.getCompound("").getCompound("EnderItems");
                    compound.clearNBT();
                    playerFile.save();
                }

            } catch (IOException e) {
                IridiumSkyblock.getInstance().getLogger().warning("Cannot mutate user: " + user.getName() + ". See stacktrace for details.");
                IridiumSkyblock.getInstance().getLogger().warning(e.getMessage());
            } catch (NullPointerException e) {
                IridiumSkyblock.getInstance().getLogger().warning("Cannot mutate user: " + user.getName() + ". Either player or compound doesn't exist (See stacktrace for details).");
                IridiumSkyblock.getInstance().getLogger().warning(e.getMessage());
            }
        }
    }

    // TODO: should consider chunk-batching like w/ WE
    public void deleteIA(World world, Location pos1, Location pos2, int minHeight, int maxHeight, CompletableFuture<Void> completableFuture, int delay) {

        Set<Long> modifiedChunks = new HashSet<>();

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        new BukkitRunnable() {

            int y = maxHeight;
            int x = minX;
            int z = minZ;

            @Override
            public void run() {
                for (int i = 0; i < IridiumSkyblock.getInstance().getConfiguration().pasterLimitPerTick; i++) {

                    Block block = world.getBlockAt(x, y, z);

                    while (block.isEmpty()) {
                        if(increment()) return;
                        block = world.getBlockAt(x, y, z);
                    }

                    if (block.getState() instanceof InventoryHolder) {
                        ((InventoryHolder) block.getState()).getInventory().clear();
                    }
                    block.setType(Material.AIR, false);
                    modifiedChunks.add(((long) (x >> 4) << 32) | ((z >> 4) & 0xFFFFFFFFL));

                    increment();
                }
            }

            public boolean increment() {
                if (++z > maxZ) {
                    z = minZ;
                    if (++x > maxX) {
                        x = minX;
                        if (--y < minHeight) {
                            finish();
                            return true;
                        }
                    }
                }
                return false;
            }

            private void finish() {
                this.cancel();

                completableFuture.complete(null);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!IridiumSkyblock.getInstance().getConfiguration().generatorType.isTerrainGenerator()) {
                            modifiedChunks.forEach(key -> {world.refreshChunk((int) (key >> 32), key.intValue());});
                        }
                    }
                }.runTaskAsynchronously(IridiumSkyblock.getInstance());
            }
        }.runTaskTimer(IridiumSkyblock.getInstance(), delay, 1L);
    }

    public void deleteWE(World world, Location pos1, Location pos2, int minHeight, int maxHeight, CompletableFuture<Void> completableFuture, int delay) {

        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(world);

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld)) {
            BlockVector3 min = BlockVector3.at(pos1.getBlockX(), minHeight, pos1.getBlockZ());
            BlockVector3 max = BlockVector3.at(pos2.getBlockX(), maxHeight, pos2.getBlockZ());
            CuboidRegion selection = new CuboidRegion(min, max);

            editSession.getChangeSet().setRecordChanges(false);
            editSession.setReorderMode(EditSession.ReorderMode.FAST);
            editSession.setSideEffectApplier(SideEffectSet.none());
            ExistingBlockMask nonAirMask = new ExistingBlockMask(editSession);
            editSession.setMask(nonAirMask);

            // grab all points and remove air blocks from the selection
            List<BlockVector3> points = new ArrayList<>();
            for (BlockVector3 point : selection) {
                if (nonAirMask.test(point)) {
                    points.add(point);
                }
            }

            points = points.reversed(); // style points

            // group points into chunks
            Map<BlockVector2, List<BlockVector3>> chunkBatches = new LinkedHashMap<>();
            for (BlockVector3 pt : points) {
                BlockVector2 chunkPos = BlockVector2.at(pt.getX() >> 4, pt.getZ() >> 4);
                chunkBatches.computeIfAbsent(chunkPos, k -> new ArrayList<>()).add(pt);
            }

            // chunk list
            List<Map.Entry<BlockVector2, List<BlockVector3>>> batches = new ArrayList<>(chunkBatches.entrySet());

            new BukkitRunnable() {
                int chunkIndex = 0;
                int blockInsideChunkIndex = 0;
                final int blocksPerTick = IridiumSkyblock.getInstance().getConfiguration().pasterLimitPerTick;
                private final com.sk89q.worldedit.world.block.BlockState air =
                        com.sk89q.worldedit.world.block.BlockTypes.AIR.getDefaultState();

                @Override
                public void run() {
                    int processedThisTick = 0;

                    while (processedThisTick < blocksPerTick) {

                        if (chunkIndex >= batches.size()) {
                            finish();
                            return;
                        }

                        List<BlockVector3> currentChunkBlocks = batches.get(chunkIndex).getValue();

                        while (blockInsideChunkIndex < currentChunkBlocks.size() && processedThisTick < blocksPerTick) {
                            BlockVector3 point = currentChunkBlocks.get(blockInsideChunkIndex);

                            try {
                                editSession.setBlock(point, air);
                            } catch (MaxChangedBlocksException e) {
                                finish();
                                return;
                            }

                            blockInsideChunkIndex++;
                            processedThisTick++;
                        }

                        if (blockInsideChunkIndex >= currentChunkBlocks.size()) {
                            chunkIndex++;
                            blockInsideChunkIndex = 0;
                            editSession.flushSession(); // deprecated, but this pushes changes to the server, so it's critical for performance
                        }
                    }
                }

                private void finish() {
                    this.cancel();

                    completableFuture.complete(null);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!IridiumSkyblock.getInstance().getConfiguration().generatorType.isTerrainGenerator()) {
                                weWorld.fixLighting(selection.getChunks());
                            }
                        }
                    }.runTask(IridiumSkyblock.getInstance());
                }
            }.runTaskTimer(IridiumSkyblock.getInstance(), delay, 1L);
        }
    }

    public void deleteFAWE(World world, Location pos1, Location pos2, int minHeight, int maxHeight, CompletableFuture<Void> completableFuture, int delay) {

        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(world);

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld)) {
            BlockVector3 min = BlockVector3.at(pos1.getBlockX(), minHeight, pos1.getBlockZ());
            BlockVector3 max = BlockVector3.at(pos2.getBlockX(), maxHeight, pos2.getBlockZ());
            CuboidRegion selection = new CuboidRegion(min, max);

            com.sk89q.worldedit.world.block.BlockState air = BlockTypes.AIR.getDefaultState();
            ExistingBlockMask nonAirMask = new ExistingBlockMask(editSession);

            editSession.getChangeSet().setRecordChanges(false);
            editSession.setReorderMode(EditSession.ReorderMode.FAST);
            editSession.setSideEffectApplier(SideEffectSet.none());

            editSession.replaceBlocks(selection, nonAirMask, (Pattern) air); // no style points :(

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!IridiumSkyblock.getInstance().getConfiguration().generatorType.isTerrainGenerator()) {
                        weWorld.fixLighting(selection.getChunks());
                    }
                }
            }.runTask(IridiumSkyblock.getInstance());

        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }

        completableFuture.complete(null);
    }

    // TODO: should consider chunk-batching like w/ WE
    public void regenIA(World world, World regenWorld, Location pos1, Location pos2, int minHeight, int maxHeight, CompletableFuture<Void> completableFuture, int delay) {

        Set<Long> modifiedChunks = new HashSet<>();

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        new BukkitRunnable() {

            int y = minHeight;
            int x = minX;
            int z = minZ;

            @Override
            public void run() {
                for (int i = 0; i < IridiumSkyblock.getInstance().getConfiguration().pasterLimitPerTick; i++) {

                    Block blockA = regenWorld.getBlockAt(x, y, z);
                    Block blockB = world.getBlockAt(x, y, z);

                    while (blockA.isEmpty() || blockA.getBlockData().equals(blockB.getBlockData())) {
                        if (increment()) return;
                        blockA = regenWorld.getBlockAt(x, y, z);
                        blockB = world.getBlockAt(x, y, z);
                    }

                    blockB.setBlockData(blockA.getBlockData(), false);
                    modifiedChunks.add(((long) (x >> 4) << 32) | ((z >> 4) & 0xFFFFFFFFL));

                    increment();
                }
            }

            public boolean increment() {
                if (++z > maxZ) {
                    z = minZ;
                    if (++x > maxX) {
                        x = minX;
                        if (++y > maxHeight) {
                            finish();
                            return true;
                        }
                    }
                }
                return false;
            }

            private void finish() {
                this.cancel();

                completableFuture.complete(null);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        modifiedChunks.forEach(key -> {world.refreshChunk((int) (key >> 32), key.intValue());});
                    }
                }.runTaskAsynchronously(IridiumSkyblock.getInstance());
            }
        }.runTaskTimer(IridiumSkyblock.getInstance(), delay, 1L);
    }

    public void regenWE(World world, World regenWorld, Location pos1, Location pos2, Location pos3, Location pos4, int minHeight, int maxHeight, CompletableFuture<Void> completableFuture, int delay) {

        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(world);
        com.sk89q.worldedit.world.World weRegenWorld = BukkitAdapter.adapt(regenWorld);

        // there's gotta be a more succinct method to do this
        BlockVector3 min = BlockVector3.at(pos1.getBlockX(), minHeight, pos1.getBlockZ());
        BlockVector3 max = BlockVector3.at(pos2.getBlockX(), maxHeight, pos2.getBlockZ());
        CuboidRegion selection = new CuboidRegion(min, max);

        BlockVector3 min2 = BlockVector3.at(pos3.getBlockX(), minHeight, pos3.getBlockZ());
        BlockVector3 max2 = BlockVector3.at(pos4.getBlockX(), maxHeight, pos4.getBlockZ());
        CuboidRegion selection2 = new CuboidRegion(min2, max2);

        BlockArrayClipboard clipboard = new BlockArrayClipboard(selection);

        // copy
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(weRegenWorld)) {
            editSession.getChangeSet().setRecordChanges(false);
            editSession.setReorderMode(EditSession.ReorderMode.FAST);
            editSession.setSideEffectApplier(SideEffectSet.none());
            ExistingBlockMask nonAirMask = new ExistingBlockMask(editSession);
            editSession.setMask(nonAirMask);

            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                    weRegenWorld,
                    selection,
                    clipboard,
                    selection.getMinimumPoint()
            );

            Operations.complete(forwardExtentCopy);

        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }

        // paste
        EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld);
        editSession.getChangeSet().setRecordChanges(false);
        editSession.setReorderMode(EditSession.ReorderMode.FAST);
        editSession.setSideEffectApplier(SideEffectSet.none());

        BlockVector3 offset = selection2.getMinimumPoint().subtract(clipboard.getMinimumPoint());

        Mask clipboardMask = new ExistingBlockMask(clipboard);
        MaskingExtent maskedClipboard = new MaskingExtent(clipboard, clipboardMask);

        // grab all points and remove air blocks from the selection
        List<BlockVector3> points = new ArrayList<>();
        for (BlockVector3 point : clipboard.getRegion()) {
            if (maskedClipboard.getMask().test(point)) {
                points.add(point);
            }
        }

        // group points into chunks
        Map<BlockVector2, List<BlockVector3>> chunkBatches = new LinkedHashMap<>();
        for (BlockVector3 pt : points) {
            BlockVector2 chunkPos = BlockVector2.at(pt.getX() >> 4, pt.getZ() >> 4);
            chunkBatches.computeIfAbsent(chunkPos, k -> new ArrayList<>()).add(pt);
        }

        // chunk list
        List<Map.Entry<BlockVector2, List<BlockVector3>>> batches = new ArrayList<>(chunkBatches.entrySet());

        new BukkitRunnable() {
            int chunkIndex = 0;
            int blockInsideChunkIndex = 0;
            final int blocksPerTick = IridiumSkyblock.getInstance().getConfiguration().pasterLimitPerTick;
            private final com.sk89q.worldedit.world.block.BlockState air =
                    com.sk89q.worldedit.world.block.BlockTypes.AIR.getDefaultState();

            @Override
            public void run() {
                int processedThisTick = 0;

                while (processedThisTick < blocksPerTick) {

                    if (chunkIndex >= batches.size()) {
                        finish();
                        return;
                    }

                    List<BlockVector3> currentChunkBlocks = batches.get(chunkIndex).getValue();

                    while (blockInsideChunkIndex < currentChunkBlocks.size() && processedThisTick < blocksPerTick) {

                        BlockVector3 point = currentChunkBlocks.get(blockInsideChunkIndex);
                        com.sk89q.worldedit.world.block.BlockState block = clipboard.getBlock(point);

                        try {
                            editSession.setBlock(point.add(offset), block);
                        } catch (MaxChangedBlocksException e) {
                            finish();
                            return;
                        }

                        blockInsideChunkIndex++;
                        processedThisTick++;
                    }

                    if (blockInsideChunkIndex >= currentChunkBlocks.size()) {
                        chunkIndex++;
                        blockInsideChunkIndex = 0;
                        editSession.flushSession(); // style points
                    }
                }
            }

            private void finish() {
                this.cancel();

                completableFuture.complete(null);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!IridiumSkyblock.getInstance().getConfiguration().generatorType.isTerrainGenerator()) {
                            weWorld.fixLighting(selection.getChunks());
                        }
                    }
                }.runTask(IridiumSkyblock.getInstance());
            }
        }.runTaskTimer(IridiumSkyblock.getInstance(), delay, 1L);
    }

    public void regenFAWE(World world, World regenWorld, Location pos1, Location pos2, Location pos3, Location pos4, int minHeight, int maxHeight, CompletableFuture<Void> completableFuture, int delay) {

        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(world);
        com.sk89q.worldedit.world.World weRegenWorld = BukkitAdapter.adapt(regenWorld);

        BlockVector3 min = BlockVector3.at(pos1.getBlockX(), minHeight, pos1.getBlockZ());
        BlockVector3 max = BlockVector3.at(pos2.getBlockX(), maxHeight, pos2.getBlockZ());
        CuboidRegion selection = new CuboidRegion(min, max);

        BlockVector3 min2 = BlockVector3.at(pos3.getBlockX(), minHeight, pos3.getBlockZ());
        BlockVector3 max2 = BlockVector3.at(pos4.getBlockX(), maxHeight, pos4.getBlockZ());
        CuboidRegion selection2 = new CuboidRegion(min2, max2);

        BlockArrayClipboard clipboard = new BlockArrayClipboard(selection);

        // copy
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(weRegenWorld)) {
            editSession.getChangeSet().setRecordChanges(false);
            ExistingBlockMask nonAirMask = new ExistingBlockMask(editSession);
            editSession.setMask(nonAirMask);

            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                    weRegenWorld,
                    selection,
                    clipboard,
                    selection.getMinimumPoint()
            );

            Operations.complete(forwardExtentCopy);

        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }

        // paste
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld)) {
            editSession.getChangeSet().setRecordChanges(false);

            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(selection2.getMinimumPoint())
                    .copyBiomes(true)
                    .copyEntities(false)
                    .build();

            Operations.complete(operation);
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }

        completableFuture.complete(null);
        weWorld.fixLighting(selection.getChunks());
    }
}
