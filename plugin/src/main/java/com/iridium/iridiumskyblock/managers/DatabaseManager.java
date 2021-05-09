package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.SQL;
import com.iridium.iridiumskyblock.database.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.support.ConnectionSource;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Class which handles the database connection and acts as a DAO.
 */
@Getter
public class DatabaseManager {

    private final TableManager<User, UUID> userTableManager;
    private final TableManager<Island, Integer> islandTableManager;
    private final TableManager<SchematicData, String> schematicTableManager;
    private final IslandTableManager<IslandInvite, Integer> islandInviteTableManager;
    private final IslandTableManager<IslandPermission, Integer> islandPermissionTableManager;
    private final IslandTableManager<IslandBlocks, Integer> islandBlocksTableManager;
    private final IslandTableManager<IslandBank, Integer> islandBankTableManager;
    private final IslandTableManager<IslandMission, Integer> islandMissionTableManager;
    private final IslandTableManager<IslandReward, Integer> islandRewardTableManager;
    private final IslandTableManager<IslandUpgrade, Integer> islandUpgradeTableManager;
    private final IslandTableManager<IslandTrusted, Integer> islandTrustedTableManager;

    @Getter(AccessLevel.NONE)
    private final ConnectionSource connectionSource;

    /**
     * The default constructor.
     *
     * @throws SQLException If the connection or any operations failed
     */
    public DatabaseManager() throws SQLException {
        SQL sqlConfig = IridiumSkyblock.getInstance().getSql();
        String databaseURL = getDatabaseURL(sqlConfig);

        this.connectionSource = new JdbcConnectionSource(
                databaseURL,
                sqlConfig.username,
                sqlConfig.password,
                DatabaseTypeUtils.createDatabaseType(databaseURL)
        );

        this.userTableManager = new TableManager<>(connectionSource, User.class, false);
        this.islandTableManager = new TableManager<>(connectionSource, Island.class, false);
        this.schematicTableManager = new TableManager<>(connectionSource, SchematicData.class, false);
        this.islandInviteTableManager = new IslandTableManager<>(connectionSource, IslandInvite.class, false);
        this.islandPermissionTableManager = new IslandTableManager<>(connectionSource, IslandPermission.class, false);
        this.islandBlocksTableManager = new IslandTableManager<>(connectionSource, IslandBlocks.class, false);
        this.islandBankTableManager = new IslandTableManager<>(connectionSource, IslandBank.class, false);
        this.islandMissionTableManager = new IslandTableManager<>(connectionSource, IslandMission.class, false);
        this.islandRewardTableManager = new IslandTableManager<>(connectionSource, IslandReward.class, false);
        this.islandUpgradeTableManager = new IslandTableManager<>(connectionSource, IslandUpgrade.class, false);
        this.islandTrustedTableManager = new IslandTableManager<>(connectionSource, IslandTrusted.class, false);
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
            islandTableManager.getDao().commit(connectionSource.getReadOnlyConnection(null));
            Island is = islandTableManager.getDao().queryBuilder().where().eq("name", island.getName()).queryForFirst();
            islandTableManager.addEntry(is);
            return is;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return island;
    }

}
