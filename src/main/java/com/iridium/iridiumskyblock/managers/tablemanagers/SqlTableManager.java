package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SqlTableManager<Value, ID> {
    @Getter
    private final Dao<Value, ID> dao;
    private final Lock lock = new ReentrantLock();

    public SqlTableManager(ConnectionSource connectionSource, Class<Value> clazz) throws SQLException {
        this.dao = DaoManager.createDao(connectionSource, clazz);
        TableUtils.createTableIfNotExists(connectionSource, clazz);
    }

    public void save(Value value) {
        try {
            if (!lock.tryLock(5, TimeUnit.SECONDS)) {
                IridiumSkyblock.getInstance().getLogger()
                        .warning("Warning: Failed to acquire lock within 5 seconds in save(value)");
                return;
            }

            try {
                dao.createOrUpdate(value);

            } finally {
                lock.unlock();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Value> query(PreparedQuery<Value> preparedQuery) {
        try {
            return dao.query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
