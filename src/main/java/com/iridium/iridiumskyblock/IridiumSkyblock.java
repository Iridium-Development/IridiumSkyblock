package com.iridium.iridiumskyblock;

import com.heretere.hdl.dependency.maven.annotation.MavenDependency;
import com.heretere.hdl.relocation.annotation.Relocation;
import com.heretere.hdl.spigot.DependencyPlugin;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.commands.CommandManager;
import com.iridium.iridiumskyblock.configs.*;
import com.iridium.iridiumskyblock.generators.SkyblockGenerator;
import com.iridium.iridiumskyblock.listeners.InventoryClickListener;
import com.iridium.iridiumskyblock.listeners.PlayerJoinListener;
import com.iridium.iridiumskyblock.listeners.PlayerTeleportListener;
import com.iridium.iridiumskyblock.managers.DatabaseManager;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.iridium.iridiumskyblock.managers.SchematicManager;
import com.iridium.iridiumskyblock.nms.NMS;
import com.iridium.iridiumskyblock.nms.v1_16_R3;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    private static IridiumSkyblock instance;

    private Persist persist;
    private NMS nms;

    private CommandManager commandManager;
    private DatabaseManager databaseManager;
    private IslandManager islandManager;
    private SchematicManager schematicManager;

    private Configuration configuration;
    private Messages messages;
    private SQL sql;
    private Schematics schematics;
    private Inventories inventories;

    private ChunkGenerator chunkGenerator;

    @Override
    public void load() {
        chunkGenerator = new SkyblockGenerator();
    }

    /**
     * Plugin startup logic.
     */
    @Override
    public void enable() {
        instance = this;
        // Initialize the configs
        this.persist = new Persist(Persist.PersistType.YAML, this);
        loadConfigs();
        saveConfigs();

        // Initialize the commands
        this.commandManager = new CommandManager("iridiumskyblock");

        // Try to connect to the database
        try {
            this.databaseManager = new DatabaseManager();
        } catch (SQLException exception) {
            // We don't want the plugin to start if the connection fails
            exception.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
        this.islandManager = new IslandManager();
        this.islandManager.createWorld(World.Environment.NORMAL, configuration.worldName);

        // Initialize the API
        IridiumSkyblockAPI.initializeAPI(this);

        this.schematicManager = new SchematicManager();

        // Save data regularly
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, this::saveData, 0, 20 * 60 * 5);

        registerListeners();

        this.nms = new v1_16_R3();

        //Send island border to all players
        Bukkit.getOnlinePlayers().forEach(player -> IridiumSkyblockAPI.getInstance().getIslandViaLocation(player.getLocation()).ifPresent(island -> PlayerUtils.sendBorder(player, island)));

        getLogger().info("----------------------------------------");
        getLogger().info("");
        getLogger().info(getDescription().getName() + " Enabled!");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("");
        getLogger().info("----------------------------------------");
    }

    /**
     * Sets the {@link ChunkGenerator} for the Skyblock worlds.
     */
    @Nullable
    @Override
    public ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return this.chunkGenerator;
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
     * Registers the plugin's listeners
     */
    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerTeleportListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    /**
     * Saves islands and users to the database.
     */
    public void saveData() {
        getDatabaseManager().saveIslands();
        getDatabaseManager().saveUsers();
        getDatabaseManager().saveIslandInvites();
    }

    /**
     * Loads the configuration required for this plugin.
     *
     * @see Persist
     */
    public void loadConfigs() {
        this.configuration = persist.load(Configuration.class);
        this.messages = persist.load(Messages.class);
        this.sql = persist.load(SQL.class);
        this.schematics = persist.load(Schematics.class);
        this.inventories = persist.load(Inventories.class);
    }

    /**
     * Saves changes to the configuration files.
     *
     * @see Persist
     */
    public void saveConfigs() {
        this.persist.save(configuration);
        this.persist.save(messages);
        this.persist.save(sql);
        this.persist.save(schematics);
        this.persist.save(inventories);
    }

    public static IridiumSkyblock getInstance() {
        return instance;
    }
}
