package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.DataConverter;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.SQL;
import com.iridium.iridiumskyblock.database.*;
import com.iridium.iridiumskyblock.database.types.XMaterialType;
import com.iridium.iridiumskyblock.managers.tablemanagers.ForeignIslandTableManager;
import com.iridium.iridiumskyblock.managers.tablemanagers.IslandTableManager;
import com.iridium.iridiumskyblock.managers.tablemanagers.UserTableManager;
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
import java.io.IOException;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

/**
 * Class which handles the database connection and acts as a DAO.
 */
@Getter
public class DatabaseManager {

    private final int version = 4;
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
    private ForeignIslandTableManager<IslandSetting, Integer> islandSettingTableManager;

    @Getter(AccessLevel.NONE)
    private ConnectionSource connectionSource;

    public void init() throws SQLException {
        LoggerFactory.setLogBackendFactory(new NullLogBackend.NullLogBackendFactory());

        SQL sqlConfig = IridiumSkyblock.getInstance().getSql();
        String databaseURL = getDatabaseURL(sqlConfig);

        DataPersisterManager.registerDataPersisters(XMaterialType.getSingleton());

        if (!IridiumSkyblock.getInstance().isTesting()) {
            this.connectionSource = new JdbcConnectionSource(
                    databaseURL,
                    sqlConfig.username,
                    sqlConfig.password,
                    DatabaseTypeUtils.createDatabaseType(databaseURL)
            );

            if (connectionSource.getReadWriteConnection(null).isTableExists("islands")) {
                convertDatabaseData(sqlConfig.driver);
            }
        }

        this.islandTableManager = new IslandTableManager(connectionSource);
        this.userTableManager = new UserTableManager(connectionSource);
        this.islandInviteTableManager = new ForeignIslandTableManager<>(connectionSource, IslandInvite.class, Comparator.comparing(IslandInvite::getIslandId).thenComparing(islandInvite -> islandInvite.getUser().getUuid()));
        this.islandPermissionTableManager = new ForeignIslandTableManager<>(connectionSource, IslandPermission.class, Comparator.comparing(IslandPermission::getIslandId).thenComparing(IslandPermission::getRank).thenComparing(IslandPermission::getPermission));
        this.islandBlocksTableManager = new ForeignIslandTableManager<>(connectionSource, IslandBlocks.class, Comparator.comparing(IslandBlocks::getIslandId).thenComparing(IslandBlocks::getMaterial));
        this.islandSpawnersTableManager = new ForeignIslandTableManager<>(connectionSource, IslandSpawners.class, Comparator.comparing(IslandSpawners::getIslandId).thenComparing(IslandSpawners::getSpawnerType));
        this.islandBankTableManager = new ForeignIslandTableManager<>(connectionSource, IslandBank.class, Comparator.comparing(IslandBank::getIslandId).thenComparing(IslandBank::getBankItem));
        this.islandMissionTableManager = new ForeignIslandTableManager<>(connectionSource, IslandMission.class, Comparator.comparing(IslandMission::getIslandId).thenComparing(IslandMission::getMissionName).thenComparing(IslandMission::getMissionIndex));
        this.islandRewardTableManager = new ForeignIslandTableManager<>(connectionSource, IslandReward.class, Comparator.comparing(IslandReward::getIslandId));
        this.islandUpgradeTableManager = new ForeignIslandTableManager<>(connectionSource, IslandUpgrade.class, Comparator.comparing(IslandUpgrade::getIslandId).thenComparing(IslandUpgrade::getUpgrade));
        this.islandTrustedTableManager = new ForeignIslandTableManager<>(connectionSource, IslandTrusted.class, Comparator.comparing(IslandTrusted::getIslandId).thenComparing(islandTrusted -> islandTrusted.getUser().getUuid()));
        this.islandBoosterTableManager = new ForeignIslandTableManager<>(connectionSource, IslandBooster.class, Comparator.comparing(IslandBooster::getIslandId).thenComparing(IslandBooster::getBooster));
        this.islandWarpTableManager = new ForeignIslandTableManager<>(connectionSource, IslandWarp.class, Comparator.comparing(IslandWarp::getIslandId));
        this.islandLogTableManager = new ForeignIslandTableManager<>(connectionSource, IslandLog.class, Comparator.comparing(IslandLog::getIslandId));
        this.islandBanTableManager = new ForeignIslandTableManager<>(connectionSource, IslandBan.class, Comparator.comparing(IslandBan::getIslandId).thenComparing(islandBan -> islandBan.getBannedUser().getUuid()));
        this.islandSettingTableManager = new ForeignIslandTableManager<>(connectionSource, IslandSetting.class, Comparator.comparing(IslandSetting::getIslandId).thenComparing(IslandSetting::getSetting));
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

    private void convertDatabaseData(SQL.Driver driver) {
        Path versionFile = Paths.get("plugins", "IridiumSkyblock", "sql_version.txt");
        try {
            Files.write(versionFile, Collections.singleton(String.valueOf(version)), StandardOpenOption.CREATE_NEW);
            DataConverter.updateDatabaseData(1, version, connectionSource, driver);
        } catch (FileAlreadyExistsException exception) {
            try {
                int oldVersion = Integer.parseInt(Files.readAllLines(versionFile).get(0));
                if (oldVersion != version) {
                    DataConverter.updateDatabaseData(oldVersion, version, connectionSource, driver);
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
     *
     * @param island The island we are saving
     * @return The island with variables like id added
     */
    public synchronized CompletableFuture<Void> registerIsland(Island island) {
        return CompletableFuture.runAsync(() -> {
            islandTableManager.save(island);
            islandTableManager.addEntry(island);
        });
    }

}
