package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.SQL;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.SchematicData;
import com.iridium.iridiumskyblock.database.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.SQLException;
import java.util.*;

public class DatabaseManager {

    private final SQL sqlConfig;
    private final IridiumSkyblock iridiumSkyblock;
    private final ConnectionSource connectionSource;

    private final Dao<User, UUID> userDao;
    private final Dao<Island, Integer> islandDao;
    private final Dao<SchematicData, String> schematicDao;

    @Getter
    private final List<User> userList;
    @Getter
    private final List<Island> islandList;

    /**
     * The default constructor.
     *
     * @param iridiumSkyblock The instance of IridiumSkyblock used by the plugin
     * @throws SQLException If the connection or any operations failed
     */
    public DatabaseManager(IridiumSkyblock iridiumSkyblock) throws SQLException {
        this.sqlConfig = iridiumSkyblock.getSql();
        this.iridiumSkyblock = iridiumSkyblock;
        String databaseURL = getDatabaseURL();

        connectionSource = new JdbcConnectionSource(
                databaseURL,
                sqlConfig.username,
                sqlConfig.password,
                DatabaseTypeUtils.createDatabaseType(databaseURL)
        );

        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, Island.class);
        TableUtils.createTableIfNotExists(connectionSource, SchematicData.class);

        this.userDao = DaoManager.createDao(connectionSource, User.class);
        this.islandDao = DaoManager.createDao(connectionSource, Island.class);
        this.schematicDao = DaoManager.createDao(connectionSource, SchematicData.class);

        userDao.setAutoCommit(getDatabaseConnection(), false);
        islandDao.setAutoCommit(getDatabaseConnection(), false);

        this.userList = getUsers();
        this.islandList = getIslands();
    }

    /**
     * Database connection String used for establishing a connection.
     *
     * @return The database URL String
     */
    private @NotNull String getDatabaseURL() {
        switch (sqlConfig.driver) {
            case MYSQL:
            case MARIADB:
            case POSTGRESQL:
                return "jdbc:" + sqlConfig.driver + "://" + sqlConfig.host + ":" + sqlConfig.port + "/" + sqlConfig.database;
            case SQLSERVER:
                return "jdbc:sqlserver://" + sqlConfig.host + ":" + sqlConfig.port + ";databaseName=" + sqlConfig.database;
            case H2:
                return "jdbc:h2:file:" + sqlConfig.database;
            case SQLITE:
                return "jdbc:sqlite:" + new File(iridiumSkyblock.getDataFolder(), sqlConfig.database + ".db");
        }
        throw new RuntimeException("How did we get here?");
    }

    /**
     * Returns a connection from the connection source.
     *
     * @return The connection to the database for operations
     * @throws SQLException If there is an error with the exception
     */
    private DatabaseConnection getDatabaseConnection() throws SQLException {
        return connectionSource.getReadWriteConnection(null);
    }

    /**
     * Returns a list of all users in the database.
     * Might be empty if an error occurs.
     *
     * @return a List of all users
     */
    private @NotNull List<User> getUsers() {
        try {
            return userDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Returns a list of all islands in the database.
     * Might be empty if an error occurs.
     *
     * @return a List of all islands
     */
    private @NotNull List<Island> getIslands() {
        try {
            return islandDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Returns a list of all schematics in the database.
     * Might be empty if an error occurs.
     *
     * @return a list of all schematics in the database
     */
    public @NotNull List<SchematicData> getSchematics() {
        try {
            return schematicDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Finds an User by his {@link UUID}.
     *
     * @param uuid The uuid of the player
     * @return the User class of the player
     */
    public Optional<User> getUserByUUID(@NotNull UUID uuid) {
        return userList.stream().filter(user -> user.getUuid().equals(uuid)).findFirst();
    }

    /**
     * Finds an Island by its id.
     *
     * @param id The id of the island
     * @return An Optional with the Island, empty if there is none
     */
    public Optional<Island> getIslandById(int id) {
        return islandList.stream().filter(island -> island.getId() == id).findFirst();
    }

    /**
     * Finds an Island by its name.
     *
     * @param name The name of the island
     * @return An Optional with the Island, empty if there is none
     */
    public Optional<Island> getIslandByName(String name) {
        return islandList.stream().filter(island -> island.getName().equalsIgnoreCase(name)).findFirst();
    }

    /**
     * Saves an island to the database and initializes variables like ID
     *
     * @param island The island we are saving
     * @return The island with variables like id added
     */
    public Island saveIsland(Island island) {
        try {
            islandDao.createOrUpdate(island);
            islandDao.commit(getDatabaseConnection());
            return islandDao.queryBuilder().where().eq("name", island.getName()).queryForFirst();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return island;
    }

    /**
     * Saves all users to the database.
     * Creates them if they don't exist
     */
    public void saveUsers() {
        try {
            for (User user : userList) {
                userDao.createOrUpdate(user);
            }
            userDao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Saves all islands to the database.
     * Creates them if they don't exist.
     */
    public void saveIslands() {
        try {
            for (Island island : islandList) {
                islandDao.createOrUpdate(island);
            }
            islandDao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Saves all schematics to the database.
     * Creates them if they don't exist.
     */

    public void saveSchematics(@NotNull Collection<SchematicData> schematics) {
        try {
            for (SchematicData schematic : schematics) {
                schematicDao.createOrUpdate(schematic);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
