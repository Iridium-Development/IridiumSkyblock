package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.SQL;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandInvite;
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
import java.util.stream.Collectors;

/**
 * Class which handles the database connection and acts as a DAO.
 * TODO: Split up this class
 */
public class DatabaseManager {

    private final SQL sqlConfig;
    private final ConnectionSource connectionSource;

    private final Dao<User, UUID> userDao;
    private final Dao<Island, Integer> islandDao;
    private final Dao<SchematicData, String> schematicDao;
    private final Dao<IslandInvite, Integer> islandInviteDao;

    @Getter
    private final List<User> userList;
    @Getter
    private final List<Island> islandList;
    @Getter
    private final List<IslandInvite> islandInviteList;

    /**
     * The default constructor.
     *
     * @throws SQLException If the connection or any operations failed
     */
    public DatabaseManager() throws SQLException {
        this.sqlConfig = IridiumSkyblock.getInstance().getSql();
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
        TableUtils.createTableIfNotExists(connectionSource, IslandInvite.class);

        this.userDao = DaoManager.createDao(connectionSource, User.class);
        this.islandDao = DaoManager.createDao(connectionSource, Island.class);
        this.schematicDao = DaoManager.createDao(connectionSource, SchematicData.class);
        this.islandInviteDao = DaoManager.createDao(connectionSource, IslandInvite.class);

        userDao.setAutoCommit(getDatabaseConnection(), false);
        islandDao.setAutoCommit(getDatabaseConnection(), false);
        islandInviteDao.setAutoCommit(getDatabaseConnection(), false);

        this.userList = getUsers();
        this.islandList = getIslands();
        this.islandInviteList = getIslandInvites();
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
                return "jdbc:sqlite:" + new File(IridiumSkyblock.getInstance().getDataFolder(), sqlConfig.database + ".db");
            default:
                throw new UnsupportedOperationException("Unsupported driver (how did we get here?): " + sqlConfig.driver.name());
        }
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
     * Returns a list of all islandInvites in the database.
     * Might be empty if an error occurs.
     *
     * @return a List of all islandInvites
     */
    private @NotNull List<IslandInvite> getIslandInvites() {
        try {
            return islandInviteDao.queryForAll();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }

    public @NotNull List<User> getIslandMembers(@NotNull Island island) {
        return userList.stream().filter(user -> island.equals(user.getIsland().orElse(null))).collect(Collectors.toList());
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
     * Gets all IslandInvites for an Island
     *
     * @param island The island who's invites we are retrieving
     * @return A list of Invites
     */
    public List<IslandInvite> getInvitesByIsland(@NotNull Island island) {
        return islandInviteList.stream().filter(islandInvite -> island.equals(islandInvite.getIsland().orElse(null))).collect(Collectors.toList());
    }

    /**
     * Gets all IslandInvites for a User
     *
     * @param user The user who's invites we are retrieving
     * @return A list of Invites
     */
    public List<IslandInvite> getInvitesByUser(@NotNull User user) {
        return islandInviteList.stream().filter(islandInvite -> user.equals(islandInvite.getUser())).collect(Collectors.toList());
    }

    /**
     * Finds an User by his {@link UUID}.
     *
     * @param uuid The uuid of the onlyForPlayers
     * @return the User class of the onlyForPlayers
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
    public Island registerIsland(Island island) {
        try {
            islandDao.createOrUpdate(island);
            islandDao.commit(getDatabaseConnection());
            Island is = islandDao.queryBuilder().where().eq("name", island.getName()).queryForFirst();
            islandList.add(is);
            return is;
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
     * Saves all islandInvites to the database.
     * Creates them if they don't exist.
     */
    public void saveIslandInvites() {
        try {
            for (IslandInvite islandInvite : islandInviteList) {
                islandInviteDao.createOrUpdate(islandInvite);
            }
            islandInviteDao.commit(getDatabaseConnection());
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

    /**
     * Removes an island from the database and removes all references to the island
     *
     * @param island The island being deleted.
     */
    public void deleteIsland(@NotNull Island island) {
        try {
            island.getMembers().forEach(user -> user.setIsland(null));
            islandDao.delete(island);
            islandList.remove(island);
            islandDao.commit(getDatabaseConnection());
            saveIslands();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
