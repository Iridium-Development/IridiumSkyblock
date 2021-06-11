package com.iridium.iridiumskyblock;

import com.iridium.iridiumcore.IridiumCore;
import com.iridium.iridiumcore.utils.NumberFormatter;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.bank.BankItem;
import com.iridium.iridiumskyblock.commands.CommandManager;
import com.iridium.iridiumskyblock.configs.*;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.listeners.*;
import com.iridium.iridiumskyblock.managers.DatabaseManager;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.iridium.iridiumskyblock.managers.SchematicManager;
import com.iridium.iridiumskyblock.managers.UserManager;
import com.iridium.iridiumskyblock.placeholders.ClipPlaceholderAPI;
import com.iridium.iridiumskyblock.placeholders.MVDWPlaceholderAPI;
import com.iridium.iridiumskyblock.shop.ShopManager;
import com.iridium.iridiumskyblock.support.*;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import de.jeff_media.updatechecker.UpdateChecker;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * The main class of this plugin which handles initialization
 * and shutdown of the plugin.
 */
@Getter
public class IridiumSkyblock extends IridiumCore {

    private static IridiumSkyblock instance;

    private CommandManager commandManager;
    private DatabaseManager databaseManager;
    private IslandManager islandManager;
    private UserManager userManager;
    private SchematicManager schematicManager;
    private ShopManager shopManager;

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
    private Commands commands;
    private Shop shop;

    private ChunkGenerator chunkGenerator;

    private List<BankItem> bankItemList;
    private HashMap<String, Permission> permissionList;
    private HashMap<String, Mission> missionsList;
    private HashMap<String, Upgrade> upgradesList;
    private HashMap<String, Booster> boosterList;

    private Economy economy;
    private SpawnerSupport spawnerSupport;

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
        super.onLoad();

