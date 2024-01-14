package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.databaseadapter.DatabaseAdapter;
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
    private final DatabaseKey<Key, Value> databaseKey;
    private final DatabaseAdapter<Value, ID> databaseAdapter;

    public TableManager(DatabaseKey<Key, Value> databaseKey, DatabaseAdapter<Value, ID> databaseAdapter) throws SQLException {
        this.databaseKey = databaseKey;
        this.databaseAdapter = databaseAdapter;

        databaseAdapter.queryAll().forEach(this::addEntry);
        getEntries().forEach(value -> value.setChanged(false));
    }

    public void save() {
        List<Value> values = entries.values().stream().filter(DatabaseObject::isChanged).collect(Collectors.toList());
        values.forEach(value -> value.setChanged(false));
        databaseAdapter.save(values);
    }

    public void save(Value value) {
        if (!value.isChanged()) return;
        databaseAdapter.save(value);
        value.setChanged(false);
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
        return databaseAdapter.delete(value);
    }

    public CompletableFuture<Void> delete(Collection<Value> values) {
        values.forEach(value -> entries.remove(databaseKey.getKey(value)));
        return databaseAdapter.delete(values);
    }
}
