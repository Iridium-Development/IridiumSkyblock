package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.SQL;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {
    private final SQL SQL_CONFIG = IridiumSkyblock.getInstance().getSql();
    private final ConnectionSource connectionSource;

    private final Dao<User, UUID> users;
    private final Dao<Island, Integer> islands;

    public DatabaseManager() throws SQLException {
        String databaseURL = getDatabaseURL();

        connectionSource = new JdbcConnectionSource(
                databaseURL,
                SQL_CONFIG.username,
                SQL_CONFIG.password,
                DatabaseTypeUtils.createDatabaseType(databaseURL)
        );

        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, Island.class);

        this.users = DaoManager.createDao(connectionSource, User.class);
        this.islands = DaoManager.createDao(connectionSource, Island.class);

        users.setAutoCommit(getDatabaseConnection(), false);
        islands.setAutoCommit(getDatabaseConnection(), false);
    }

    private @NotNull String getDatabaseURL() {
        switch (SQL_CONFIG.driver) {
            case MYSQL:
            case MARIADB:
            case POSTGRESQL:
                return "jdbc:" + SQL_CONFIG.driver + "://" + SQL_CONFIG.host + ":" + SQL_CONFIG.port + "/" + SQL_CONFIG.database;
            case SQLSERVER:
                return "jdbc:sqlserver://" + SQL_CONFIG.host + ":" + SQL_CONFIG.port + ";databaseName=" + SQL_CONFIG.database;
            case H2:
                return "jdbc:h2:file:" + SQL_CONFIG.database;
            case SQLITE:
                return "jdbc:sqlite:" + new File(IridiumSkyblock.getInstance().getDataFolder(), SQL_CONFIG.database + ".db");
        }
        throw new RuntimeException("How did we get here?");
    }

    private DatabaseConnection getDatabaseConnection() throws SQLException {
        return connectionSource.getReadWriteConnection(null);
    }

    public CompletableFuture<@Nullable User> getUserByUUID(@NotNull UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return users.queryBuilder().where().eq("uuid", uuid).queryForFirst();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }

            return null;
        });
    }

    public CompletableFuture<@Nullable Island> getIslandById(int id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return islands.queryBuilder().where().eq("id", id).queryForFirst();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }

            return null;
        });
    }

    public void saveUsers(@NotNull User... userList) {
        try {
            for (User user : userList) {
                users.createOrUpdate(user);
            }
            users.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void saveIslands(@NotNull Island... islandList) {
        try {
            for (Island island : islandList) {
                islands.createOrUpdate(island);
            }
            islands.commit(getDatabaseConnection());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
