package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.SQL;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.LostItems;
import com.iridium.iridiumskyblock.managers.tablemanagers.*;
import com.iridium.iridiumteams.database.*;
import com.iridium.iridiumteams.database.types.*;
import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.logger.NullLogBackend;
import com.j256.ormlite.support.ConnectionSource;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

@Getter
public class DatabaseManager {

    @Getter(AccessLevel.NONE)
    private ConnectionSource connectionSource;

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

    public void init() throws SQLException {
        LoggerFactory.setLogBackendFactory(new NullLogBackend.NullLogBackendFactory());

        SQL sqlConfig = IridiumSkyblock.getInstance().getSql();
        String databaseURL = getDatabaseURL(sqlConfig);

        DataPersisterManager.registerDataPersisters(XMaterialType.getSingleton());
        DataPersisterManager.registerDataPersisters(LocationType.getSingleton());
        DataPersisterManager.registerDataPersisters(InventoryType.getSingleton());
        DataPersisterManager.registerDataPersisters(LocalDateTimeType.getSingleton());
        DataPersisterManager.registerDataPersisters(RewardType.getSingleton(IridiumSkyblock.getInstance()));

        this.connectionSource = new JdbcConnectionSource(
                databaseURL,
                sqlConfig.username,
                sqlConfig.password,
                DatabaseTypeUtils.createDatabaseType(databaseURL)
        );

        this.userTableManager = new UserTableManager(connectionSource);
        this.islandTableManager = new IslandTableManager(connectionSource);
        this.lostItemsTableManager = new LostItemsTableManager(connectionSource, LostItems.class);
        this.teamMissionDataTableManager = new TableManager<>(teamMissionData -> getDatabaseKey(teamMissionData.getMissionID(), teamMissionData.getMissionIndex()), connectionSource, TeamMissionData.class);
        this.invitesTableManager = new ForeignIslandTableManager<>(teamInvite -> getDatabaseKey(teamInvite.getTeamID(), teamInvite.getUser()), connectionSource, TeamInvite.class);
        this.trustTableManager = new ForeignIslandTableManager<>(teamTrust -> getDatabaseKey(teamTrust.getTeamID(), teamTrust.getUser()), connectionSource, TeamTrust.class);
        this.permissionsTableManager = new ForeignIslandTableManager<>(teamPermission -> getDatabaseKey(teamPermission.getTeamID(), teamPermission.getPermission(), teamPermission.getRank()), connectionSource, TeamPermission.class);
        this.bankTableManager = new ForeignIslandTableManager<>(teamBank -> getDatabaseKey(teamBank.getTeamID(), teamBank.getBankItem()), connectionSource, TeamBank.class);
        this.enhancementTableManager = new ForeignIslandTableManager<>(teamEnhancement -> getDatabaseKey(teamEnhancement.getTeamID(), teamEnhancement.getEnhancementName()), connectionSource, TeamEnhancement.class);
        this.teamBlockTableManager = new ForeignIslandTableManager<>(teamBlock -> getDatabaseKey(teamBlock.getTeamID(), teamBlock.getXMaterial().name()), connectionSource, TeamBlock.class);
        this.teamSpawnerTableManager = new ForeignIslandTableManager<>(teamSpawner -> getDatabaseKey(teamSpawner.getTeamID(), teamSpawner.getEntityType().name()), connectionSource, TeamSpawners.class);
        this.teamWarpTableManager = new ForeignIslandTableManager<>(teamWarp -> getDatabaseKey(teamWarp.getTeamID(), teamWarp.getName()), connectionSource, TeamWarp.class);
        this.teamMissionTableManager = new ForeignIslandTableManager<>(teamMission -> getDatabaseKey(teamMission.getTeamID(), teamMission.getMissionName()), connectionSource, TeamMission.class);
        this.teamRewardsTableManager = new ForeignIslandTableManager<>(teamRewards -> getDatabaseKey(teamRewards.getId()), connectionSource, TeamReward.class);
        this.teamSettingsTableManager = new ForeignIslandTableManager<>(teamSetting -> getDatabaseKey(teamSetting.getTeamID(), teamSetting.getSetting()), connectionSource, TeamSetting.class);
    }

    private String getDatabaseKey(Object... params) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Object obj : params) {
            stringBuilder.append(obj);
            stringBuilder.append("-");
        }

        return stringBuilder.toString();
    }


    /**
     * Database connection String used for establishing a connection.
     *
     * @return The database URL String
     */
    private @NotNull String getDatabaseURL(SQL sqlConfig) {
        switch (sqlConfig.driver) {
            case MYSQL:
                return "jdbc:" + sqlConfig.driver.name().toLowerCase() + "://" + sqlConfig.host + ":" + sqlConfig.port + "/" + sqlConfig.database + "?useSSL=" + sqlConfig.useSSL;
            case SQLITE:
                return "jdbc:sqlite:" + new File(IridiumSkyblock.getInstance().getDataFolder(), sqlConfig.database + ".db");
            default:
                throw new UnsupportedOperationException("Unsupported driver (how did we get here?): " + sqlConfig.driver.name());
        }
    }

    public CompletableFuture<Void> registerIsland(Island island) {
        return CompletableFuture.runAsync(() -> {
            // Saving the object will also assign the Island's ID
            islandTableManager.save(island);

            islandTableManager.addEntry(island);
        });
    }

}
