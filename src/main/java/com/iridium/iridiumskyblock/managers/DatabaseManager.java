package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.SQL;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.managers.tablemanagers.ForeignIslandTableManager;
import com.iridium.iridiumskyblock.managers.tablemanagers.IslandTableManager;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.iridium.iridiumskyblock.managers.tablemanagers.UserTableManager;
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
    private ForeignIslandTableManager<TeamInvite, Integer> invitesTableManager;
    private ForeignIslandTableManager<TeamPermission, Integer> permissionsTableManager;
    private ForeignIslandTableManager<TeamBank, Integer> bankTableManager;
    private ForeignIslandTableManager<TeamEnhancement, Integer> enhancementTableManager;
    private ForeignIslandTableManager<TeamBlock, Integer> teamBlockTableManager;
    private ForeignIslandTableManager<TeamSpawners, Integer> teamSpawnerTableManager;
    private ForeignIslandTableManager<TeamWarp, Integer> teamWarpTableManager;
    private ForeignIslandTableManager<TeamMission, Integer> teamMissionTableManager;
    private TableManager<TeamMissionData, Integer> teamMissionDataTableManager;
    private ForeignIslandTableManager<TeamReward, Integer> teamRewardsTableManager;
    private ForeignIslandTableManager<TeamSetting, Integer> teamSettingsTableManager;

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
        this.invitesTableManager = new ForeignIslandTableManager<>(connectionSource, TeamInvite.class, Comparator.comparing(TeamInvite::getTeamID).thenComparing(TeamInvite::getUser));
        this.permissionsTableManager = new ForeignIslandTableManager<>(connectionSource, TeamPermission.class, Comparator.comparing(TeamPermission::getTeamID).thenComparing(TeamPermission::getPermission));
        this.bankTableManager = new ForeignIslandTableManager<>(connectionSource, TeamBank.class, Comparator.comparing(TeamBank::getTeamID).thenComparing(TeamBank::getBankItem));
        this.enhancementTableManager = new ForeignIslandTableManager<>(connectionSource, TeamEnhancement.class, Comparator.comparing(TeamEnhancement::getTeamID).thenComparing(TeamEnhancement::getEnhancementName));
        this.teamBlockTableManager = new ForeignIslandTableManager<>(connectionSource, TeamBlock.class, Comparator.comparing(TeamBlock::getTeamID).thenComparing(TeamBlock::getXMaterial));
        this.teamSpawnerTableManager = new ForeignIslandTableManager<>(connectionSource, TeamSpawners.class, Comparator.comparing(TeamSpawners::getTeamID).thenComparing(TeamSpawners::getEntityType));
        this.teamWarpTableManager = new ForeignIslandTableManager<>(connectionSource, TeamWarp.class, Comparator.comparing(TeamWarp::getTeamID).thenComparing(TeamWarp::getName));
        this.teamMissionTableManager = new ForeignIslandTableManager<>(connectionSource, TeamMission.class, Comparator.comparing(TeamMission::getTeamID).thenComparing(TeamMission::getMissionName));
        this.teamMissionDataTableManager = new TableManager<>(connectionSource, TeamMissionData.class, Comparator.comparing(TeamMissionData::getMissionID).thenComparing(TeamMissionData::getMissionIndex));
        this.teamRewardsTableManager = new ForeignIslandTableManager<>(connectionSource, TeamReward.class, Comparator.comparing(TeamReward::getTeamID));
        this.teamSettingsTableManager = new ForeignIslandTableManager<>(connectionSource, TeamSetting.class, Comparator.comparing(TeamSetting::getTeamID).thenComparing(TeamSetting::getSetting));
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
            islandTableManager.addEntry(island);
            // Saving the object will also assign the Faction's ID
            islandTableManager.save();
            // Since the FactionID was null before we need to resort
            islandTableManager.sort();
        });
    }

}
