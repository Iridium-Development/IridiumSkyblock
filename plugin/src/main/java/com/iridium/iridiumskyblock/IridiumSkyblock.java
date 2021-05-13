package com.iridium.iridiumskyblock;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.bank.BankItem;
import com.iridium.iridiumskyblock.commands.CommandManager;
import com.iridium.iridiumskyblock.configs.*;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.gui.GUI;
import com.iridium.iridiumskyblock.listeners.*;
import com.iridium.iridiumskyblock.managers.DatabaseManager;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.iridium.iridiumskyblock.managers.SchematicManager;
import com.iridium.iridiumskyblock.managers.UserManager;
import com.iridium.iridiumskyblock.multiversion.MultiVersion;
import com.iridium.iridiumskyblock.nms.NMS;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import io.papermc.lib.PaperLib;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.InventoryHolder;
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
    private MultiVersion multiversion;

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
    private Upgrades upgrades;
    private Boosters boosters;

    private ChunkGenerator chunkGenerator;

    private List<Permission> permissionList;
    private List<BankItem> bankItemList;
    private HashMap<String, Mission> missionsList;
    private HashMap<String, Upgrade> upgradesList;
    private HashMap<String, Booster> boosterList;

    private Economy economy;

    /**
     * The default constructor.
     */
    public IridiumSkyblock() {
        instance = this;
    }

    /**
     * Returns the plugin's instance of this class.
     *
     * @return Instance of this class
     */
    public static IridiumSkyblock getInstance() {
        return instance;
    }

    /**
     * Code that should be executed before this plugin gets enabled.
     * Initializes the configurations and sets the default world generator.
     */
    @Override
    public void onLoad() {
        // Create the data folder in order to make Jackson work
        getDataFolder().mkdir();

        // Initialize the configs
        this.persist = new Persist(Persist.PersistType.YAML, this);
        loadConfigs();
        saveConfigs();

        // Initialize the ChunkGenerator
        this.chunkGenerator = configuration.generatorSettings.generatorType.getChunkGenerator();
    }

    /**
     * Plugin startup logic.
     */
    @Override
    public void onEnable() {

        this.nms = setupNMS();
        if (this.nms == null) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.multiversion = setupMultiversion();

        if (!PaperLib.isSpigot()) {
            // isSpigot returns true if the server is using spigot or a fork
            getLogger().warning("CraftBukkit isnt supported, please use spigot or one of its forks");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Initialize the commands
        this.commandManager = new CommandManager("iridiumskyblock");

        // Initialize the manager classes (bad) and create the world
        this.islandManager = new IslandManager();
        this.userManager = new UserManager();
        this.islandManager.createWorld(World.Environment.NORMAL, configuration.worldName);
        this.islandManager.createWorld(World.Environment.NETHER, configuration.worldName + "_nether");
        this.islandManager.createWorld(World.Environment.THE_END, configuration.worldName + "_the_end");

        this.databaseManager = new DatabaseManager();
        // Try to connect to the database
        try {
            databaseManager.init();
        } catch (SQLException exception) {
            // We don't want the plugin to start if the connection fails
            exception.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Initialize the API
        IridiumSkyblockAPI.initializeAPI(this);

        this.schematicManager = new SchematicManager();

        // Save data regularly
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, this::saveData, 0, 20 * 60 * 5);

        registerListeners();

        // Initialize Vault economy support
        this.economy = setupEconomy();

        // Send island border to all players
        Bukkit.getOnlinePlayers().forEach(player -> getIslandManager().getIslandViaLocation(player.getLocation()).ifPresent(island -> PlayerUtils.sendBorder(player, island)));

        // Auto recalculate islands
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            ListIterator<Integer> islands = getDatabaseManager().getIslandTableManager().getEntries().stream().map(Island::getId).collect(Collectors.toList()).listIterator();

            @Override
            public void run() {
                if (!islands.hasNext()) {
                    islands = getDatabaseManager().getIslandTableManager().getEntries().stream().map(Island::getId).collect(Collectors.toList()).listIterator();
                } else {
                    getIslandManager().getIslandById(islands.next()).ifPresent(island -> getIslandManager().recalculateIsland(island));
                }
            }
        }, 0, getConfiguration().islandRecalculateInterval * 20L);

        // Automatically update all inventories
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            InventoryHolder inventoryHolder = player.getOpenInventory().getTopInventory().getHolder();
            if (inventoryHolder instanceof GUI) {
                ((GUI) inventoryHolder).addContent(player.getOpenInventory().getTopInventory());
            }
        }), 0, 20);

        resetIslandMissions();

        new Metrics(this, 5825);

        getLogger().info("----------------------------------------");
        getLogger().info("");
        getLogger().info(getDescription().getName() + " Enabled!");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("");
        getLogger().info("----------------------------------------");
    }

    /**
     * Automatically resets the Island missions in a defined time intervall.
     */
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
                databaseManager.getIslandMissionTableManager().delete(
                        databaseManager.getIslandMissionTableManager().getEntries().stream().filter(islandMission ->
                                islandMission.getType() == Mission.MissionType.DAILY).collect(Collectors.toList()
                        )
                );
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
     * Registers the plugin's listeners.
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
        Bukkit.getPluginManager().registerEvents(new EntityDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockFormListener(), this);
        Bukkit.getPluginManager().registerEvents(new SpawnerSpawnListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerPortalListener(), this);
    }

    /**
     * Saves islands, users and other data to the database.
     */
    public void saveData() {
        getDatabaseManager().getUserTableManager().save();
        getDatabaseManager().getIslandTableManager().save();
        getDatabaseManager().getIslandInviteTableManager().save();
        getDatabaseManager().getIslandPermissionTableManager().save();
        getDatabaseManager().getIslandBlocksTableManager().save();
        getDatabaseManager().getIslandBankTableManager().save();
        getDatabaseManager().getIslandMissionTableManager().save();
        getDatabaseManager().getIslandRewardTableManager().save();
        getDatabaseManager().getSchematicTableManager().save();
        getDatabaseManager().getIslandUpgradeTableManager().save();
        getDatabaseManager().getIslandTrustedTableManager().save();
        getDatabaseManager().getIslandBoosterTableManager().save();
        getDatabaseManager().getIslandWarpTableManager().save();
    }

    /**
     * Tries to initialize the Vault support.
     *
     * @return Vault's economy interface, null if none is found
     */
    private Economy setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider == null) {
            getLogger().warning("You do not have an economy plugin installed (like Essentials)");
            return null;
        }
        return economyProvider.getProvider();
    }

    /**
     * Automatically gets the correct NMS version from minecraft version
     *
     * @return The correct NMS Version
     */
    private NMS setupNMS() {
        String version = Bukkit.getServer().getClass().getPackage().getName().toUpperCase().split("\\.")[3];
        try {
            return (NMS) Class.forName("com.iridium.iridiumskyblock.nms." + version).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            getLogger().warning("Un-Supported Minecraft Version: " + version);
        }
        return null;
    }

    /**
     * Automatically gets the correct Multiversion version from minecraft version
     *
     * @return The correct Multiversion Version
     */
    private MultiVersion setupMultiversion() {
        String version = Bukkit.getServer().getClass().getPackage().getName().toUpperCase().split("\\.")[3];
        try {
            return (MultiVersion) Class.forName("com.iridium.iridiumskyblock.multiversion." + version).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            getLogger().warning("Un-Supported Minecraft Version: " + version);
        }
        return null;
    }

    /**
     * Loads the configurations required for this plugin.
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
        this.upgrades = persist.load(Upgrades.class);
        this.boosters = persist.load(Boosters.class);

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
        this.permissionList.add(permissions.manageWarps);
        this.permissionList.add(permissions.withdrawBank);
        this.permissionList.add(permissions.trust);
        this.permissionList.add(permissions.border);

        this.bankItemList = new ArrayList<>();
        this.bankItemList.add(bankItems.crystalsBankItem);
        this.bankItemList.add(bankItems.experienceBankItem);
        this.bankItemList.add(bankItems.moneyBankItem);

        this.missionsList = new HashMap<>(missions.missions);
        this.upgradesList = new HashMap<>(ImmutableMap.<String, Upgrade>builder()
                .put("size", upgrades.sizeUpgrade)
                .put("generator", upgrades.oresUpgrade)
                .put("warp", upgrades.warpsUpgrade)
                .build());

        this.boosterList = new HashMap<>(ImmutableMap.<String, Booster>builder()
                .put("experience", boosters.islandExperienceBooster)
                .put("flight", boosters.islandFlightBooster)
                .put("farming", boosters.islandFarmingBooster)
                .put("spawner", boosters.islandSpawnerBooster)
                .build());
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
        this.persist.save(upgrades);
        this.persist.save(boosters);
    }

}
