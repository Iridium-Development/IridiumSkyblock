package com.iridium.iridiumskyblock.configs;

/**
 * The SQL database connection file used by IridiumSkyblock.
 * Is persisted automatically on plugin shutdown and reload.
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
     * Used in the {@link com.iridium.iridiumskyblock.database.DatabaseManager}.
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