        // Initialize the ChunkGenerator
        this.chunkGenerator = configuration.generatorSettings.generatorType.getChunkGenerator();
    }

    /**
     * Plugin startup logic.
     */
    @Override
    public void onEnable() {
        super.onEnable();

        // Convert old IridiumSkyblock data
        DataConverter.run(this);

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

        this.shopManager = new ShopManager();

        // Initialize the API
        IridiumSkyblockAPI.initializeAPI(this);

        this.schematicManager = new SchematicManager(new File(getDataFolder(), "schematics"));

        this.spawnerSupport = setupSpawnerSupport();

        // Initialize Vault economy support
        Bukkit.getScheduler().runTask(this, () -> this.economy = setupEconomy());

        // Register Placeholders Support
        Plugin MVDWPlaceholderAPI = getServer().getPluginManager().getPlugin("MVdWPlaceholderAPI");
        if (MVDWPlaceholderAPI != null && MVDWPlaceholderAPI.isEnabled()) {
            new MVDWPlaceholderAPI();
            getLogger().info("Successfully registered placeholders with MVDWPlaceholderAPI.");
        }

        Plugin PlaceholderAPI = getServer().getPluginManager().getPlugin("PlaceholderAPI");
        if (PlaceholderAPI != null && PlaceholderAPI.isEnabled()) {
            if (new ClipPlaceholderAPI().register()) {
                getLogger().info("Successfully registered placeholders with PlaceholderAPI.");
            }
        }

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

        Bukkit.getScheduler().runTaskLater(this, () -> {
            registerMultiverse(islandManager.getWorld());
            registerMultiverse(islandManager.getNetherWorld());
            registerMultiverse(islandManager.getEndWorld());
        }, 1);

        resetIslandMissions();

        new Metrics(this, 5825);

        UpdateChecker.init(this, 62480)
                .checkEveryXHours(24)
                .setDownloadLink(62480)
                .setColoredConsoleOutput(true)
                .checkNow();

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
     * Registers the plugin's listeners.
     */
    @Override
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
        Bukkit.getPluginManager().registerEvents(new PlayerRespawnListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerFishListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockGrowListener(), this);
        Bukkit.getPluginManager().registerEvents(new PotionBrewListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockFormListener(), this);
        Bukkit.getPluginManager().registerEvents(new SpawnerSpawnListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerPortalListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPistonListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityExplodeListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockExplodeListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntitySpawnListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), this);
    }

    /**
     * Saves islands, users and other data to the database.
     */
    @Override
    public void saveData() {
        getDatabaseManager().getUserTableManager().save();
        getDatabaseManager().getIslandTableManager().save();
        getDatabaseManager().getIslandInviteTableManager().save();
        getDatabaseManager().getIslandPermissionTableManager().save();
        getDatabaseManager().getIslandBlocksTableManager().save();
        getDatabaseManager().getIslandSpawnersTableManager().save();
        getDatabaseManager().getIslandBankTableManager().save();
        getDatabaseManager().getIslandMissionTableManager().save();
        getDatabaseManager().getIslandRewardTableManager().save();
        getDatabaseManager().getIslandUpgradeTableManager().save();
        getDatabaseManager().getIslandTrustedTableManager().save();
        getDatabaseManager().getIslandBoosterTableManager().save();
        getDatabaseManager().getIslandWarpTableManager().save();
        getDatabaseManager().getIslandLogTableManager().save();
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
     * Gets the SpawnerSupport Object
     *
     * @return The Spawner Support Object
     */
    private SpawnerSupport setupSpawnerSupport() {
        if (Bukkit.getPluginManager().isPluginEnabled("RoseStacker")) return new RoseStackerSupport();
        if (Bukkit.getPluginManager().isPluginEnabled("WildStacker")) return new WildStackerSupport();
        if (Bukkit.getPluginManager().isPluginEnabled("AdvancedSpawners")) return new AdvancedSpawnersSupport();
        if (Bukkit.getPluginManager().isPluginEnabled("UltimateStacker")) return new UltimateStackerSupport();
        if (Bukkit.getPluginManager().isPluginEnabled("EpicSpawners")) return new EpicSpawnersSupport();
        return spawner -> 1;
    }

    /**
     * Registers Multiverse Support for this world
     *
     * @param world The specified World
     */
    private void registerMultiverse(World world) {
        if (Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mv import " + world.getName() + " normal -g " + getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mv modify set generator " + getName() + " " + world.getName());
        }
    }

    /**
     * Loads the configurations required for this plugin.
     */
    @Override
    public void loadConfigs() {
        this.configuration = getPersist().load(Configuration.class);
        this.messages = getPersist().load(Messages.class);
        this.sql = getPersist().load(SQL.class);
        this.schematics = getPersist().load(Schematics.class);
        this.inventories = getPersist().load(Inventories.class);
        this.permissions = getPersist().load(Permissions.class);
        this.blockValues = getPersist().load(BlockValues.class);
        this.bankItems = getPersist().load(BankItems.class);
        this.missions = getPersist().load(Missions.class);
        this.upgrades = getPersist().load(Upgrades.class);
        this.boosters = getPersist().load(Boosters.class);
        this.commands = getPersist().load(Commands.class);
        this.shop = getPersist().load(Shop.class);

        int maxSize = upgrades.sizeUpgrade.upgrades.values().stream().max(Comparator.comparing(sizeUpgrade -> sizeUpgrade.size)).get().size;
        if (configuration.distance <= maxSize) {
            configuration.distance = maxSize + 1;
        }

        this.permissionList = new HashMap<>();
        this.permissionList.put("redstone", permissions.redstone);
        this.permissionList.put("blockPlace", permissions.blockPlace);
        this.permissionList.put("blockBreak", permissions.blockBreak);
        this.permissionList.put("bucket", permissions.bucket);
        this.permissionList.put("doors", permissions.doors);
        this.permissionList.put("killMobs", permissions.killMobs);
        this.permissionList.put("openContainers", permissions.openContainers);
        this.permissionList.put("spawners", permissions.spawners);
        this.permissionList.put("changePermissions", permissions.changePermissions);
        this.permissionList.put("kick", permissions.kick);
        this.permissionList.put("invite", permissions.invite);
        this.permissionList.put("regen", permissions.regen);
        this.permissionList.put("promote", permissions.promote);
        this.permissionList.put("demote", permissions.demote);
        this.permissionList.put("pickupItems", permissions.pickupItems);
        this.permissionList.put("dropItems", permissions.dropItems);
        this.permissionList.put("interactEntities", permissions.interactEntities);
        this.permissionList.put("manageWarps", permissions.manageWarps);
        this.permissionList.put("withdrawBank", permissions.withdrawBank);
        this.permissionList.put("trust", permissions.trust);
        this.permissionList.put("border", permissions.border);

        this.bankItemList = new ArrayList<>();
        this.bankItemList.add(bankItems.crystalsBankItem);
        this.bankItemList.add(bankItems.experienceBankItem);
        this.bankItemList.add(bankItems.moneyBankItem);

        this.missionsList = new HashMap<>(missions.missions);

        this.upgradesList = new HashMap<>();
        if (upgrades.sizeUpgrade.enabled) upgradesList.put("size", upgrades.sizeUpgrade);
        if (upgrades.oresUpgrade.enabled) upgradesList.put("generator", upgrades.oresUpgrade);
        if (upgrades.memberUpgrade.enabled) upgradesList.put("member", upgrades.memberUpgrade);
        if (upgrades.warpsUpgrade.enabled) upgradesList.put("warp", upgrades.warpsUpgrade);

        this.boosterList = new HashMap<>();
        if (boosters.experienceBooster.enabled) boosterList.put("experience", boosters.experienceBooster);
        if (boosters.flightBooster.enabled) boosterList.put("flight", boosters.flightBooster);
        if (boosters.farmingBooster.enabled) boosterList.put("farming", boosters.farmingBooster);
        if (boosters.spawnerBooster.enabled) boosterList.put("spawner", boosters.spawnerBooster);

        File schematicFolder = new File(getDataFolder(), "schematics");
        if (!schematicFolder.exists()) {
            schematicFolder.mkdir();
        }
        saveFile(schematicFolder, "desert.iridiumschem");
        saveFile(schematicFolder, "mushroom.iridiumschem");
        saveFile(schematicFolder, "jungle.iridiumschem");
        saveFile(schematicFolder, "desert_nether.iridiumschem");
        saveFile(schematicFolder, "mushroom_nether.iridiumschem");
        saveFile(schematicFolder, "jungle_nether.iridiumschem");
        saveFile(schematicFolder, "desert_end.iridiumschem");
        saveFile(schematicFolder, "mushroom_end.iridiumschem");
        saveFile(schematicFolder, "jungle_end.iridiumschem");

        if(shopManager != null)shopManager.reloadCategories();
    }

    private void saveFile(File parent, String name) {
        File file = new File(parent, name);
        if (!file.exists()) {
            if (getResource(name) != null) {
                InputStream in = this.getResource(name);
                if (in != null) {
                    try {
                        OutputStream out = new FileOutputStream(file);
                        byte[] buf = new byte[1024];

                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }

                        out.close();
                        in.close();
                    } catch (IOException var10) {
                        getLogger().log(Level.SEVERE, "Could not save " + file.getName() + " to " + file, var10);
                    }

                }
            }
        }
    }


    /**
     * Saves changes to the configuration files.
     */
    @Override
    public void saveConfigs() {
        getPersist().save(configuration);
        getPersist().save(messages);
        getPersist().save(sql);
        getPersist().save(schematics);
        getPersist().save(inventories);
        getPersist().save(permissions);
        getPersist().save(blockValues);
        getPersist().save(bankItems);
        getPersist().save(missions);
        getPersist().save(upgrades);
        getPersist().save(boosters);
        getPersist().save(commands);
        getPersist().save(shop);
    }

    /**
     * Gets the Number Formatter
     *
     * @return The Number Formatter
     */
    public NumberFormatter getNumberFormatter() {
        return configuration.numberFormatter;
    }

}
