package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumcore.utils.SortedList;
import com.iridium.iridiumskyblock.DatabaseObject;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Used for handling Crud operations on a table + handling cache
 *
 * @param <T> The Table Class
 * @param <S> The Table Primary Id Class
 */
public class TableManager<T extends DatabaseObject, S> {
    private final SortedList<T> entries;
    private final Dao<T, S> dao;
    private final Class<T> clazz;

    private final ConnectionSource connectionSource;

    public TableManager(ConnectionSource connectionSource, Class<T> clazz, Comparator<T> comparator) throws SQLException {
        this.connectionSource = connectionSource;
        TableUtils.createTableIfNotExists(connectionSource, clazz);
        this.dao = DaoManager.createDao(connectionSource, clazz);
        this.dao.setAutoCommit(getDatabaseConnection(), false);
        this.entries = new SortedList<>(comparator);
        this.entries.addAll(dao.queryForAll());
        entries.forEach(t -> t.setChanged(false));
        this.entries.sort(comparator);
        this.clazz = clazz;
    }

    /**
     * Saves everything to the Database
     */
    public void save() {
        List<T> entryList = new LinkedList<>(entries);
        try {
            for (T t : entryList) {
                if (t.isChanged()) {
                    dao.createOrUpdate(t);
                    t.setChanged(false);
                }
            }
            dao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void save(T t) {
        try {
            if (t.isChanged()) {
                dao.createOrUpdate(t);
                dao.commit(getDatabaseConnection());
                t.setChanged(false);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void saveHashMapList(LinkedHashMap<?, List<T>> saveMultipleListInMap) {
        LinkedList<List<T>> listLinkedList = new LinkedList<>(saveMultipleListInMap.values());
        multipleSave(listLinkedList.stream().flatMap(Collection::stream).collect(Collectors.toCollection(LinkedList::new)));
    }

    public void saveHashMap(LinkedHashMap<?, T> uuidLinkedHashMap) {
        multipleSave(new LinkedList<>(uuidLinkedHashMap.values()));
    }

    private void multipleSave(List<T> tList) {
        int sizeList = tList.size();
        int savedata = 0;
        try {
            for (int i = 0; i < sizeList; i++) {
                T t = tList.get(i);
                if (t == null) continue;
                dao.createOrUpdate(t);
                savedata++;
            }
            dao.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        if (IridiumSkyblock.getInstance().getConfiguration().debug) {
            System.out.println("Sauvegarde faite : " + savedata + "/" + sizeList);
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
            dao.commit(getDatabaseConnection());
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
            dao.commit(getDatabaseConnection());
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
            dao.commit(getDatabaseConnection());
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
