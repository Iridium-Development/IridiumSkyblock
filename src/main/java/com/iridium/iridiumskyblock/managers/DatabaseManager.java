package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.DataConverter;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.SQL;
import com.iridium.iridiumskyblock.database.*;
import com.iridium.iridiumskyblock.database.types.XMaterialType;
import com.iridium.iridiumskyblock.managers.tablemanagers.ForeignIslandTableManager;
import com.iridium.iridiumskyblock.managers.tablemanagers.IslandTableManager;
import com.iridium.iridiumskyblock.managers.tablemanagers.UserTableManager;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.logger.NullLogBackend;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.SQLException;

/**
 * Class which handles the database connection and acts as a DAO.
 */
@Getter
public class DatabaseManager {

    private final int version = 1;
    private IslandTableManager islandTableManager;
    private UserTableManager userTableManager;
    private ForeignIslandTableManager<IslandBan, Integer> islandBanTableManager;
    private ForeignIslandTableManager<IslandBank, Integer> islandBankTableManager;
    private ForeignIslandTableManager<IslandBlocks, Integer> islandBlocksTableManager;
    private ForeignIslandTableManager<IslandBooster, Integer> islandBoosterTableManager;
    private ForeignIslandTableManager<IslandInvite, Integer> islandInviteTableManager;
    private ForeignIslandTableManager<IslandLog, Integer> islandLogTableManager;
    private ForeignIslandTableManager<IslandMission, Integer> islandMissionTableManager;
    private ForeignIslandTableManager<IslandPermission, Integer> islandPermissionTableManager;
    private ForeignIslandTableManager<IslandReward, Integer> islandRewardTableManager;
    private ForeignIslandTableManager<IslandSpawners, Integer> islandSpawnersTableManager;
    private ForeignIslandTableManager<IslandTrusted, Integer> islandTrustedTableManager;
    private ForeignIslandTableManager<IslandUpgrade, Integer> islandUpgradeTableManager;
    private ForeignIslandTableManager<IslandWarp, Integer> islandWarpTableManager;

    @Getter(AccessLevel.NONE)
    private ConnectionSource connectionSource;

    public void init() throws SQLException {
        LoggerFactory.setLogBackendFactory(new NullLogBackend.NullLogBackendFactory());

        SQL sqlConfig = IridiumSkyblock.getInstance().getSql();
        String databaseURL = getDatabaseURL(sqlConfig);

        DataPersisterManager.registerDataPersisters(XMaterialType.getSingleton());

        this.connectionSource = new JdbcConnectionSource(
                databaseURL,
                sqlConfig.username,
                sqlConfig.password,
                DatabaseTypeUtils.createDatabaseType(databaseURL)
        );

        this.islandTableManager = new IslandTableManager(connectionSource, false);
        this.userTableManager = new UserTableManager(connectionSource, false);
        this.islandInviteTableManager = new ForeignIslandTableManager<>(connectionSource, IslandInvite.class, false);
        this.islandPermissionTableManager = new ForeignIslandTableManager<>(connectionSource, IslandPermission.class, false);
        this.islandBlocksTableManager = new ForeignIslandTableManager<>(connectionSource, IslandBlocks.class, false);
        this.islandSpawnersTableManager = new ForeignIslandTableManager<>(connectionSource, IslandSpawners.class, false);
        this.islandBankTableManager = new ForeignIslandTableManager<>(connectionSource, IslandBank.class, false);
        this.islandMissionTableManager = new ForeignIslandTableManager<>(connectionSource, IslandMission.class, false);
        this.islandRewardTableManager = new ForeignIslandTableManager<>(connectionSource, IslandReward.class, false);
        this.islandUpgradeTableManager = new ForeignIslandTableManager<>(connectionSource, IslandUpgrade.class, false);
        this.islandTrustedTableManager = new ForeignIslandTableManager<>(connectionSource, IslandTrusted.class, false);
        this.islandBoosterTableManager = new ForeignIslandTableManager<>(connectionSource, IslandBooster.class, false);
        this.islandWarpTableManager = new ForeignIslandTableManager<>(connectionSource, IslandWarp.class, false);
        this.islandLogTableManager = new ForeignIslandTableManager<>(connectionSource, IslandLog.class, false);
        this.islandBanTableManager = new ForeignIslandTableManager<>(connectionSource, IslandBan.class, false);

        convertDatabaseData();
    }

    /**
     * Database connection String used for establishing a connection.
     *
     * @return The database URL String
     */
    private @NotNull String getDatabaseURL(SQL sqlConfig) {
        switch (sqlConfig.driver) {
            case MYSQL:
            case MARIADB:
            case POSTGRESQL:
                return "jdbc:" + sqlConfig.driver.name().toLowerCase() + "://" + sqlConfig.host + ":" + sqlConfig.port + "/" + sqlConfig.database + "?useSSL=" + sqlConfig.useSSL;
            case SQLSERVER:
                return "jdbc:sqlserver://" + sqlConfig.host + ":" + sqlConfig.port + ";databaseName=" + sqlConfig.database;
            case H2:
                return "jdbc:h2:file:" + sqlConfig.database;
            case SQLITE:
                return "jdbc:sqlite:" + new File(IridiumSkyblock.getInstance().getDataFolder(), sqlConfig.database + ".db");
            default:
                throw new UnsupportedOperationException("Unsupported driver (how did we get here?): " + sqlConfig.driver.name());
        }
    }

    private void convertDatabaseData() {
        Path versionFile = Paths.get("plugins", "IridiumSkyblock", "sql_version.txt");
        try {
            Files.write(versionFile, Collections.singleton(String.valueOf(version)), StandardOpenOption.CREATE_NEW);
            DataConverter.updateDatabaseData(1, 1, connectionSource);
        } catch (FileAlreadyExistsException exception) {
            try {
                int oldVersion = Integer.parseInt(Files.readAllLines(versionFile).get(0));
                if (oldVersion != version) {
                    DataConverter.updateDatabaseData(oldVersion, version, connectionSource);
                    Files.delete(versionFile);
                    Files.write(versionFile, Collections.singleton(String.valueOf(version)), StandardOpenOption.CREATE);
                }
            } catch (IOException | IndexOutOfBoundsException | NumberFormatException updateException) {
                updateException.printStackTrace();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Saves an island to the database and initializes variables like ID
     * ik this is really dumb but I cant think of a better way to do this
     *
     * @param island The island we are saving
     * @return The island with variables like id added
     */
    public Island registerIsland(Island island) {
        try {
            islandTableManager.getDao().createOrUpdate(island);
            DatabaseConnection connection = connectionSource.getReadWriteConnection(null);
            islandTableManager.getDao().commit(connection);
            Island is = islandTableManager.getDao().queryBuilder().where().eq("name", island.getName()).queryForFirst();
            islandTableManager.addEntry(is);
            connectionSource.releaseConnection(connection);
            return is;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return island;
    }

}
