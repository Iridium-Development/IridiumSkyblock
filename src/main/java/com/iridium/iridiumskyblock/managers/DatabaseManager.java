package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.LostItems;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.databaseadapter.DatabaseAdapterFactory;
import com.iridium.iridiumskyblock.managers.tablemanagers.*;
import com.iridium.iridiumteams.database.*;
import lombok.Getter;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

@Getter
public class DatabaseManager {

    private UserTableManager userTableManager;
    private IslandTableManager islandTableManager;
    private LostItemsTableManager lostItemsTableManager;
    private TableManager<String, TeamMissionData, Integer> teamMissionDataTableManager;
    private ForeignIslandTableManager<String, TeamInvite> invitesTableManager;
    private ForeignIslandTableManager<String, TeamTrust> trustTableManager;
    private ForeignIslandTableManager<String, TeamPermission> permissionsTableManager;
    private ForeignIslandTableManager<String, TeamBank> bankTableManager;
    private ForeignIslandTableManager<String, TeamEnhancement> enhancementTableManager;
    private ForeignIslandTableManager<String, TeamBlock> teamBlockTableManager;
    private ForeignIslandTableManager<String, TeamSpawners> teamSpawnerTableManager;
    private ForeignIslandTableManager<String, TeamWarp> teamWarpTableManager;
    private ForeignIslandTableManager<String, TeamMission> teamMissionTableManager;
    private ForeignIslandTableManager<String, TeamReward> teamRewardsTableManager;
    private ForeignIslandTableManager<String, TeamSetting> teamSettingsTableManager;

    private final DatabaseAdapterFactory databaseAdapterFactory = new DatabaseAdapterFactory();

    public void init() throws SQLException {
        databaseAdapterFactory.init();

        this.userTableManager = new UserTableManager(databaseAdapterFactory.CreateDatabaseAdapter(User.class));
        this.islandTableManager = new IslandTableManager(databaseAdapterFactory.CreateDatabaseAdapter(Island.class));
        this.lostItemsTableManager = new LostItemsTableManager(databaseAdapterFactory.CreateDatabaseAdapter(LostItems.class));
        this.teamMissionDataTableManager = new TableManager<>(teamMissionData -> teamMissionData.getMissionID()+"-"+teamMissionData.getMissionIndex() ,databaseAdapterFactory.CreateDatabaseAdapter(TeamMissionData.class));
        this.invitesTableManager = new ForeignIslandTableManager<>(teamInvite -> teamInvite.getTeamID()+"-"+teamInvite.getUser().toString(), databaseAdapterFactory.CreateDatabaseAdapter(TeamInvite.class));
        this.trustTableManager = new ForeignIslandTableManager<>(teamTrust -> teamTrust.getTeamID()+"-"+teamTrust.getUser().toString(), databaseAdapterFactory.CreateDatabaseAdapter(TeamTrust.class));
        this.permissionsTableManager = new ForeignIslandTableManager<>(teamPermission -> teamPermission.getTeamID()+"-"+teamPermission.getPermission()+"-"+teamPermission.getRank(), databaseAdapterFactory.CreateDatabaseAdapter(TeamPermission.class));
        this.bankTableManager = new ForeignIslandTableManager<>(teamBank -> teamBank.getTeamID()+"-"+teamBank.getBankItem(), databaseAdapterFactory.CreateDatabaseAdapter(TeamBank.class));
        this.enhancementTableManager = new ForeignIslandTableManager<>(teamEnhancement -> teamEnhancement.getTeamID()+"-"+teamEnhancement.getEnhancementName(), databaseAdapterFactory.CreateDatabaseAdapter(TeamEnhancement.class));
        this.teamBlockTableManager = new ForeignIslandTableManager<>(teamBlock -> teamBlock.getTeamID()+"-"+teamBlock.getXMaterial().name(), databaseAdapterFactory.CreateDatabaseAdapter(TeamBlock.class));
        this.teamSpawnerTableManager = new ForeignIslandTableManager<>(teamSpawner -> teamSpawner.getTeamID()+"-"+teamSpawner.getEntityType().name(), databaseAdapterFactory.CreateDatabaseAdapter(TeamSpawners.class));
        this.teamWarpTableManager = new ForeignIslandTableManager<>(teamWarp -> teamWarp.getTeamID()+"-"+teamWarp.getName(), databaseAdapterFactory.CreateDatabaseAdapter(TeamWarp.class));
        this.teamMissionTableManager = new ForeignIslandTableManager<>(teamMission -> teamMission.getTeamID()+"-"+teamMission.getMissionName(), databaseAdapterFactory.CreateDatabaseAdapter(TeamMission.class));
        this.teamRewardsTableManager = new ForeignIslandTableManager<>(teamRewards -> String.valueOf(teamRewards.getId()), databaseAdapterFactory.CreateDatabaseAdapter(TeamReward.class));
        this.teamSettingsTableManager = new ForeignIslandTableManager<>(teamSetting -> teamSetting.getTeamID()+"-"+teamSetting.getSetting(), databaseAdapterFactory.CreateDatabaseAdapter(TeamSetting.class));
    }

    public CompletableFuture<Void> registerIsland(Island island) {
        return CompletableFuture.runAsync(() -> {
            // Saving the object will also assign the Island's ID
            islandTableManager.save(island);

            islandTableManager.addEntry(island);
        });
    }

}
