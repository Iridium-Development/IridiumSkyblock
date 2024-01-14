package com.iridium.iridiumskyblock.databaseadapter;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DatabaseAdapter<Value, ID>{
    private final Dao<Value, ID> dao;

    private final ConnectionSource connectionSource;
    
    public DatabaseAdapter(ConnectionSource connectionSource, Class<Value> clazz) throws SQLException{
        if(IridiumSkyblock.getInstance().isTesting()){
            this.connectionSource = null;
            this.dao = null;
            return;
        }
        this.connectionSource = connectionSource;
        this.dao = DaoManager.createDao(connectionSource, clazz);

        this.dao.setAutoCommit(getDatabaseConnection(), false);
        TableUtils.createTableIfNotExists(connectionSource, clazz);
    }

    public void save(Value value) {
        if(IridiumSkyblock.getInstance().isTesting())return;
        try {
            dao.createOrUpdate(value);
            dao.commit(getDatabaseConnection());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void save(Collection<Value> values) {
        if(IridiumSkyblock.getInstance().isTesting())return;
        try {
            List<Value> entryList = new ArrayList<>(values);
            for (Value t : entryList) {
                dao.createOrUpdate(t);
            }
            dao.commit(getDatabaseConnection());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public CompletableFuture<Void> delete(Value value) {
        if(IridiumSkyblock.getInstance().isTesting()) return GetCompletedFuture();

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
        if(IridiumSkyblock.getInstance().isTesting()) return GetCompletedFuture();

        return CompletableFuture.runAsync(() -> {
            try {
                List<Value> entryList = new ArrayList<>(values);
                for (Value t : entryList) {
                    dao.delete(t);
                }
                dao.commit(getDatabaseConnection());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public List<Value> queryAll() throws SQLException {
        if(IridiumSkyblock.getInstance().isTesting()) return Collections.emptyList();
        return dao.queryForAll();
    }

    private DatabaseConnection getDatabaseConnection() throws SQLException {
        return connectionSource.getReadWriteConnection(null);
    }

    private CompletableFuture<Void> GetCompletedFuture(){
        return CompletableFuture.runAsync(() -> {});
    }
}
