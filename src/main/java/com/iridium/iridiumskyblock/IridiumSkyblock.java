package com.iridium.iridiumskyblock;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.configs.*;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.generators.VoidGenerator;
import com.iridium.iridiumskyblock.listeners.*;
import com.iridium.iridiumskyblock.managers.*;
import com.iridium.iridiumskyblock.placeholders.IslandPlaceholderBuilder;
import com.iridium.iridiumskyblock.placeholders.TeamChatPlaceholderBuilder;
import com.iridium.iridiumskyblock.placeholders.UserPlaceholderBuilder;
import com.iridium.iridiumteams.IridiumTeams;
import com.iridium.iridiumteams.managers.MissionManager;
import com.iridium.iridiumteams.managers.ShopManager;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Objects;

@Getter
public class IridiumSkyblock extends IridiumTeams<Island, User> {
    private static IridiumSkyblock instance;

    private Configuration configuration;
    private Messages messages;
    private Permissions permissions;
    private Inventories inventories;
    private Commands commands;
    private BankItems bankItems;
    private Enhancements enhancements;
    private BlockValues blockValues;
    private Top top;
    private SQL sql;
    private Missions missions;
    private Schematics schematics;
    private Shop shop;
    private Biomes biomes;
    private Settings settings;

    private IslandPlaceholderBuilder teamsPlaceholderBuilder;
    private UserPlaceholderBuilder userPlaceholderBuilder;
    private TeamChatPlaceholderBuilder teamChatPlaceholderBuilder;

    private IslandManager teamManager;
    private UserManager userManager;
    private CommandManager commandManager;
    private DatabaseManager databaseManager;
    private MissionManager<Island, User> missionManager;
    private SchematicManager schematicManager;
    private ShopManager<Island, User> shopManager;
    private BiomeManager biomeManager;

    private Economy economy;

    private ChunkGenerator chunkGenerator;

    public IridiumSkyblock(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        instance = this;
    }

    public IridiumSkyblock() {
        instance = this;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.chunkGenerator = new VoidGenerator();
    }

