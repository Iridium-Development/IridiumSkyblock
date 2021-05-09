package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandData;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

/**
 * Used for handling Crud operations on a table + handling cache
 *
 * @param <T> The Table Class
 * @param <S> The Table Primary Id Class
 */
public class IslandTableManager<T extends IslandData, S> {
    private final List<T> entries;
    private final Dao<T, S> dao;

    private final boolean autoCommit;
    private final ConnectionSource connectionSource;

    public IslandTableManager(ConnectionSource connectionSource, Class<T> clazz, boolean autoCommit) throws SQLException {
        this.connectionSource = connectionSource;
        this.autoCommit = autoCommit;
        TableUtils.createTableIfNotExists(connectionSource, clazz);
        this.dao = DaoManager.createDao(connectionSource, clazz);
        this.dao.setAutoCommit(getDatabaseConnection(), autoCommit);
        this.entries = dao.queryForAll();
        sort();
    }

    /**
     * Saves everything to the Database
     */
    public void save() {
        try {
            for (T t : entries) {
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
     * Sort the list of entries by island id
     */
    private void sort() {
        entries.sort(Comparator.comparing(t1 -> {
            if (t1.getIsland().isPresent()) {
                return t1.getIsland().get().getId();
            }
            return 0;
        }));
    }

    /**
     * Add an item to the list whilst maintaining sorted list
     *
     * @param t The item we are adding
     */
    public void addEntry(T t) {
        entries.add(t);
        sort();
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
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<T> getEntries(@NotNull Island island) {
        int index = getIndex(island);
        if (index == -1) return Collections.emptyList();
        int currentIndex = index;
        List<T> result = new ArrayList<>();
        result.add(entries.get(index));

        while (true) {
            if (currentIndex < 0) break;
            IslandData t = entries.get(currentIndex);
            if (island.equals(t.getIsland().orElse(null))) {
                result.add(entries.get(currentIndex));
                currentIndex--;
            } else {
                break;
            }
        }

        currentIndex = index;

        while (true) {
            if (currentIndex >= entries.size()) break;
            IslandData t = entries.get(currentIndex);
            if (island.equals(t.getIsland().orElse(null))) {
                result.add(entries.get(currentIndex));
                currentIndex++;
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * Gets the index of the island -1 if not found
     *
     * @param island The specified island
     * @return The index where its located
     */
    private int getIndex(@NotNull Island island) {
        int first = 0;
        int last = entries.size() - 1;
        int mid = last / 2;
        while (first <= last) {
            IslandData islandData = entries.get(mid);
            int islandId = islandData.getIsland().isPresent() ? islandData.getIsland().get().getId() : 0;
            if (islandId < island.getId()) {
                first = mid + 1;
            } else if (islandId == island.getId()) {
                return mid;
            } else {
                last = mid - 1;
            }
            mid = (first + last) / 2;
        }
        return -1;
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
            entries.removeAll(t);
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
    public Dao<T, S> getDao() {
        return dao;
    }
}
