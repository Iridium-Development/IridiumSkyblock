package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.database.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.*;

/**
 * Used for handling Crud operations on a table + handling cache
 */
public class UserTableManager {
    private final List<User> entries;
    private final Dao<User, Integer> dao;

    private final boolean autoCommit;
    private final ConnectionSource connectionSource;

    public UserTableManager(ConnectionSource connectionSource, boolean autoCommit) throws SQLException {
        this.connectionSource = connectionSource;
        this.autoCommit = autoCommit;
        TableUtils.createTableIfNotExists(connectionSource, User.class);
        this.dao = DaoManager.createDao(connectionSource, User.class);
        this.dao.setAutoCommit(getDatabaseConnection(), autoCommit);
        this.entries = dao.queryForAll();
        sort();
    }

    /**
     * Saves everything to the Database
     */
    public void save() {
        try {
            for (User island : entries) {
                dao.createOrUpdate(island);
            }
            if (!autoCommit) {
                dao.commit(getDatabaseConnection());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Sort the list of entries by island id
     */
    public void sort() {
        entries.sort(Comparator.comparing(User::getUuid));
    }

    /**
     * Add an item to the list whilst maintaining sorted list
     *
     * @param island The item we are adding
     */
    public void addEntry(User island) {
        entries.add(island);
        sort();
    }

    /**
     * Gets all T's from cache
     *
     * @return The list of all T's
     */
    public List<User> getEntries() {
        return entries;
    }

    public Optional<User> getUser(UUID uuid) {
        int index = Collections.binarySearch(entries, new User(uuid, ""), Comparator.comparing(User::getUuid));
        if (index < 0) return Optional.empty();
        return Optional.of(entries.get(index));
    }

    /**
     * Delete T from the database
     *
     * @param island the variable we are deleting
     */
    public void delete(User island) {
        try {
            dao.delete(island);
            entries.remove(island);
            if (!autoCommit) {
                dao.commit(getDatabaseConnection());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Delete all t's in the database
     *
     * @param islands The collection of islands we are deleting
     */
    public void delete(Collection<User> islands) {
        try {
            dao.delete(islands);
            entries.removeAll(islands);
            if (!autoCommit) {
                dao.commit(getDatabaseConnection());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
     * Returns the Dao for this class
     *
     * @return The dao
     */
    public Dao<User, Integer> getDao() {
        return dao;
    }
}
