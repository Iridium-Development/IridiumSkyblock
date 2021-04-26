package com.iridium.iridiumskyblock.managers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * @param <T> The Table Class
 * @param <S> The Table Primary Id Class
 */
public class TableManager<T, S> {
    private final List<T> list;
    private final Dao<T, S> dao;

    private final boolean autoCommit;
    private final ConnectionSource connectionSource;

    public TableManager(ConnectionSource connectionSource, Class<T> clazz, boolean autoCommit) throws SQLException {
        this.connectionSource = connectionSource;
        this.autoCommit = autoCommit;
        TableUtils.createTableIfNotExists(connectionSource, clazz);
        this.dao = DaoManager.createDao(connectionSource, clazz);
        this.dao.setAutoCommit(getDatabaseConnection(), autoCommit);
        this.list = dao.queryForAll();
    }

    /**
     * Saves everything to the Database
     */
    public void save() {
        try {
            for (T t : list) {
                dao.createOrUpdate(t);
            }
            if (!autoCommit) {
                dao.commit(getDatabaseConnection());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Saves a single variable to the db and gets the Value back
     * with filled in Id's and other params
     *
     * @param t The variable we are saving
     * @return The value saves the the db with filled ID's
     */
    public T save(T t) {
        return null;
        //TODO
    }

    /**
     * Gets all T's from cache
     *
     * @return The list of all T's
     */
    public List<T> getList() {
        return list;
    }

    /**
     * Delete T from the database
     *
     * @param t the variable we are deleting
     */
    public void delete(T t) {
        try {
            dao.delete(t);
            list.remove(t);
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
     * @param t The collection of variables we are deleting
     */
    public void delete(Collection<T> t) {
        try {
            dao.delete(t);
            list.removeAll(t);
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
}
