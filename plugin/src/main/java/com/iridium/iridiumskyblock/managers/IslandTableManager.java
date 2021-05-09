package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.database.Island;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Used for handling Crud operations on a table + handling cache
 */
public class IslandTableManager {
    private final List<Island> entries;
    private final Dao<Island, Integer> dao;

    private final boolean autoCommit;
    private final ConnectionSource connectionSource;

    public IslandTableManager(ConnectionSource connectionSource, boolean autoCommit) throws SQLException {
        this.connectionSource = connectionSource;
        this.autoCommit = autoCommit;
        TableUtils.createTableIfNotExists(connectionSource, Island.class);
        this.dao = DaoManager.createDao(connectionSource, Island.class);
        this.dao.setAutoCommit(getDatabaseConnection(), autoCommit);
        this.entries = dao.queryForAll();
        sort();
    }

    /**
     * Saves everything to the Database
     */
    public void save() {
        try {
            for (Island island : entries) {
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
    private void sort() {
        entries.sort(Comparator.comparing(Island::getId));
    }

    /**
     * Add an item to the list whilst maintaining sorted list
     *
     * @param island The item we are adding
     */
    public void addEntry(Island island) {
        entries.add(island);
        sort();
    }

    /**
     * Gets all T's from cache
     *
     * @return The list of all T's
     */
    public List<Island> getEntries() {
        return entries;
    }

    public Optional<Island> getIsland(int id) {
        int first = 0;
        int last = entries.size() - 1;
        int mid = last / 2;
        while (first <= last) {
            int islandId = entries.get(mid).getId();
            if (islandId < id) {
                first = mid + 1;
            } else if (islandId == id) {
                return Optional.of(entries.get(mid));
            } else {
                last = mid - 1;
            }
            mid = (first + last) / 2;
        }
        return Optional.empty();
    }

    /**
     * Delete T from the database
     *
     * @param island the variable we are deleting
     */
    public void delete(Island island) {
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
    public void delete(Collection<Island> islands) {
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
    public Dao<Island, Integer> getDao() {
        return dao;
    }
}
