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

import java.sql.SQLException;

@MavenDependency("com|fasterxml|jackson|core:jackson-databind:2.12.1")
@MavenDependency("com|fasterxml|jackson|core:jackson-core:2.12.1")
@MavenDependency("com|fasterxml|jackson|core:jackson-annotations:2.12.1")
@MavenDependency("com|fasterxml|jackson|dataformat:jackson-dataformat-yaml:2.12.1")
@MavenDependency("org|yaml:snakeyaml:1.27")
@Relocation(from = "org|yaml", to = "com|iridium|iridiumskyblock")
@Getter
public class IridiumSkyblock extends DependencyPlugin {
    private static IridiumSkyblock instance;
    private Persist persist;

    private CommandManager commandManager;
    private DatabaseManager databaseManager;

    private Configuration configuration;
    private Messages messages;
    private SQL sql;

    @Override
    public void load() {

    }

    @Override
    public void enable() {
        instance = this;
        this.persist = new Persist(Persist.PersistType.YAML);
        this.commandManager = new CommandManager("iridiumskyblock");
        loadConfigs();
        saveConfigs();
        try {
            this.databaseManager = new DatabaseManager();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        new IridiumSkyblockAPI(this);
        getLogger().info("----------------------------------------");
        getLogger().info("");
        getLogger().info(getDescription().getName() + " Enabled!");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("");
        getLogger().info("----------------------------------------");
    }

    @Override
    public void disable() {
        getLogger().info("-------------------------------");
        getLogger().info("");
        getLogger().info(getDescription().getName() + " Disabled!");
        getLogger().info("");
        getLogger().info("-------------------------------");
    }

    public void loadConfigs() {
        this.configuration = persist.load(Configuration.class);
        this.messages = persist.load(Messages.class);
        this.sql = persist.load(SQL.class);
    }

    public void saveConfigs() {
        this.persist.save(configuration);
        this.persist.save(messages);
        this.persist.save(sql);
    }

    public static IridiumSkyblock getInstance() {
        return instance;
    }
}
