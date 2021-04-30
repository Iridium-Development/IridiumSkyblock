package com.iridium.iridiumskyblock.managers;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.Permission;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.bank.BankItem;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.*;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import io.papermc.lib.PaperLib;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandInviteTableManager().getEntries().stream()
                .filter(islandInvite -> islandInvite.getUser().equals(user) && island.equals(islandInvite.getIsland().orElse(null)))
                .findFirst();
    }

    /**
     * Teleports a player to the Island's home
     *
     * @param player The player we are teleporting
     * @param island The island we are teleporting them to
     */
    public void teleportHome(@NotNull Player player, @NotNull Island island) {
        player.setFallDistance(0);
        PaperLib.teleportAsync(player, island.getHome()).thenRun(() ->
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportingHome.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)))
        );
    }

    /**
     * Creates an island for a specific Player and then teleports them to the island home.
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

        if (getIslandByName(name).isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandWithNameAlreadyExists.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }

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
     * Deletes all blocks in the island and re-pastes the schematic.
     *
     * @param island      The specified Island
     * @param schematicID The ID of the schematic we are pasting
     */
    public void regenerateIsland(@NotNull Island island, @NotNull String schematicID) {
        deleteIslandBlocks(island, IridiumSkyblockAPI.getInstance().getWorld(), 0).thenRun(() ->
                IridiumSkyblock.getInstance().getSchematicManager().pasteSchematic(island, IridiumSkyblockAPI.getInstance().getWorld(), schematicID, 0)
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
                    chunks.add(PaperLib.getChunkAtAsyncUrgently(world, x, z, true));
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
        return IridiumSkyblock.getInstance().getDatabaseManager().getUserTableManager().getEntries().stream().filter(user -> island.equals(user.getIsland().orElse(null))).collect(Collectors.toList());
    }

    /**
     * Gets all IslandInvites for an Island.
     *
     * @param island The island who's invites we are retrieving
     * @return A list of Invites
     */
    public List<IslandInvite> getInvitesByIsland(@NotNull Island island) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandInviteTableManager().getEntries().stream().filter(islandInvite -> island.equals(islandInvite.getIsland().orElse(null))).collect(Collectors.toList());
    }

    /**
     * Finds an Island by its id.
     *
     * @param id The id of the island
     * @return An Optional with the Island, empty if there is none
     */
    public Optional<Island> getIslandById(int id) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries().stream().filter(island -> island.getId() == id).findFirst();
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
        if (!Objects.equals(location.getWorld(), IridiumSkyblockAPI.getInstance().getWorld())) return Optional.empty();
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries().stream().filter(island -> island.isInIsland(location)).findFirst();
    }

    /**
     * Gets whether an IslandRank has the permission on the provided island.
     *
     * @param island     The specified Island
     * @param islandRank The specified Rank
     * @param permission The specified Permission
     * @return If the permission is allowed
     */
    public boolean getIslandPermission(@NotNull Island island, @NotNull IslandRank islandRank, @NotNull Permission permission) {
        Optional<IslandPermission> islandPermission = IridiumSkyblock.getInstance().getDatabaseManager().getIslandPermissionTableManager().getEntries().stream().filter(isPermission -> isPermission.getPermission().equalsIgnoreCase(permission.getName()) && isPermission.getRank().equals(islandRank) && island.equals(isPermission.getIsland().orElse(null))).findFirst();
        return islandPermission.map(IslandPermission::isAllowed).orElseGet(() -> islandRank.getLevel() >= permission.getDefaultRank().getLevel());
    }

    /**
     * Gets weather a permission is allowed or denied.
     *
     * @param island     The specified Island
     * @param user       The Specified User
     * @param permission The Specified permission
     * @return The the permission is allowed
     */
    public boolean getIslandPermission(@NotNull Island island, @NotNull User user, @NotNull Permission permission) {
        IslandRank islandRank = island.equals(user.getIsland().orElse(null)) ? user.getIslandRank() : IslandRank.VISITOR;
        return getIslandPermission(island, islandRank, permission) || user.isBypass();
    }

    /**
     * Gets an Island's bank from BankItem.
     *
     * @param island   The specified Island
     * @param bankItem The BankItem we are getting
     * @return the IslandBank
     */
    public IslandBank getIslandBank(@NotNull Island island, @NotNull BankItem bankItem) {
        Optional<IslandBank> optionalIslandBank = IridiumSkyblock.getInstance().getDatabaseManager().getIslandBankTableManager().getEntries().stream().filter(islandBank -> islandBank.getIsland() == island.getId() && islandBank.getBankItem().equalsIgnoreCase(bankItem.getName())).findFirst();
        if (optionalIslandBank.isPresent()) {
            return optionalIslandBank.get();
        } else {
            IslandBank islandBank = new IslandBank(island, bankItem.getName(), 0);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandBankTableManager().getEntries().add(islandBank);
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
    public Optional<IslandBlocks> getIslandBlock(@NotNull Island island, @NotNull XMaterial material) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandBlocksTableManager().getEntries().stream().filter(islandBlocks -> material.equals(islandBlocks.getMaterial()) && island.equals(islandBlocks.getIsland().orElse(null))).findFirst();
    }

    /**
     * Sets whether a permission is allowed or denied for the specified IslandRank.
     *
     * @param island     The specified Island
     * @param islandRank The specified Rank
     * @param permission The specified Permission
     * @param allowed    If the permission is allowed
     */
    public void setIslandPermission(@NotNull Island island, @NotNull IslandRank islandRank, @NotNull Permission permission, boolean allowed) {
        Optional<IslandPermission> islandPermission = IridiumSkyblock.getInstance().getDatabaseManager().getIslandPermissionTableManager().getEntries().stream().filter(isPermission -> isPermission.getPermission().equalsIgnoreCase(permission.getName()) && isPermission.getRank().equals(islandRank) && island.equals(isPermission.getIsland().orElse(null))).findFirst();
        if (islandPermission.isPresent()) {
            islandPermission.get().setAllowed(allowed);
        } else {
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandPermissionTableManager().getEntries().add(new IslandPermission(island, permission.getName(), islandRank, allowed));
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
                if (world.getBlockAt(x, y, z).getType() != Material.AIR)
                    IridiumSkyblock.getInstance().getNms().setBlockFast(world, x, y, z, 0, (byte) 0, false);
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
     */
    public void deleteIsland(@NotNull Island island) {
        deleteIslandBlocks(island, IridiumSkyblockAPI.getInstance().getWorld(), 3);

        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().delete(island));
        IridiumSkyblock.getInstance().getIslandManager().getIslandMembers(island).forEach(user -> {
            Player player = Bukkit.getPlayer(user.getUuid());
            if (player != null) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandDeleted.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                if (island.isInIsland(player.getLocation())) {
                    PlayerUtils.teleportSpawn(player);
                }
            }
        });
    }

    /**
     * Gets all island missions and creates them if they don't exist.
     *
     * @param island The specified Island
     * @return A list of Island Missions
     */
    public IslandMission getIslandMission(@NotNull Island island, @NotNull Mission mission, @NotNull String missionKey, int missionIndex) {
        Optional<IslandMission> islandMissionOptional = IridiumSkyblock.getInstance().getDatabaseManager().getIslandMissionTableManager().getEntries().stream().filter(isMission -> isMission.getIsland() == island.getId() && isMission.getMissionName().equalsIgnoreCase(missionKey) && isMission.getMissionIndex() == missionIndex - 1).findFirst();
        if (islandMissionOptional.isPresent()) {
            return islandMissionOptional.get();
        } else {
            IslandMission islandMission = new IslandMission(island, mission, missionKey, missionIndex - 1);
            IridiumSkyblock.getInstance().getDatabaseManager().getIslandMissionTableManager().getEntries().add(islandMission);
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
        List<IslandMission> islandMissions = IridiumSkyblock.getInstance().getDatabaseManager().getIslandMissionTableManager().getEntries().stream().filter(islandMission -> islandMission.getIsland() == island.getId() && islandMission.getType() == Mission.MissionType.DAILY).collect(Collectors.toList());

        if (islandMissions.isEmpty()) {
            Random random = new Random();
            List<String> missionList = IridiumSkyblock.getInstance().getMissionsList().keySet().stream().filter(mission -> IridiumSkyblock.getInstance().getMissionsList().get(mission).getMissionType() == Mission.MissionType.DAILY).collect(Collectors.toList());
            for (int i = 0; i < IridiumSkyblock.getInstance().getMissions().dailySlots.size(); i++) {
                String key = missionList.get(random.nextInt(missionList.size()));
                Mission mission = IridiumSkyblock.getInstance().getMissionsList().get(key);
                missionList.remove(key);

                for (int j = 0; j < mission.getMissions().size(); j++) {
                    IridiumSkyblock.getInstance().getDatabaseManager().getIslandMissionTableManager().getEntries().add(new IslandMission(island, mission, key, j));
                }

                missions.put(key, mission);
            }
        } else {
            islandMissions.forEach(islandMission -> missions.put(islandMission.getMissionName(), IridiumSkyblock.getInstance().getMissionsList().get(islandMission.getMissionName())));
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
        IridiumSkyblock.getInstance().getBlockValues().blockValues.keySet().stream().map(material -> IridiumSkyblock.getInstance().getIslandManager().getIslandBlock(island, material)).forEach(islandBlocks -> islandBlocks.ifPresent(blocks -> blocks.setAmount(0)));
        island.setValue(0.00);

        // Calculate and set their new value
        getIslandChunks(island, IridiumSkyblockAPI.getInstance().getWorld()).thenAccept(chunks -> recalculateIsland(island, chunks.stream().map(chunk -> chunk.getChunkSnapshot(true, false, false)).collect(Collectors.toList())));
    }

    /**
     * Recalculates the island async with specified ChunkSnapshots.
     *
     * @param island         The specified Island
     * @param chunkSnapshots The specified ChunkSnapshots
     */
    private void recalculateIsland(@NotNull Island island, @NotNull List<ChunkSnapshot> chunkSnapshots) {
        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () ->
                chunkSnapshots.forEach(chunk -> {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            if (island.isInIsland(x + (chunk.getX() * 16), z + (chunk.getZ() * 16))) {
                                final int maxy = chunk.getHighestBlockYAt(x, z);
                                for (int y = 0; y <= maxy; y++) {
                                    XMaterial material = XMaterial.matchXMaterial(chunk.getBlockType(x, y, z));
                                    if (material.equals(XMaterial.AIR)) continue;

                                    if (IridiumSkyblock.getInstance().getBlockValues().blockValues.containsKey(material)) {
                                        Optional<IslandBlocks> optionalIslandBlock = IridiumSkyblock.getInstance().getIslandManager().getIslandBlock(island, material);

                                        if (optionalIslandBlock.isPresent()) {
                                            optionalIslandBlock.get().setAmount(optionalIslandBlock.get().getAmount() + 1);
                                        } else {
                                            IslandBlocks islandBlocks = new IslandBlocks(island, material);
                                            islandBlocks.setAmount(1);
                                            IridiumSkyblock.getInstance().getDatabaseManager().getIslandBlocksTableManager().getEntries().add(islandBlocks);
                                        }

                                        island.setValue(island.getValue() + IridiumSkyblock.getInstance().getBlockValues().blockValues.get(material).value);
                                    }
                                }
                            }
                        }
                    }
                })
        );
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

        for (String key : IridiumSkyblock.getInstance().getMissionsList().keySet()) {
            Mission mission = IridiumSkyblock.getInstance().getMissionsList().get(key);
            boolean completedBefore = true;

            for (int i = 1; i <= mission.getMissions().size(); i++) {
                String[] conditions = mission.getMissions().get(i - 1).toUpperCase().split(":");
                // If the conditions are the same length (+1 because missionConditions doesn't include amount)
                if (missionConditions.length + 1 != conditions.length) break;

                // Check if this is a mission we want to increment
                boolean matches = matchesMission(missionConditions, conditions);
                if (!matches) continue;

                IslandMission islandMission = IridiumSkyblock.getInstance().getIslandManager().getIslandMission(island, mission, key, i);
                String number = conditions[missionData.split(":").length];

                // Validate the required number for this condition
                if (number.matches("^[0-9]+$")) {
                    int amount = Integer.parseInt(number);
                    if (islandMission.getProgress() >= amount) break;
                    completedBefore = false;
                    islandMission.setProgress(Math.min(islandMission.getProgress() + increment, amount));
                } else {
                    IridiumSkyblock.getInstance().getLogger().warning("Unknown format " + mission.getMissions().get(i - 1));
                    IridiumSkyblock.getInstance().getLogger().warning(number + " Is not a number");
                }
            }

            // Check if this mission is now completed
            if (!completedBefore && hasCompletedMission(island, mission, key)) {
                island.getMembers().stream().map(user -> Bukkit.getPlayer(user.getUuid())).filter(Objects::nonNull).forEach(player -> {
                    mission.getMessage().stream().map(string -> StringUtils.color(string.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))).forEach(player::sendMessage);
                    mission.getCompleteSound().play(player);
                    IridiumSkyblock.getInstance().getDatabaseManager().getIslandRewardTableManager().getEntries().add(new IslandReward(island, mission.getReward()));
                });
            }
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

}
