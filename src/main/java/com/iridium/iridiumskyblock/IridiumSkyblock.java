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
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.*;
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
    private BankItems bankItems;
    private Missions missions;

    private ChunkGenerator chunkGenerator;
    private List<Permission> permissionList;
    private List<BankItem> bankItemList;
    private HashMap<String, Mission> missionsList;

    private Economy economy;

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

        this.economy = setupEconomy();

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

        resetIslandMissions();

        getLogger().info("----------------------------------------");
        getLogger().info("");
        getLogger().info(getDescription().getName() + " Enabled!");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("");
        getLogger().info("----------------------------------------");
    }

    private void resetIslandMissions() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                IridiumSkyblock.getInstance().getDatabaseManager().deleteDailyMissions();
                Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> resetIslandMissions());
            }
        }, c.getTime());
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
        Bukkit.getPluginManager().registerEvents(new ItemCraftListener(), this);
        Bukkit.getPluginManager().registerEvents(new EnchantItemListener(), this);
        Bukkit.getPluginManager().registerEvents(new FurnaceSmeltListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerFishListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockGrowListener(), this);
        Bukkit.getPluginManager().registerEvents(new PotionBrewListener(), this);
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
        getDatabaseManager().saveIslandBank();
        getDatabaseManager().saveIslandMissions();
    }

    private Economy setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().warning("You do not have an economy plugin installed (Like Essentials)");
            return null;
        }
        return rsp.getProvider();
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
        this.bankItems = persist.load(BankItems.class);
        this.missions = persist.load(Missions.class);

        this.permissionList = new ArrayList<>();
        this.permissionList.add(permissions.redstone);
        this.permissionList.add(permissions.blockPlace);
        this.permissionList.add(permissions.blockBreak);
        this.permissionList.add(permissions.bucket);
        this.permissionList.add(permissions.doors);
        this.permissionList.add(permissions.killMobs);
        this.permissionList.add(permissions.openContainers);
        this.permissionList.add(permissions.spawners);
        this.permissionList.add(permissions.changePermissions);
        this.permissionList.add(permissions.kick);
        this.permissionList.add(permissions.invite);
        this.permissionList.add(permissions.regen);
        this.permissionList.add(permissions.promote);
        this.permissionList.add(permissions.demote);
        this.permissionList.add(permissions.pickupItems);
        this.permissionList.add(permissions.dropItems);
        this.permissionList.add(permissions.interactEntities);

        this.bankItemList = new ArrayList<>();
        this.bankItemList.add(bankItems.crystalsBankItem);
        this.bankItemList.add(bankItems.experienceBankItem);
        this.bankItemList.add(bankItems.moneyBankItem);

        this.missionsList = new HashMap<>(missions.missions);
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
        this.persist.save(bankItems);
        this.persist.save(missions);
    }

    public static IridiumSkyblock getInstance() {
        return instance;
    }
}
