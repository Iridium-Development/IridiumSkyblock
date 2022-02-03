package com.iridium.iridiumskyblock;

import com.iridium.iridiumcore.Color;
import com.iridium.iridiumcore.IridiumCore;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.NumberFormatter;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.api.IridiumSkyblockReloadEvent;
import com.iridium.iridiumskyblock.bank.BankItem;
import com.iridium.iridiumskyblock.commands.CommandManager;
import com.iridium.iridiumskyblock.configs.*;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import com.iridium.iridiumskyblock.database.IslandMission;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.GUI;
import com.iridium.iridiumskyblock.listeners.*;
import com.iridium.iridiumskyblock.managers.*;
import com.iridium.iridiumskyblock.placeholders.ClipPlaceholderAPI;
import com.iridium.iridiumskyblock.placeholders.MVDWPlaceholderAPI;
import com.iridium.iridiumskyblock.shop.ShopManager;
import com.iridium.iridiumskyblock.support.RoseStackerSupport;
import com.iridium.iridiumskyblock.support.StackerSupport;
import com.iridium.iridiumskyblock.support.WildStackerSupport;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import de.jeff_media.updatechecker.UpdateChecker;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
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
    private MissionManager missionManager;
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
    private Border border;
    private Placeholders placeholders;
    private IslandSettings islandSettings;

    private ChunkGenerator chunkGenerator;

    private List<BankItem> bankItemList;
    private Map<String, Permission> permissionList;
    private Map<String, Setting> settingsList;
    private Map<String, Mission> missionsList;
    private Map<String, Upgrade<?>> upgradesList;
    private Map<String, Booster> boosterList;

    private Economy economy;

    private StackerSupport stackerSupport;

    /**
     * The default constructor.
     */
    public IridiumSkyblock() {
        instance = this;
    }

    /**
     * The unit test constructor.
     *
     * @param loader      The JavaPluginLoader
     * @param description The PluginDescriptionFile
     * @param dataFolder  The data folder File
     * @param file        A file
     */
    public IridiumSkyblock(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
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
        DataConverter.copyLegacyData();

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

        this.missionManager = new MissionManager();

        this.shopManager = new ShopManager();
        shopManager.reloadCategories();

        // Initialize the API
        IridiumSkyblockAPI.initializeAPI(this);

        this.schematicManager = new SchematicManager();

        // Initialize Vault economy support
        Bukkit.getScheduler().runTask(this, () -> this.economy = setupEconomy());

        this.stackerSupport = registerBlockStackerSupport();

        registerPlaceholderSupport();

        // Send island border to all players
        Bukkit.getOnlinePlayers().forEach(player -> getIslandManager().getIslandViaLocation(player.getLocation()).ifPresent(island -> PlayerUtils.sendBorder(player, island)));

        // Auto recalculate islands
        if (getConfiguration().islandRecalculateInterval > 0) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                ListIterator<Integer> islands = getDatabaseManager().getIslandTableManager().getAllIslandID().listIterator();

                @Override
                public void run() {
                    if (!islands.hasNext()) {
                        islands = getDatabaseManager().getIslandTableManager().getAllIslandID().listIterator();
                    } else {
                        getIslandManager().getIslandById(islands.next()).ifPresent(island -> getIslandManager().recalculateIsland(island, null));
                    }
                }

            }, 0, getConfiguration().islandRecalculateInterval * 20L);
        }

        // Automatically update all inventories
        Bukkit.getScheduler().runTaskTimer(this, () -> Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            try {
                InventoryHolder inventoryHolder = player.getOpenInventory().getTopInventory().getHolder();
                if (inventoryHolder instanceof GUI gui) {
                    gui.addContent(player.getOpenInventory().getTopInventory());
                }
            } catch (Exception e) {
                System.out.println("Error : IridiumSkyblock#runTaskTimer#inventoryHolder");
                e.printStackTrace();
            }

        }), 0, 20);

        // Register worlds with multiverse
        if (Bukkit.getPluginManager().isPluginEnabled("Multiverse-Core")) {
            Bukkit.getScheduler().runTaskLater(this, () -> {
                registerMultiverse(islandManager.getWorld());
                registerMultiverse(islandManager.getNetherWorld());
                registerMultiverse(islandManager.getEndWorld());
            }, 1);
        }

        resetIslandMissions();

        Metrics metrics = new Metrics(this, 5825);
        metrics.addCustomChart(new SimplePie("database_type", () -> sql.driver.name()));

        if (getConfiguration().enableCheckVersion) {
            UpdateChecker.init(this, 62480)
                    .checkEveryXHours(24)
                    .setDownloadLink(62480)
                    .setColoredConsoleOutput(true)
                    .checkNow();
        }

        getLogger().info("----------------------------------------");
        getLogger().info("");
        getLogger().info(getDescription().getName() + " Enabled!");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("");
        getLogger().info("----------------------------------------");

        playerStopGlitchBorder();
    }

    private void registerPlaceholderSupport() {
        Plugin MVDWPlaceholderAPI = getServer().getPluginManager().getPlugin("MVdWPlaceholderAPI");
        if (MVDWPlaceholderAPI != null && MVDWPlaceholderAPI.isEnabled()) {
            new MVDWPlaceholderAPI();
            getLogger().info("Successfully registered " + com.iridium.iridiumskyblock.placeholders.Placeholders.placeholders.size() + " placeholders with MVDWPlaceholderAPI.");
        }

        Plugin PlaceholderAPI = getServer().getPluginManager().getPlugin("PlaceholderAPI");
        if (PlaceholderAPI != null && PlaceholderAPI.isEnabled()) {
            if (new ClipPlaceholderAPI().register()) {
                getLogger().info("Successfully registered " + com.iridium.iridiumskyblock.placeholders.Placeholders.placeholders.size() + " placeholders with PlaceholderAPI.");
            }
        }
    }

    private StackerSupport registerBlockStackerSupport() {
        if (Bukkit.getPluginManager().isPluginEnabled("RoseStacker")) return new RoseStackerSupport();
        if (Bukkit.getPluginManager().isPluginEnabled("WildStacker")) return new WildStackerSupport();
        return new StackerSupport() {
            @Override
            public int getExtraBlocks(Island island, XMaterial material) {
                return 0;
            }

            @Override
            public int getExtraSpawners(Island island, EntityType entityType) {
                return 0;
            }
        };
    }

    /**
     * Automatically resets the Island missions in a defined time interval.
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
                List<IslandMission> missionList = databaseManager.getIslandMissionTableManager().getAllEntries().stream().filter(islandMission ->
                        islandMission.getType() == Mission.MissionType.DAILY).toList();
                databaseManager.getIslandMissionTableManager().delete(missionList);
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
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new BlockBreakListener(), this);
        pluginManager.registerEvents(new BlockExplodeListener(), this);
        pluginManager.registerEvents(new BlockFormListener(), this);
        pluginManager.registerEvents(new BlockFromToListener(), this);
        pluginManager.registerEvents(new BlockGrowListener(), this);
        pluginManager.registerEvents(new BlockPistonListener(), this);
        pluginManager.registerEvents(new BlockPlaceListener(), this);
        pluginManager.registerEvents(new BlockSpreadListener(), this);
        pluginManager.registerEvents(new BucketListener(), this);
        pluginManager.registerEvents(new EnchantItemListener(), this);
        pluginManager.registerEvents(new EntityChangeBlockListener(), this);
        pluginManager.registerEvents(new EntityDamageListener(), this);
        pluginManager.registerEvents(new EntityDeathListener(), this);
        pluginManager.registerEvents(new EntityExplodeListener(), this);
        pluginManager.registerEvents(new EntityPickupItemListener(), this);
        pluginManager.registerEvents(new EntitySpawnListener(), this);
        pluginManager.registerEvents(new EntityTargetListener(), this);
        pluginManager.registerEvents(new FurnaceSmeltListener(), this);
        pluginManager.registerEvents(new InventoryClickListener(), this);
        pluginManager.registerEvents(new ItemCraftListener(), this);
        pluginManager.registerEvents(new LeavesDecayListener(), this);
        pluginManager.registerEvents(new PlayerChatListener(), this);
        pluginManager.registerEvents(new PlayerDropItemListener(), this);
        pluginManager.registerEvents(new PlayerFishListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(), this);
        pluginManager.registerEvents(new PlayerJoinQuitListener(), this);
        pluginManager.registerEvents(new PlayerMoveListener(), this);
        pluginManager.registerEvents(new PlayerPortalListener(), this);
        pluginManager.registerEvents(new PlayerRespawnListener(), this);
        pluginManager.registerEvents(new PlayerTeleportListener(), this);
        pluginManager.registerEvents(new PotionBrewListener(), this);
        pluginManager.registerEvents(new SpawnerSpawnListener(), this);
        pluginManager.registerEvents(new VehicleDamageListener(), this);
        pluginManager.registerEvents(new BlockBurnListener(), this);
        pluginManager.registerEvents(new StructureGrowListener(), this);
        pluginManager.registerEvents(new PlayerHarvestBlockListener(), this);
        pluginManager.registerEvents(new PlayerBucketFillListener(), this);
    }


    private boolean endSave = true; // Est il en pleine save ?
    private boolean isEnd = false;
    /**
     * Saves islands, users and other data to the database.
     */
    @Override
    public void saveData() {

        try {
            System.out.println("Sauvegarde des Users");
            getDatabaseManager().getUserTableManager().saveHashMap(getDatabaseManager().getUserTableManager().getUserIslandMap(), false);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            System.out.println("Sauvegarde des Islands");
            getDatabaseManager().getIslandTableManager().saveHashMap(getDatabaseManager().getIslandTableManager().getIslandMapByID(), false);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            System.out.println("Sauvegarde des Invites");
            getDatabaseManager().getIslandInviteTableManager().saveHashMapList(getDatabaseManager().getIslandInviteTableManager().getIslandInviteById(), true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            System.out.println("Sauvegarde des Permissions");
            getDatabaseManager().getIslandPermissionTableManager().saveHashMapList(getDatabaseManager().getIslandPermissionTableManager().getIslandPermissionById(), true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            System.out.println("Sauvegarde des Spawners");
            getDatabaseManager().getIslandSpawnersTableManager().saveHashMapList(getDatabaseManager().getIslandSpawnersTableManager().getIslandSpawnerById(), true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            System.out.println("Sauvegarde des Banks");
            getDatabaseManager().getIslandBankTableManager().saveHashMapList(getDatabaseManager().getIslandBankTableManager().getIslandBankById(), true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            System.out.println("Sauvegarde des Missions");
            getDatabaseManager().getIslandMissionTableManager().saveHashMapList(getDatabaseManager().getIslandMissionTableManager().getIslandMissionById(), true);
        }  catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            System.out.println("Sauvegarde des Reward");
            getDatabaseManager().getIslandRewardTableManager().saveHashMapList(getDatabaseManager().getIslandRewardTableManager().getIslandRewardById(), true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            System.out.println("Sauvegarde des Upgrades");
            getDatabaseManager().getIslandUpgradeTableManager().saveHashMapList(getDatabaseManager().getIslandUpgradeTableManager().getIslandUpgradeById(), true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            System.out.println("Sauvegarde des Confiances");
            getDatabaseManager().getIslandTrustedTableManager().saveHashMapList(getDatabaseManager().getIslandTrustedTableManager().getIslandTrustedById(), false);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            System.out.println("Sauvegarde des Booster");
            getDatabaseManager().getIslandBoosterTableManager().saveHashMapList(getDatabaseManager().getIslandBoosterTableManager().getIslandBoosterById(), false);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            System.out.println("Sauvegarde des Warps");
            getDatabaseManager().getIslandWarpTableManager().saveHashMapList(getDatabaseManager().getIslandWarpTableManager().getIslandWarpById(), true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            System.out.println("Sauvegarde des Bans");
            getDatabaseManager().getIslandBanTableManager().saveHashMapList(getDatabaseManager().getIslandBanTableManager().getIslandBanById(), true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            System.out.println("Sauvegarde des Blocks");
            getDatabaseManager().getIslandBlocksTableManager().saveHashMapList(getDatabaseManager().getIslandBlocksTableManager().getIslandBlockById(), true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            System.out.println("Sauvegarde des Logs");
            getDatabaseManager().getIslandLogTableManager().saveHashMapList(getDatabaseManager().getIslandLogTableManager().getIslandLogById(), true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        try {
            System.out.println("Sauvegarde des Settings");
            getDatabaseManager().getIslandSettingTableManager().saveHashMapList(getDatabaseManager().getIslandSettingTableManager().getIslandSettingById(), true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        System.out.println("Fin des sauvegardes");
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
     * Registers Multiverse Support for this world
     *
     * @param world The specified World
     */
    private void registerMultiverse(World world) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mv import " + world.getName() + " normal -g " + getName());
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mv modify set generator " + getName() + " " + world.getName());
    }

    /**
     * Loads the configurations required for this plugin.
     */
    @Override
    public void loadConfigs() {
        loadConfigFiles();

        for (Color color : Color.values()) {
            if (!border.enabled.containsKey(color)) {
                border.enabled.put(color, true);
            }
        }

        int maxSize = upgrades.sizeUpgrade.upgrades.values().stream().max(Comparator.comparing(sizeUpgrade -> sizeUpgrade.size)).get().size;
        if (configuration.distance <= maxSize) {
            configuration.distance = maxSize + 1;
        }

        if (inventories.confirmationGUI.yes.slot == null || inventories.confirmationGUI.yes.slot == 0) {
            inventories.confirmationGUI.yes.slot = 15;
        }
        if (inventories.confirmationGUI.no.slot == null || inventories.confirmationGUI.no.slot == 0) {
            inventories.confirmationGUI.no.slot = 1;
        }

        initializePermissionList();
        initializeSettingsList();

        for (Permission permission : permissionList.values()) {
            if (permission.getPage() == 0) permission.setPage(1);
        }

        if (bankItems.crystalsBankItem.getDisplayName() == null) bankItems.crystalsBankItem.setDisplayName("Crystal");
        if (bankItems.experienceBankItem.getDisplayName() == null)
            bankItems.experienceBankItem.setDisplayName("Experience");
        if (bankItems.moneyBankItem.getDisplayName() == null) bankItems.moneyBankItem.setDisplayName("Money");

        if (boosters.experienceBooster.name == null) boosters.experienceBooster.name = "Experience";
        if (boosters.farmingBooster.name == null) boosters.experienceBooster.name = "Farming";
        if (boosters.flightBooster.name == null) boosters.experienceBooster.name = "Flight";
        if (boosters.spawnerBooster.name == null) boosters.experienceBooster.name = "Spawner";

        if (upgrades.warpsUpgrade.name == null) upgrades.warpsUpgrade.name = "Warps";
        if (upgrades.sizeUpgrade.name == null) upgrades.sizeUpgrade.name = "Size";
        if (upgrades.blockLimitUpgrade.name == null) upgrades.blockLimitUpgrade.name = "Block Limit";
        if (upgrades.memberUpgrade.name == null) upgrades.memberUpgrade.name = "Members";
        if (upgrades.oresUpgrade.name == null) upgrades.oresUpgrade.name = "Ore Generator";

        this.bankItemList = new ArrayList<>();
        if (bankItems.crystalsBankItem.isEnabled()) {
            this.bankItemList.add(bankItems.crystalsBankItem);
        }
        if (bankItems.experienceBankItem.isEnabled()) {
            this.bankItemList.add(bankItems.experienceBankItem);
        }
        if (bankItems.moneyBankItem.isEnabled()) {
            this.bankItemList.add(bankItems.moneyBankItem);
        }

        for (Map.Entry<String, Schematics.SchematicConfig> schematics : schematics.schematics.entrySet()) {
            Schematics.SchematicConfig schematic = schematics.getValue();
            if (schematic.overworld.islandHeight == null) schematic.overworld.islandHeight = 90.0;
            if (schematic.overworld.ignoreAirBlocks == null) schematic.overworld.ignoreAirBlocks = true;
            if (schematic.nether.islandHeight == null) schematic.nether.islandHeight = 90.0;
            if (schematic.nether.ignoreAirBlocks == null) schematic.nether.ignoreAirBlocks = true;
            if (schematic.end.islandHeight == null) schematic.end.islandHeight = 90.0;
            if (schematic.end.ignoreAirBlocks == null) schematic.end.ignoreAirBlocks = true;
        }

        this.missionsList = new HashMap<>(missions.missions);

        this.upgradesList = new HashMap<>();
        if (upgrades.sizeUpgrade.enabled)
            upgradesList.put("size", upgrades.sizeUpgrade);
        if (upgrades.memberUpgrade.enabled)
            upgradesList.put("member", upgrades.memberUpgrade);
        if (upgrades.warpsUpgrade.enabled)
            upgradesList.put("warp", upgrades.warpsUpgrade);
        if (upgrades.blockLimitUpgrade.enabled)
            upgradesList.put("blocklimit", upgrades.blockLimitUpgrade);
        if (upgrades.oresUpgrade.enabled) {
            upgradesList.put("generator", upgrades.oresUpgrade);
            BlockFormListener.generateOrePossibilities();
        }

        this.boosterList = new HashMap<>();
        if (boosters.experienceBooster.enabled)
            boosterList.put("experience", boosters.experienceBooster);
        if (boosters.flightBooster.enabled)
            boosterList.put("flight", boosters.flightBooster);
        if (boosters.farmingBooster.enabled)
            boosterList.put("farming", boosters.farmingBooster);
        if (boosters.spawnerBooster.enabled)
            boosterList.put("spawner", boosters.spawnerBooster);

        saveSchematics();

        if (shopManager != null)
            shopManager.reloadCategories();
        if (commandManager != null)
            commandManager.reloadCommands();

        if (schematicManager != null) schematicManager.schematicPaster.clearCache();

        IridiumSkyblockReloadEvent reloadEvent = new IridiumSkyblockReloadEvent();
        Bukkit.getPluginManager().callEvent(reloadEvent);
    }

    private void loadConfigFiles() {
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
        this.border = getPersist().load(Border.class);
        this.placeholders = getPersist().load(Placeholders.class);
        this.islandSettings = getPersist().load(IslandSettings.class);
    }

    private void initializePermissionList() {
        this.permissionList = new HashMap<>();
        this.permissionList.put(PermissionType.REDSTONE.getPermissionKey(), permissions.redstone);
        this.permissionList.put(PermissionType.BLOCK_PLACE.getPermissionKey(), permissions.blockPlace);
        this.permissionList.put(PermissionType.BLOCK_BREAK.getPermissionKey(), permissions.blockBreak);
        this.permissionList.put(PermissionType.BUCKET.getPermissionKey(), permissions.bucket);
        this.permissionList.put(PermissionType.DOORS.getPermissionKey(), permissions.doors);
        this.permissionList.put(PermissionType.KILL_MOBS.getPermissionKey(), permissions.killMobs);
        this.permissionList.put(PermissionType.OPEN_CONTAINERS.getPermissionKey(), permissions.openContainers);
        this.permissionList.put(PermissionType.SPAWNERS.getPermissionKey(), permissions.spawners);
        this.permissionList.put(PermissionType.CHANGE_PERMISSIONS.getPermissionKey(), permissions.changePermissions);
        this.permissionList.put(PermissionType.KICK.getPermissionKey(), permissions.kick);
        this.permissionList.put(PermissionType.INVITE.getPermissionKey(), permissions.invite);
        this.permissionList.put(PermissionType.REGEN.getPermissionKey(), permissions.regen);
        this.permissionList.put(PermissionType.PROMOTE.getPermissionKey(), permissions.promote);
        this.permissionList.put(PermissionType.EXPEL.getPermissionKey(), permissions.expel);
        this.permissionList.put(PermissionType.BAN.getPermissionKey(), permissions.ban);
        this.permissionList.put(PermissionType.UNBAN.getPermissionKey(), permissions.unban);
        this.permissionList.put(PermissionType.DEMOTE.getPermissionKey(), permissions.demote);
        this.permissionList.put(PermissionType.PICKUP_ITEMS.getPermissionKey(), permissions.pickupItems);
        this.permissionList.put(PermissionType.DROP_ITEMS.getPermissionKey(), permissions.dropItems);
        this.permissionList.put(PermissionType.INTERACT_ENTITIES.getPermissionKey(), permissions.interactEntities);
        this.permissionList.put(PermissionType.MANAGE_WARPS.getPermissionKey(), permissions.manageWarps);
        this.permissionList.put(PermissionType.WITHDRAW_BANK.getPermissionKey(), permissions.withdrawBank);
        this.permissionList.put(PermissionType.TRUST.getPermissionKey(), permissions.trust);
        this.permissionList.put(PermissionType.BORDER.getPermissionKey(), permissions.border);
        this.permissionList.put(PermissionType.DESTROY_VEHICLE.getPermissionKey(), permissions.destroyVehicle);
        this.permissionList.put(PermissionType.TRAMPLE_CROPS.getPermissionKey(), permissions.trampleCrops);
        this.permissionList.put(PermissionType.INTERACT.getPermissionKey(), permissions.interact);
        this.permissionList.put(PermissionType.PORTAL.getPermissionKey(), permissions.portal);
        this.permissionList.put(PermissionType.ISLAND_SETTINGS.getPermissionKey(), permissions.islandSettings);
    }

    private void initializeSettingsList() {
        this.settingsList = new HashMap<>();
        this.settingsList.put(SettingType.MOB_SPAWN.getSettingName(), islandSettings.mobSpawn);
        this.settingsList.put(SettingType.LEAF_DECAY.getSettingName(), islandSettings.leafDecay);
        this.settingsList.put(SettingType.WEATHER.getSettingName(), islandSettings.weather);
        this.settingsList.put(SettingType.TIME.getSettingName(), islandSettings.time);
        this.settingsList.put(SettingType.ENDERMAN_GRIEF.getSettingName(), islandSettings.endermanGrief);
        this.settingsList.put(SettingType.LIQUID_FLOW.getSettingName(), islandSettings.liquidFlow);
        this.settingsList.put(SettingType.TNT_DAMAGE.getSettingName(), islandSettings.tntDamage);
        this.settingsList.put(SettingType.FIRE_SPREAD.getSettingName(), islandSettings.fireSpread);
    }

    private void saveSchematics() {
        File schematicFolder = new File(getDataFolder(), "schematics");
        if (!schematicFolder.exists()) {
            schematicFolder.mkdir();
        }

        // Return if there are already schematics in the schematics folder
        if (Objects.requireNonNull(schematicFolder.list()).length != 0) {
            return;
        }

        saveFile(schematicFolder, "desert.schem");
        saveFile(schematicFolder, "mushroom.schem");
        saveFile(schematicFolder, "jungle.schem");
        saveFile(schematicFolder, "desert_nether.schem");
        saveFile(schematicFolder, "mushroom_nether.schem");
        saveFile(schematicFolder, "jungle_nether.schem");
        saveFile(schematicFolder, "desert_end.schem");
        saveFile(schematicFolder, "mushroom_end.schem");
        saveFile(schematicFolder, "jungle_end.schem");
    }

    private void saveFile(File parent, String name) {
        File file = new File(parent, name);
        if (!file.exists()) {
            try {
                InputStream source = getResource(name);
                Path target = file.toPath();

                if (source == null) return;
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                getLogger().warning("Could not copy " + name + " to " + file.getAbsolutePath());
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
        getPersist().save(border);
        getPersist().save(placeholders);
        getPersist().save(islandSettings);
    }

    /**
     * Gets the Number Formatter
     *
     * @return The Number Formatter
     */
    public NumberFormatter getNumberFormatter() {
        return configuration.numberFormatter;
    }

    private void playerStopGlitchBorder() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(getInstance(), () -> {
            Collection<? extends Player> players = Bukkit.getOnlinePlayers();
            for (Player player : players) {
                if (!player.isOnline()) continue; // Check au cas où !
                try {
                    if (!IridiumSkyblockAPI.getInstance().isIslandWorld(player.getWorld())) continue;
                    User playerUser = User.of(player);
                    Optional<Island> island = getInstance().getIslandManager().getIslandViaPlayerLocation(player, playerUser);
                    if (island.isEmpty()) {
                        if (playerUser.isBypassing()) continue;
                        if (playerUser.isFlying() && !player.hasPermission("iridiumskyblock.fly")) {
                            playerUser.setFlying(false);
                            if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
                                player.setFlying(false);
                                player.setAllowFlight(false);
                                player.sendMessage(StringUtils.color(getInstance().getMessages().flightDisabled
                                        .replace("%player%", player.getName())
                                        .replace("%prefix%", getInstance().getConfiguration().prefix))
                                );
                            }
                        }
                        if (player.hasPermission("iridiumskyblock.locationisland.bypass")) continue;
                        Bukkit.getScheduler().runTask(getInstance(), () -> PlayerUtils.teleportSpawn(player));
                    } else {
                        IslandBooster islandBooster = getInstance().getIslandManager().getIslandBooster(island.get(), "flight");
                        if (playerUser.isFlying() && !islandBooster.isActive() && !player.hasPermission("iridiumskyblock.fly")) {
                            playerUser.setFlying(false);
                            if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
                                player.setFlying(false);
                                player.setAllowFlight(false);
                                player.sendMessage(StringUtils.color(getInstance().getMessages().flightDisabled
                                        .replace("%player%", player.getName())
                                        .replace("%prefix%", getInstance().getConfiguration().prefix))
                                );
                            }
                        }
                        if (!player.hasPermission("iridiumskyblock.bypassban") && getInstance().getIslandManager().isBannedOnIsland(island.get(), playerUser)) {
                            player.sendMessage(StringUtils.color(getInstance().getMessages().youHaveBeenBanned
                                    .replace("%prefix%", getInstance().getConfiguration().prefix)
                                    .replace("%owner%", island.get().getOwner().getName())
                                    .replace("%name%", island.get().getName())
                            ));
                            Bukkit.getScheduler().runTask(getInstance(), () -> PlayerUtils.teleportSpawn(player));
                        }
                        if (player.getLocation().getY() < LocationUtils.getMinHeight(player.getWorld()) && getInstance().getConfiguration().voidTeleport && IridiumSkyblockAPI.getInstance().isIslandWorld(player.getWorld())) {
                            Bukkit.getScheduler().runTask(getInstance(), () -> getInstance().getIslandManager().teleportHome(player, playerUser, island.get(), 0));
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error : IridiumSkyblock#runTaskTimerAsynchronously#checkEmplacement");
                    e.printStackTrace();
                }
            }
        },0, 5*20); // toutes les 5 secondes
    }

}
