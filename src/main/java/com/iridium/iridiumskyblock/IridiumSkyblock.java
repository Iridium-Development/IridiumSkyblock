package com.iridium.iridiumskyblock;

import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.commands.BlockValues;
import com.iridium.iridiumskyblock.commands.CommandManager;
import com.iridium.iridiumskyblock.configs.*;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.generators.SkyblockGenerator;
import com.iridium.iridiumskyblock.listeners.*;
import com.iridium.iridiumskyblock.managers.DatabaseManager;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.iridium.iridiumskyblock.managers.SchematicManager;
import com.iridium.iridiumskyblock.managers.UserManager;
import com.iridium.iridiumskyblock.nms.NMS;
import com.iridium.iridiumskyblock.nms.v1_16_R3;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * The main class of this plugin which handles initialization
 * and shutdown of the plugin.
 */
@Getter
public class IridiumSkyblock extends JavaPlugin {

    private static IridiumSkyblock instance;

    private Persist persist;
    private NMS nms;

    private CommandManager commandManager;
    private DatabaseManager databaseManager;
    private IslandManager islandManager;
    private UserManager userManager;
    private SchematicManager schematicManager;

    private Configuration configuration;
    private Messages messages;
    private SQL sql;
    private Schematics schematics;
    private Inventories inventories;
    private Permissions permissions;
    private BlockValues blockValues;

    private ChunkGenerator chunkGenerator;
    private List<Permission> permissionList;

    /**
     * Code that should be executed before this plugin gets enabled.
     * Sets the default world generator.
     */
    @Override
    public void onLoad() {
        chunkGenerator = new SkyblockGenerator();
    }

    /**
     * Plugin startup logic.
     */
    @Override
    public void onEnable() {
        instance = this;

        getDataFolder().mkdir();

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

        // Initialize the manager classes (bad) and create the world
        this.islandManager = new IslandManager();
        this.userManager = new UserManager();
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

        //Auto recalculate islands
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            ListIterator<Integer> islands = getDatabaseManager().getIslandList().stream().map(Island::getId).collect(Collectors.toList()).listIterator();

            @Override
            public void run() {
                if (!islands.hasNext()) {
                    islands = getDatabaseManager().getIslandList().stream().map(Island::getId).collect(Collectors.toList()).listIterator();
                } else {
                    getIslandManager().getIslandById(islands.next()).ifPresent(island -> getIslandManager().recalculateIsland(island));
                }
            }
        }, 0, getConfiguration().islandRecalculateInterval * 20L);

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
    public void onDisable() {
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
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), this);
        Bukkit.getPluginManager().registerEvents(new BucketListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityPickupItemListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDropItemListener(), this);
    }

    /**
     * Saves islands and users to the database.
     */
    public void saveData() {
        getDatabaseManager().saveIslands();
        getDatabaseManager().saveUsers();
        getDatabaseManager().saveIslandInvites();
        getDatabaseManager().saveIslandPermissions();
        getDatabaseManager().saveIslandBlocks();
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
        this.permissions = persist.load(Permissions.class);
        this.blockValues = persist.load(BlockValues.class);

        permissionList = new ArrayList<>();
        permissionList.add(permissions.redstone);
        permissionList.add(permissions.blockPlace);
        permissionList.add(permissions.blockBreak);
        permissionList.add(permissions.bucket);
        permissionList.add(permissions.doors);
        permissionList.add(permissions.killMobs);
        permissionList.add(permissions.openContainers);
        permissionList.add(permissions.spawners);
        permissionList.add(permissions.changePermissions);
        permissionList.add(permissions.kick);
        permissionList.add(permissions.invite);
        permissionList.add(permissions.regen);
        permissionList.add(permissions.promote);
        permissionList.add(permissions.demote);
        permissionList.add(permissions.pickupItems);
        permissionList.add(permissions.dropItems);
        permissionList.add(permissions.interactEntities);
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
        this.persist.save(permissions);
        this.persist.save(blockValues);
    }

    public static IridiumSkyblock getInstance() {
        return instance;
    }
}
