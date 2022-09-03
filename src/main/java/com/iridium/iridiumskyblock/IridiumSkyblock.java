package com.iridium.iridiumskyblock;

import com.iridium.iridiumskyblock.configs.*;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.generators.VoidGenerator;
import com.iridium.iridiumskyblock.listeners.BlockFormListener;
import com.iridium.iridiumskyblock.listeners.PlayerJoinListener;
import com.iridium.iridiumskyblock.listeners.PlayerMoveListener;
import com.iridium.iridiumskyblock.managers.CommandManager;
import com.iridium.iridiumskyblock.managers.DatabaseManager;
import com.iridium.iridiumskyblock.managers.IslandManager;
import com.iridium.iridiumskyblock.managers.UserManager;
import com.iridium.iridiumskyblock.placeholders.IslandPlaceholderBuilder;
import com.iridium.iridiumskyblock.placeholders.TeamChatPlaceholderBuilder;
import com.iridium.iridiumskyblock.placeholders.UserPlaceholderBuilder;
import com.iridium.iridiumteams.IridiumTeams;
import com.iridium.iridiumteams.managers.MissionManager;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.sql.SQLException;

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

    private IslandPlaceholderBuilder teamsPlaceholderBuilder;
    private UserPlaceholderBuilder userPlaceholderBuilder;
    private TeamChatPlaceholderBuilder teamChatPlaceholderBuilder;

    private IslandManager teamManager;
    private UserManager userManager;
    private CommandManager commandManager;
    private DatabaseManager databaseManager;
    private MissionManager<Island, User> missionManager;

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

        this.userManager = new UserManager();
        this.commandManager = new CommandManager("iridiumskyblock");
        this.databaseManager = new DatabaseManager();
        this.missionManager = new MissionManager<>(this);
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
        super.loadConfigs();
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
    }

    @Override
    public void saveData() {
        getDatabaseManager().getUserTableManager().save();
        getDatabaseManager().getIslandTableManager().save();
        getDatabaseManager().getInvitesTableManager().save();
        getDatabaseManager().getPermissionsTableManager().save();
        getDatabaseManager().getBankTableManager().save();
        getDatabaseManager().getEnhancementTableManager().save();
        getDatabaseManager().getTeamBlockTableManager().save();
        getDatabaseManager().getTeamSpawnerTableManager().save();
        getDatabaseManager().getTeamWarpTableManager().save();
        getDatabaseManager().getTeamMissionTableManager().save();
        getDatabaseManager().getTeamMissionDataTableManager().save();
        getDatabaseManager().getTeamRewardsTableManager().save();
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
        addEnhancement("members", getEnhancements().membersEnhancement);
        addEnhancement("warps", getEnhancements().warpsEnhancement);
        addEnhancement("void", getEnhancements().voidEnhancement);
        addEnhancement("generator", getEnhancements().generatorEnhancement);
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
