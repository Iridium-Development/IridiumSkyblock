package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.Rank;
import com.iridium.iridiumteams.database.*;
import com.iridium.iridiumteams.managers.TeamManager;
import com.iridium.iridiumteams.missions.Mission;
import com.iridium.iridiumteams.missions.MissionType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class IslandManager extends TeamManager<Island, User> {

    public IslandManager() {
        super(IridiumSkyblock.getInstance());
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
        //TODO
        return Optional.empty();
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
    public List<Island> getTeams() {
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries();
    }

    @Override
    public List<User> getTeamMembers(Island island) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getUserTableManager().getEntries().stream()
                .filter(user -> user.getTeamID() == island.getId())
                .collect(Collectors.toList());
    }

    @Override
    public CompletableFuture<Island> createTeam(@NotNull Player owner, @NotNull String name) {
        return CompletableFuture.supplyAsync(() -> {
            User user = IridiumSkyblock.getInstance().getUserManager().getUser(owner);
            Island island = new Island(name);

            IridiumSkyblock.getInstance().getDatabaseManager().registerIsland(island).join();

            user.setTeam(island);
            user.setUserRank(Rank.OWNER.getId());

            return island;
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    @Override
    public void deleteTeam(Island island, User user) {
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().delete(island);
    }

    @Override
    public boolean getTeamPermission(Island island, int rank, String permission) {
        if (rank == Rank.OWNER.getId()) return true;
        return IridiumSkyblock.getInstance().getDatabaseManager().getPermissionsTableManager().getEntry(new TeamPermission(island, permission, rank, true))
                .map(TeamPermission::isAllowed)
                .orElse(IridiumSkyblock.getInstance().getPermissionList().get(permission).getDefaultRank() <= rank);
    }

    @Override
    public synchronized void setTeamPermission(Island island, int rank, String permission, boolean allowed) {
        TeamPermission islandPermission = new TeamPermission(island, permission, rank, true);
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
    public TeamEnhancement getTeamEnhancement(Island island, String enhancementName) {
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
        //TODO
        return CompletableFuture.runAsync(() -> {

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
    public TeamMission getTeamMission(Island island, String missionName) {
        Mission mission = IridiumSkyblock.getInstance().getMissions().missions.get(missionName);
        LocalDateTime localDateTime = IridiumSkyblock.getInstance().getMissionManager().getExpirationTime(mission == null ? MissionType.ONCE : mission.getMissionType(), LocalDateTime.now());

        TeamMission newTeamMission = new TeamMission(island, missionName, localDateTime);
        Optional<TeamMission> teamMission = IridiumSkyblock.getInstance().getDatabaseManager().getTeamMissionTableManager().getEntry(newTeamMission);
        if (teamMission.isPresent()) {
            return teamMission.get();
        } else {
            IridiumSkyblock.getInstance().getDatabaseManager().getTeamMissionTableManager().addEntry(newTeamMission);
            return newTeamMission;
        }
    }

    @Override
    public TeamMissionData getTeamMissionData(TeamMission teamMission, int missionIndex) {
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
    public void deleteTeamMission(TeamMission teamMission) {
        IridiumSkyblock.getInstance().getDatabaseManager().getTeamMissionTableManager().delete(teamMission);
    }

    @Override
    public void deleteTeamMissionData(TeamMission teamMission) {
        List<TeamMissionData> teamMissionDataList = IridiumSkyblock.getInstance().getDatabaseManager().getTeamMissionDataTableManager().getEntries().stream()
                .filter(teamMissionData -> teamMissionData.getMissionID() == teamMission.getId())
                .collect(Collectors.toList());
        IridiumSkyblock.getInstance().getDatabaseManager().getTeamMissionDataTableManager().delete(teamMissionDataList);
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
}
