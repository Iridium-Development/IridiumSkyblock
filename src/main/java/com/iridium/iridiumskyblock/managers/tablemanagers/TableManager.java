package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Used for handling Crud operations on a table + handling cache
 *
 * @param <T> The Table Class
 * @param <S> The Table Primary Id Class
 */
public class TableManager<T, S> {
    private final List<T> entries;
    private final Dao<T, S> dao;
    private final Class<T> clazz;

    private final boolean autoCommit;
    private final ConnectionSource connectionSource;

    public TableManager(ConnectionSource connectionSource, Class<T> clazz, boolean autoCommit) throws SQLException {
        this.connectionSource = connectionSource;
        this.autoCommit = autoCommit;
        TableUtils.createTableIfNotExists(connectionSource, clazz);
        this.dao = DaoManager.createDao(connectionSource, clazz);
        this.dao.setAutoCommit(getDatabaseConnection(), autoCommit);
        this.entries = dao.queryForAll();
        this.clazz = clazz;
    }

    /**
     * Saves everything to the Database
     */
    public void save() {
        try {
            List<T> entryList = new ArrayList<>(entries);
            for (T t : entryList) {
                dao.createOrUpdate(t);
            }
            if (!autoCommit) {
                dao.commit(getDatabaseConnection());
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Adds an entry to list
     *
     * @param t the item we are adding
     */
    public void addEntry(T t) {
        entries.add(t);
    }

    /**
     * Gets all T's from cache
     *
     * @return The list of all T's
     */
    public List<T> getEntries() {
        return entries;
    }

    /**
     * Delete T from the database
     *
     * @param t the variable we are deleting
     */
    public void delete(T t) {
        try {
            dao.delete(t);
            entries.remove(t);
            if (!autoCommit) {
                dao.commit(getDatabaseConnection());
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Delete all t's in the database
     *
     * @param t The collection of variables we are deleting
     */
    public void delete(Collection<T> t) {
        try {
            dao.delete(t);
            entries.removeAll(t);
            if (!autoCommit) {
                dao.commit(getDatabaseConnection());
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
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
     * Clear all entries in the database & cache
     */
    public void clear() {
        try {
            TableUtils.clearTable(connectionSource, clazz);
            if (!autoCommit) {
                dao.commit(getDatabaseConnection());
            }
            entries.clear();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Returns the Dao for this class
     *
     * @return The dao
     */
    public Dao<T, S> getDao() {
        return dao;
    }
}
