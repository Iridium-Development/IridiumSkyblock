package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.SQL;
import com.iridium.iridiumskyblock.database.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.support.ConnectionSource;
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
    private final TableManager<IslandInvite, Integer> islandInviteTableManager;
    private final TableManager<IslandPermission, Integer> islandPermissionTableManager;
    private final TableManager<IslandBlocks, Integer> islandBlocksTableManager;
    private final TableManager<IslandBank, Integer> islandBankTableManager;
    private final TableManager<IslandMission, Integer> islandMissionTableManager;

    /**
     * The default constructor.
     *
     * @throws SQLException If the connection or any operations failed
     */
    public DatabaseManager() throws SQLException {
        SQL sqlConfig = IridiumSkyblock.getInstance().getSql();
        String databaseURL = getDatabaseURL(sqlConfig);

        ConnectionSource connectionSource = new JdbcConnectionSource(
                databaseURL,
                sqlConfig.username,
                sqlConfig.password,
                DatabaseTypeUtils.createDatabaseType(databaseURL)
        );

        this.userTableManager = new TableManager<>(connectionSource, User.class, false);
        this.islandTableManager = new TableManager<>(connectionSource, Island.class, false);
        this.schematicTableManager = new TableManager<>(connectionSource, SchematicData.class, false);
        this.islandInviteTableManager = new TableManager<>(connectionSource, IslandInvite.class, false);
        this.islandPermissionTableManager = new TableManager<>(connectionSource, IslandPermission.class, false);
        this.islandBlocksTableManager = new TableManager<>(connectionSource, IslandBlocks.class, false);
        this.islandBankTableManager = new TableManager<>(connectionSource, IslandBank.class, false);
        this.islandMissionTableManager = new TableManager<>(connectionSource, IslandMission.class, false);
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
                return "jdbc:" + sqlConfig.driver + "://" + sqlConfig.host + ":" + sqlConfig.port + "/" + sqlConfig.database + "?useSSL=" + sqlConfig.useSSL;
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

}
