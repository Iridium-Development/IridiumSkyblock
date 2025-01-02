package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.managers.DatabaseKey;
import com.iridium.iridiumteams.database.DatabaseObject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TableManager<Key, Value extends DatabaseObject, ID> {
    private final ConcurrentHashMap<Key, Value> entries = new ConcurrentHashMap<>();
    private final Dao<Value, ID> dao;
    private final DatabaseKey<Key, Value> databaseKey;
    private final static Lock lock = new ReentrantLock();

    private final ConnectionSource connectionSource;

    public TableManager(DatabaseKey<Key, Value> databaseKey, ConnectionSource connectionSource, Class<Value> clazz) throws SQLException {
        this.connectionSource = connectionSource;
        this.databaseKey = databaseKey;
        this.dao = DaoManager.createDao(connectionSource, clazz);
        this.dao.setAutoCommit(getDatabaseConnection(), false);

        TableUtils.createTableIfNotExists(connectionSource, clazz);
        dao.queryForAll().forEach(this::addEntry);
        getEntries().forEach(value -> value.setChanged(false));
    }

    public void save() {
        try {
            if (!lock.tryLock(5, TimeUnit.SECONDS)) {
                IridiumSkyblock.getInstance().getLogger().warning("Warning: Lock acquisition took more than 5 second in save() method.");
            }
            try {
                List<Value> entryList = new ArrayList<>(entries.values());
                boolean modified = false;
                for (Value t : entryList) {
                    if (!t.isChanged()) continue;
                    modified = true;
                    dao.createOrUpdate(t);
                    t.setChanged(false);
                }
                if (modified) dao.commit(getDatabaseConnection());
            } finally {
                lock.unlock();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(Value value) {
        try {
            if (!lock.tryLock(5, TimeUnit.SECONDS)) {
                IridiumSkyblock.getInstance().getLogger().warning("Warning: Lock acquisition took more than 5 second in save(value) method.");
            }
            try {
                if (!value.isChanged()) return;
                dao.createOrUpdate(value);
                dao.commit(getDatabaseConnection());
                value.setChanged(false);
            } finally {
                lock.unlock();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<Void> delete(Value value) {
        entries.remove(databaseKey.getKey(value));
        return CompletableFuture.runAsync(() -> {
            try {
                if (!lock.tryLock(5, TimeUnit.SECONDS)) {
                    IridiumSkyblock.getInstance().getLogger().warning("Warning: Lock acquisition took more than 5 second in delete(value) method.");
                }
                try {
                    dao.delete(value);
                    dao.commit(getDatabaseConnection());
                } finally {
                    lock.unlock();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<Void> delete(Collection<Value> values) {
        values.forEach(value -> entries.remove(databaseKey.getKey(value)));
        return CompletableFuture.runAsync(() -> {
            try {
                if (!lock.tryLock(5, TimeUnit.SECONDS)) {
                    IridiumSkyblock.getInstance().getLogger().warning("Warning: Lock acquisition took more than 5 second in delete(Collection<value>) method.");
                }
                try {
                    dao.delete(values);
                    dao.commit(getDatabaseConnection());
                } finally {
                    lock.unlock();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void addEntry(Value value) {
        try {
            entries.put(databaseKey.getKey(value), value);
        }catch (Exception e) {
            IridiumSkyblock.getInstance().getLogger().warning("Warning: Deleting "+value.getClass().getName()+" record because "+e.getMessage());
            CompletableFuture.runAsync(() -> {
                try {
                    if (!lock.tryLock(5, TimeUnit.SECONDS)) {
                        IridiumSkyblock.getInstance().getLogger().warning("Warning: Lock acquisition took more than 5 second in delete(value) method.");
                    }
                    try {
                        dao.delete(value);
                        dao.commit(getDatabaseConnection());
                    } finally {
                        lock.unlock();
                    }
                } catch (Exception ex1) {
                    ex1.printStackTrace();
                }
            });
        }
    }

    public List<Value> getEntries(Function<? super Value, Boolean> searchFunction) {
        return entries.values().stream().filter(searchFunction::apply).collect(Collectors.toList());
    }

    public List<Value> getEntries() {
        return new ArrayList<>(entries.values());
    }

    public Optional<Value> getEntry(Key key) {
        return Optional.ofNullable(entries.get(key));
    }

    public Optional<Value> getEntry(Value value) {
        return getEntry(databaseKey.getKey(value));
    }

    public Optional<Value> getEntry(Function<? super Value, Boolean> searchFunction) {
        return entries.values().stream().filter(searchFunction::apply).findFirst();
    }

    private DatabaseConnection getDatabaseConnection() throws SQLException {
        return connectionSource.getReadWriteConnection(null);
    }
}
