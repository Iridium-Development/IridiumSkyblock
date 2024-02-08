package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumcore.dependencies.nbtapi.NBTCompound;
import com.iridium.iridiumcore.dependencies.nbtapi.NBTFile;
import com.iridium.iridiumcore.dependencies.nbtapi.NBTItem;
import com.iridium.iridiumcore.dependencies.paperlib.PaperLib;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.dependencies.xseries.XBiome;
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
import com.iridium.iridiumskyblock.utils.LocationUtils;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import com.iridium.iridiumteams.Rank;
import com.iridium.iridiumteams.Setting;
import com.iridium.iridiumteams.database.*;
import com.iridium.iridiumteams.managers.TeamManager;
import com.iridium.iridiumteams.missions.Mission;
import com.iridium.iridiumteams.missions.MissionData;
import com.iridium.iridiumteams.missions.MissionType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
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

    public void createWorld(World.Environment environment, String name) {
        if (!IridiumSkyblock.getInstance().getConfiguration().enabledWorlds.getOrDefault(environment, true)) return;
        WorldCreator worldCreator = new WorldCreator(name)
                .generator(IridiumSkyblock.getInstance().getDefaultWorldGenerator(name, null))
                .environment(environment);
        World world = Bukkit.createWorld(worldCreator);

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

    public void setIslandBiome(@NotNull Island island, @NotNull XBiome biome) {
        World.Environment dimension = biome.getEnvironment();
        World world = getWorld(dimension);

        if(world == null) return;

        getIslandChunks(island).thenAccept(chunks -> {
            Location pos1 = island.getPosition1(world);
            Location pos2 = island.getPosition2(world);
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

    private CompletableFuture<String> getSchematic(Player player) {
        CompletableFuture<String> schematicNameCompletableFuture = new CompletableFuture<>();
        if (IridiumSkyblock.getInstance().getSchematics().schematics.entrySet().size() == 1) {
            for (Map.Entry<String, Schematics.SchematicConfig> entry : IridiumSkyblock.getInstance().getSchematics().schematics.entrySet()) {
                schematicNameCompletableFuture.complete(entry.getKey());
                return schematicNameCompletableFuture;
            }
        }

        Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> player.openInventory(new CreateGUI(player.getOpenInventory().getTopInventory(), schematicNameCompletableFuture).getInventory()));
        return schematicNameCompletableFuture;
    }

    @Override
    public CompletableFuture<Island> createTeam(@NotNull Player owner, String name) {
        return CompletableFuture.supplyAsync(() -> {
            String schematic = getSchematic(owner).join();
            if (schematic == null) return null;

            User user = IridiumSkyblock.getInstance().getUserManager().getUser(owner);
            Schematics.SchematicConfig schematicConfig = IridiumSkyblock.getInstance().getSchematics().schematics.get(schematic);

            if(schematicConfig.regenCost.money != 0 || !schematicConfig.regenCost.bankItems.isEmpty()) {
                if(!IridiumSkyblock.getInstance().getSchematicManager().buy(owner, schematicConfig)) {
                    return null;
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
            deleteIslandBlocks(island).join();
            clearEntities(island);
            IridiumSkyblock.getInstance().getSchematicManager().pasteSchematic(island, schematicConfig).join();
            setIslandBiome(island, XBiome.matchXBiome(schematicConfig.overworld.biome));
            setIslandBiome(island, XBiome.matchXBiome(schematicConfig.nether.biome));
            setIslandBiome(island, XBiome.matchXBiome(schematicConfig.end.biome));
        });
    }

    private void setHome(Island island, Schematics.SchematicConfig schematicConfig) {
        Location location = island.getCenter(getWorld(World.Environment.NORMAL)).add(schematicConfig.xHome, schematicConfig.yHome, schematicConfig.zHome);
        location.setYaw(schematicConfig.yawHome);
        island.setHome(location);
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
            Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> deleteIslandBlocks(island, world, world.getMaxHeight(), completableFuture, 0));
        }
        return completableFuture;
    }

    private void deleteIslandBlocks(Island island, World world, int y, CompletableFuture<Void> completableFuture, int delay) {
        if (world == null) return;
        Location pos1 = island.getPosition1(world);
        Location pos2 = island.getPosition2(world);

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

        if (y <= LocationUtils.getMinHeight(world)) {
            completableFuture.complete(null);
        } else {
            if (delay < 1) {
                deleteIslandBlocks(island, world, y - 1, completableFuture, delay);
            } else {
                Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> deleteIslandBlocks(island, world, y - 1, completableFuture, delay), delay);
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
    public synchronized TeamSetting getTeamSetting(Island island, String settingKey) {
        Setting settingConfig = IridiumSkyblock.getInstance().getSettingsList().get(settingKey);
        String defaultValue = settingConfig == null ? "" : settingConfig.getDefaultValue();
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
                getSpawners(chunk, island).join().forEach(creatureSpawner ->
                        teamSpawners.put(creatureSpawner.getSpawnedType(), teamSpawners.getOrDefault(creatureSpawner.getSpawnedType(), 0) + 1)
                );
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
    public List<TeamReward> getTeamRewards(Island island) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getTeamRewardsTableManager().getEntries(island);
    }

    @Override
    public void addTeamReward(TeamReward teamReward) {
        IridiumSkyblock.getInstance().getDatabaseManager().getTeamRewardsTableManager().addEntry(teamReward);
    }

    @Override
    public void deleteTeamReward(TeamReward teamReward) {
        IridiumSkyblock.getInstance().getDatabaseManager().getTeamRewardsTableManager().delete(teamReward);
    }

    public @Nullable World getWorld(World.Environment environment) {
        if (!IridiumSkyblock.getInstance().getConfiguration().enabledWorlds.getOrDefault(environment, true))
            return null;
        switch (environment) {
            case NORMAL:
                return Bukkit.getWorld(IridiumSkyblock.getInstance().getConfiguration().worldName);
            case NETHER:
                return Bukkit.getWorld(IridiumSkyblock.getInstance().getConfiguration().worldName + "_nether");
            case THE_END:
                return Bukkit.getWorld(IridiumSkyblock.getInstance().getConfiguration().worldName + "_the_end");
        }
        return null;
    }

    public boolean isInSkyblockWorld(World world) {
        return Objects.equals(world, getWorld(World.Environment.NORMAL)) || Objects.equals(world, getWorld(World.Environment.NETHER)) || Objects.equals(world, getWorld(World.Environment.THE_END));
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

    public void clearTeamInventory(Island island) {

        if (IridiumSkyblock.getInstance().getConfiguration().clearInventoryOnRegen) {
            IridiumSkyblock.getInstance().getIslandManager().getMembersOnIsland(island).forEach(member ->
                    member.getPlayer().getInventory().clear());
        }

        if (IridiumSkyblock.getInstance().getConfiguration().clearEnderChestOnRegen) {
            IridiumSkyblock.getInstance().getIslandManager().getMembersOnIsland(island).forEach(member ->
                    member.getPlayer().getEnderChest().clear());
        }
    }

}