    @Override
    public void onEnable() {
        instance = this;

        this.teamManager = new IslandManager();

        this.teamManager.createWorld(World.Environment.NORMAL, configuration.worldName);
        this.teamManager.createWorld(World.Environment.NETHER, configuration.worldName + "_nether");
        this.teamManager.createWorld(World.Environment.THE_END, configuration.worldName + "_the_end");

        this.schematicManager = new SchematicManager();
        this.userManager = new UserManager();
        this.commandManager = new CommandManager("iridiumskyblock");
        this.databaseManager = new DatabaseManager();
        this.missionManager = new MissionManager<>(this);
        this.shopManager = new ShopManager<>(this);
        this.biomeManager = new BiomeManager();
        try {
            databaseManager.init();
        } catch (SQLException exception) {
            // We don't want the plugin to start if the connection fails
            exception.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.teamsPlaceholderBuilder = new IslandPlaceholderBuilder();
        this.userPlaceholderBuilder = new UserPlaceholderBuilder();
        this.teamChatPlaceholderBuilder = new TeamChatPlaceholderBuilder();

        Bukkit.getScheduler().runTask(this, () -> this.economy = setupEconomy());

        Bukkit.getServer().getOnlinePlayers().forEach(player -> getIslandManager().sendIslandBorder(player));

        addBstats(5825);
        startUpdateChecker(62480);
        super.onEnable();
    }

    private Economy setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider == null) {
            getLogger().warning("You do not have an economy plugin installed (like Essentials)");
            return null;
        }
        return economyProvider.getProvider();
    }

    @Override
    public void registerListeners() {
        super.registerListeners();
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockFormListener(), this);
        Bukkit.getPluginManager().registerEvents(new EnhancementUpdateListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerTeleportListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerPortalListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), this);
        if(!XMaterial.supports(15)) Bukkit.getPluginManager().registerEvents(new PortalCreateListener(), this);
    }

    @Override
    public void loadConfigs() {
        this.configuration = getPersist().load(Configuration.class);
        this.messages = getPersist().load(Messages.class);
        this.commands = getPersist().load(Commands.class);
        this.sql = getPersist().load(SQL.class);
        this.inventories = getPersist().load(Inventories.class);
        this.permissions = getPersist().load(Permissions.class);
        this.bankItems = getPersist().load(BankItems.class);
        this.enhancements = getPersist().load(Enhancements.class);
        this.blockValues = getPersist().load(BlockValues.class);
        this.top = getPersist().load(Top.class);
        this.missions = getPersist().load(Missions.class);
        this.schematics = getPersist().load(Schematics.class);
        this.shop = getPersist().load(Shop.class);
        this.biomes = getPersist().load(Biomes.class);
        this.settings = getPersist().load(Settings.class);
        super.loadConfigs();

        int maxSize = enhancements.sizeEnhancement.levels.values().stream()
                .max(Comparator.comparing(sizeUpgrade -> sizeUpgrade.size))
                .map(sizeEnhancementData -> sizeEnhancementData.size)
                .orElse(150);
        if (configuration.distance <= maxSize) {
            getLogger().warning("Distance: " + configuration.distance + " Is too low, must be higher than the maximum island size " + maxSize);
            configuration.distance = maxSize + 1;
            getLogger().warning("New Distance set to: " + configuration.distance);
        }

        if (schematicManager != null)
            schematicManager.reload();
    }

    @Override
    public void saveConfigs() {
        super.saveConfigs();
        getPersist().save(configuration);
        getPersist().save(messages);
        getPersist().save(commands);
        getPersist().save(sql);
        getPersist().save(inventories);
        getPersist().save(permissions);
        getPersist().save(bankItems);
        getPersist().save(enhancements);
        getPersist().save(blockValues);
        getPersist().save(top);
        getPersist().save(missions);
        getPersist().save(schematics);
        getPersist().save(shop);
        getPersist().save(biomes);
        getPersist().save(settings);
        saveSchematics();
    }

    @Override
    public void saveData() {
        getDatabaseManager().getUserTableManager().save();
        getDatabaseManager().getIslandTableManager().save();
        getDatabaseManager().getInvitesTableManager().save();
        getDatabaseManager().getTrustTableManager().save();
        getDatabaseManager().getPermissionsTableManager().save();
        getDatabaseManager().getBankTableManager().save();
        getDatabaseManager().getEnhancementTableManager().save();
        getDatabaseManager().getTeamBlockTableManager().save();
        getDatabaseManager().getTeamSpawnerTableManager().save();
        getDatabaseManager().getTeamWarpTableManager().save();
        getDatabaseManager().getTeamMissionTableManager().save();
        getDatabaseManager().getTeamMissionDataTableManager().save();
        getDatabaseManager().getTeamRewardsTableManager().save();
        getDatabaseManager().getTeamSettingsTableManager().save();
        getDatabaseManager().getLostItemsTableManager().save();
    }

    @Override
    public void initializeBankItem() {
        super.initializeBankItem();
        addBankItem(getBankItems().crystalsBankItem);
    }

    @Override
    public void initializeEnhancements() {
        super.initializeEnhancements();
        addEnhancement("size", getEnhancements().sizeEnhancement);
        addEnhancement("void", getEnhancements().voidEnhancement);
        addEnhancement("generator", getEnhancements().generatorEnhancement);
    }

    @Override
    public void initializePermissions() {
        super.initializePermissions();
        addPermission("border", getPermissions().border);
        addPermission("regen", getPermissions().regen);
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

                if (source == null)
                    return;
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                getLogger().warning("Could not copy " + name + " to " + file.getAbsolutePath());
            }
        }
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return this.chunkGenerator;
    }

    public IslandManager getIslandManager() {
        return teamManager;
    }

    public static IridiumSkyblock getInstance() {
        return instance;
    }
}
