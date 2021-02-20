package com.iridium.iridiumskyblock;

import com.heretere.hdl.dependency.maven.annotation.MavenDependency;
import com.heretere.hdl.relocation.annotation.Relocation;
import com.heretere.hdl.spigot.DependencyPlugin;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.commands.CommandManager;
import com.iridium.iridiumskyblock.configs.Configuration;
import com.iridium.iridiumskyblock.configs.Messages;
import com.iridium.iridiumskyblock.configs.SQL;
import com.iridium.iridiumskyblock.database.DatabaseManager;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.sql.SQLException;

/**
 * The main class of this plugin which handles initialization
 * and shutdown of the plugin.
 */
@MavenDependency("com|fasterxml|jackson|core:jackson-databind:2.12.1")
@MavenDependency("com|fasterxml|jackson|core:jackson-core:2.12.1")
@MavenDependency("com|fasterxml|jackson|core:jackson-annotations:2.12.1")
@MavenDependency("com|fasterxml|jackson|dataformat:jackson-dataformat-yaml:2.12.1")
@MavenDependency("org|yaml:snakeyaml:1.27")
@Relocation(from = "org|yaml", to = "com|iridium|iridiumskyblock")
@Getter
public class IridiumSkyblock extends DependencyPlugin {

    private Persist persist;

    private CommandManager commandManager;
    private DatabaseManager databaseManager;

    private Configuration configuration;
    private Messages messages;
    private SQL sql;

    @Override
    public void load() {
        // Empty because we don't need any logic
    }

    /**
     * Plugin startup logic.
     */
    @Override
    public void enable() {
        // Initialize the configs
        this.persist = new Persist(Persist.PersistType.YAML, this);
        loadConfigs();
        saveConfigs();

        // Initialize the commands
        this.commandManager = new CommandManager("iridiumskyblock", this);

        // Try to connect to the database
        try {
            this.databaseManager = new DatabaseManager(this);
        } catch (SQLException exception) {
            // We don't want the plugin to start if the connection fails
            exception.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }

        // Initialize the API
        IridiumSkyblockAPI.initializeInstance(this);

        // Save data regularly
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, this::saveData, 0, 20 * 60 * 5);
      
        getLogger().info("----------------------------------------");
        getLogger().info("");
        getLogger().info(getDescription().getName() + " Enabled!");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("");
        getLogger().info("----------------------------------------");
    }

    /**
     * Plugin shutdown logic.
     */
    @Override
    public void disable() {
        saveData();
        getLogger().info("-------------------------------");
        getLogger().info("");
        getLogger().info(getDescription().getName() + " Disabled!");
        getLogger().info("");
        getLogger().info("-------------------------------");
    }

    /**
     * Saves islands and users to the database.
     */
    public void saveData() {
        getDatabaseManager().saveIslands();
        getDatabaseManager().saveUsers();
    }
  
    /**
     * Loads the configuration required for this plugin.
     * @see Persist
     */
    private void loadConfigs() {
        this.configuration = persist.load(Configuration.class);
        this.messages = persist.load(Messages.class);
        this.sql = persist.load(SQL.class);
    }

    /**
     * Saves changes to the configuration files.
     * @see Persist
     */
    private void saveConfigs() {
        this.persist.save(configuration);
        this.persist.save(messages);
        this.persist.save(sql);
    }

}
