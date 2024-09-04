package com.iridium.iridiumskyblock.configs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The SQL database connection file used by IridiumFactions (sql.yml).
 * Is deserialized automatically on plugin startup and reload.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SQL {

    public Driver driver = Driver.SQLITE;
    public String host = "localhost";
    public String database = "IridiumSkyblock";
    public String username = "";
    public String password = "";
    public int port = 3306;
    public boolean useSSL = false;

    /**
     * Represents a Driver of a database.
     */
    public enum Driver {
        MYSQL,
        SQLITE
    }

}
