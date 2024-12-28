package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.managers.DatabaseManager;
import com.iridium.iridiumskyblock.managers.tablemanagers.*;
import com.iridium.iridiumteams.IridiumTeams;
import com.iridium.iridiumteams.commands.Command;
import com.iridium.iridiumteams.database.*;
import com.iridium.iridiumteams.gui.ConfirmationGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ClearDataCommand extends Command < Island, User > {

    public ClearDataCommand() {
        super(Collections.singletonList("cleardata"), "Deletes data from database", "%prefix% &7/is cleardata <table> <team> --skip-confirm", "iridiumSkyblock.clearData", 10);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args, IridiumTeams < Island, User > iridiumTeams) {
        DataTable table = DataTable.ALL;
        boolean skipConfirmation = false;
        Optional < Island > island = Optional.empty();

        if (args.length == 0 || Arrays.stream(DataTable.values()).noneMatch(dataTable -> dataTable.name().equalsIgnoreCase(args[0]))) {
            sender.sendMessage(StringUtils.color(syntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        switch (args.length) {
            case 2: {
                if (!args[1].equalsIgnoreCase("--skip-confirm")) {
                    island = IridiumSkyblock.getInstance().getIslandManager().getTeamViaNameOrPlayer(args[1]);
                    if (!island.isPresent()) {
                        sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teamDoesntExistByName
                                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                        return false;
                    }
                }
            }
            case 1: {
                try {
                    if (Arrays.stream(DataTable.values()).anyMatch(dataTable -> dataTable.name().equalsIgnoreCase(args[0])))
                        table = DataTable.valueOf(args[0]);
                } catch (IllegalArgumentException e) {
                    IridiumSkyblock.getInstance().getLogger().warning(e.getMessage());
                    return false;
                }
            }
        }

        if (Arrays.stream(args).anyMatch(argument -> argument.equalsIgnoreCase("--skip-confirm")))
            skipConfirmation = true;

        if (sender instanceof Player && !skipConfirmation) {
            confirmDataDeletion((Player) sender, island, table);
            return true;
        }
        deleteData(sender, island, table);

        return true;
    }

    private void confirmDataDeletion(Player player, Optional < Island > island, DataTable table) {
        String islandName = "ALL";
        if (island.isPresent()) islandName = island.get().getName() + " (" + island.get().getOwner().get().getName() + ")";
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().confirmDataDeletion
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                .replace("%table%", table.name())
                .replace("%island%", islandName)));

        player.openInventory(new ConfirmationGUI < > (() -> deleteData(player, island, table), IridiumSkyblock.getInstance()).getInventory());
    }

    private CompletableFuture < Void > deleteData(CommandSender sender, Optional < Island > island, DataTable table) {
        return CompletableFuture.runAsync(() -> {

            if (!backupDatabaseFile()) return;

            List < Island > islands = new ArrayList < > ();
            if (!island.isPresent()) {
                islands = IridiumSkyblock.getInstance().getIslandManager().getTeams();
            } else islands.add(island.get());

            boolean all = table == DataTable.ALL;

            DatabaseManager databaseManager = IridiumSkyblock.getInstance().getDatabaseManager();

            try {
                switch (table) {
                    case ALL:
                    case ISLAND: {
                        databaseManager.getIslandTableManager().delete(islands).join();
                        if (!all) break;
                    }
                    case INVITE: {
                        ForeignIslandTableManager < String, TeamInvite > inviteTableManager = databaseManager.getInvitesTableManager();
                        for (Island islandEntry: islands) {
                            for (TeamInvite teamInvite: inviteTableManager.getEntries(islandEntry)) {
                                inviteTableManager.delete(teamInvite).join();
                            }
                        }
                        if (!all) break;
                    }
                    case TRUST: {
                        ForeignIslandTableManager < String, TeamTrust > trustTableManager = databaseManager.getTrustTableManager();
                        for (Island islandEntry: islands) {
                            for (TeamTrust teamTrust: trustTableManager.getEntries(islandEntry)) {
                                trustTableManager.delete(teamTrust).join();
                            }
                        }
                        if (!all) break;
                    }
                    case PERMISSION: {
                        ForeignIslandTableManager < String, TeamPermission > permissionTableManager = databaseManager.getPermissionsTableManager();
                        for (Island islandEntry: islands) {
                            for (TeamPermission teamPermission: permissionTableManager.getEntries(islandEntry)) {
                                permissionTableManager.delete(teamPermission).join();
                            }
                        }
                        if (!all) break;
                    }
                    case BANK: {
                        ForeignIslandTableManager < String, TeamBank > bankTableManager = databaseManager.getBankTableManager();
                        for (Island islandEntry: islands) {
                            for (TeamBank teamBank: bankTableManager.getEntries(islandEntry)) {
                                bankTableManager.delete(teamBank).join();
                            }
                        }
                        if (!all) break;
                    }
                    case ENHANCEMENT: {
                        ForeignIslandTableManager < String, TeamEnhancement > enhancementTableManager = databaseManager.getEnhancementTableManager();
                        for (Island islandEntry: islands) {
                            for (TeamEnhancement teamEnhancement: enhancementTableManager.getEntries(islandEntry)) {
                                enhancementTableManager.delete(teamEnhancement).join();
                            }
                        }
                        if (!all) break;
                    }
                    case TEAM_BLOCK: {
                        ForeignIslandTableManager < String, TeamBlock > blockTableManager = databaseManager.getTeamBlockTableManager();
                        for (Island islandEntry: islands) {
                            for (TeamBlock teamBlock: blockTableManager.getEntries(islandEntry)) {
                                blockTableManager.delete(teamBlock).join();
                            }
                        }
                        if (!all) break;
                    }
                    case TEAM_SPAWNER: {
                        ForeignIslandTableManager < String, TeamSpawners > spawnerTableManager = databaseManager.getTeamSpawnerTableManager();
                        for (Island islandEntry: islands) {
                            for (TeamSpawners teamSpawners: spawnerTableManager.getEntries(islandEntry)) {
                                spawnerTableManager.delete(teamSpawners).join();
                            }
                        }
                        if (!all) break;
                    }
                    case TEAM_WARP: {
                        ForeignIslandTableManager < String, TeamWarp > warpTableManager = databaseManager.getTeamWarpTableManager();
                        for (Island islandEntry: islands) {
                            for (TeamWarp teamWarp: warpTableManager.getEntries(islandEntry)) {
                                warpTableManager.delete(teamWarp).join();
                            }
                        }
                        if (!all) break;
                    }
                    case TEAM_MISSION: {
                        ForeignIslandTableManager < String, TeamMission > missionTableManager = databaseManager.getTeamMissionTableManager();
                        for (Island islandEntry: islands) {
                            for (TeamMission teamMission: missionTableManager.getEntries(islandEntry)) {
                                IridiumSkyblock.getInstance().getIslandManager().deleteTeamMission(teamMission);
                                IridiumSkyblock.getInstance().getIslandManager().deleteTeamMissionData(teamMission);
                            }
                        }
                        if (!all) break;
                    }

                    case TEAM_REWARDS: {
                        ForeignIslandTableManager < String, TeamReward > rewardTableManager = databaseManager.getTeamRewardsTableManager();
                        for (Island islandEntry: islands) {
                            for (TeamReward teamReward: rewardTableManager.getEntries(islandEntry)) {
                                rewardTableManager.delete(teamReward).join();
                            }
                        }
                        if (!all) break;
                    }
                    case TEAM_SETTINGS: {
                        ForeignIslandTableManager < String, TeamSetting > settingTableManager = databaseManager.getTeamSettingsTableManager();
                        for (Island islandEntry: islands) {
                            for (TeamSetting teamSetting: settingTableManager.getEntries(islandEntry)) {
                                settingTableManager.delete(teamSetting).join();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                IridiumSkyblock.getInstance().getLogger().warning(e.getMessage());
                return;
            }

            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dataDeletion
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        });
    }

    private boolean backupDatabaseFile() {
        IridiumSkyblock.getInstance().getLogger().info("Creating a backup for IridiumSkyblock.db in \"backups\" folder...");

        File pluginFolder = new File(IridiumSkyblock.getInstance().getDataFolder().getPath());
        File file = new File(pluginFolder + File.separator + "IridiumSkyblock.db");
        File backupFolder = new File(pluginFolder.getPath() + File.separator + "backups");
        File backupDatabaseFile = new File(backupFolder + File.separator + file.getName());

        try {
            if (!backupFolder.exists()) backupFolder.mkdir();
            Files.copy(file.toPath(), backupDatabaseFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            IridiumSkyblock.getInstance().getLogger().info("Success! Backup \"IridiumSkyblock.db\" created, check \"" + backupFolder.getPath() + "\".");
            return true;
        } catch (IOException exception) {
            IridiumSkyblock.getInstance().getLogger().severe("Failed to move \"IridiumSkyblock.db\" to " + backupFolder + ": " +
                    exception.getMessage());
            return false;
        }
    }

    @Override
    public List < String > onTabComplete(CommandSender commandSender, String[] args, IridiumTeams < Island, User > iridiumTeams) {
        switch (args.length) {
            case 1: {
                List < String > tables = new ArrayList < > ();
                for (DataTable table: DataTable.values()) {
                    tables.add(table.name());
                }
                return tables;
            }
            case 2: {
                List < String > fullTabComplete = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
                fullTabComplete.add("--skip-confirm");
                return fullTabComplete;
            }
            case 3:
                return Collections.singletonList("--skip-confirm");
            default:
                return Collections.emptyList();
        }
    }
}

enum DataTable {
    ALL,
    ISLAND,
    INVITE,
    TRUST,
    PERMISSION,
    BANK,
    ENHANCEMENT,
    TEAM_BLOCK,
    TEAM_SPAWNER,
    TEAM_WARP,
    TEAM_MISSION,
    TEAM_REWARDS,
    TEAM_SETTINGS;
}