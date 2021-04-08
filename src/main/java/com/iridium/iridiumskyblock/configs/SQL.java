package com.iridium.iridiumskyblock.configs;

import com.iridium.iridiumskyblock.managers.DatabaseManager;

/**
 * The SQL database connection file used by IridiumSkyblock (sql.yml).
 * Is deserialized automatically on plugin startup and reload.
 */
public class SQL {

    public Driver driver = Driver.SQLITE;
    public String host = "localhost";
    public String database = "IridiumSkyblock";
    public String username = "";
    public String password = "";
    public int port = 3306;

    /**
     * Represents a Driver of a database.
     * Used in the {@link DatabaseManager}.
     */
    public enum Driver {
        MYSQL,
        MARIADB,
        SQLSERVER,
        POSTGRESQL,
        H2,
        SQLITE
    }

}
