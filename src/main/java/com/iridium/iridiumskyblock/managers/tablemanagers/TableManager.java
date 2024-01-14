package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.managers.DatabaseKey;
import com.iridium.iridiumteams.database.DatabaseObject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TableManager<Key, Value extends DatabaseObject, ID> {
    private final ConcurrentHashMap<Key, Value> entries = new ConcurrentHashMap<>();
    private final Dao<Value, ID> dao;
    private final DatabaseKey<Key, Value> databaseKey;

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
            List<Value> entryList = new ArrayList<>(entries.values());
            for (Value t : entryList) {
                if (!t.isChanged()) continue;
                dao.createOrUpdate(t);
                t.setChanged(false);
            }
            dao.commit(getDatabaseConnection());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void save(Value value) {
        try {
            if (!value.isChanged()) return;
            dao.createOrUpdate(value);
            dao.commit(getDatabaseConnection());
            value.setChanged(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addEntry(Value value) {
        entries.put(databaseKey.getKey(value), value);
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

    public CompletableFuture<Void> delete(Value value) {
        entries.remove(databaseKey.getKey(value));
        return CompletableFuture.runAsync(() -> {
            try {
                dao.delete(value);
                dao.commit(getDatabaseConnection());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public CompletableFuture<Void> delete(Collection<Value> values) {
        values.forEach(value -> entries.remove(databaseKey.getKey(value)));
        return CompletableFuture.runAsync(() -> {
            try {
                dao.delete(values);
                dao.commit(getDatabaseConnection());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    private DatabaseConnection getDatabaseConnection() throws SQLException {
        return connectionSource.getReadWriteConnection(null);
    }
}
