package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.LostItems;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.IridiumTeams;
import com.iridium.iridiumteams.bank.BankItem;
import com.iridium.iridiumteams.commands.Command;
import com.iridium.iridiumteams.database.*;
import com.iridium.iridiumteams.gui.ConfirmationGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ClearDataCommand extends Command<Island, User> {

    public String adminPermission;

    private List<String> deleteTables = Arrays.asList("all", "team", "team_bank", "team_blocks", "team_spawners",
            "team_enhancements", "team_invites", "team_trusts", "team_missions",
            "team_permissions", "team_settings", "team_warps", "lost_items");

    public ClearDataCommand() {
        super(Collections.singletonList("cleardata"), "Deletes data from database", "%prefix% &7/is cleardata <table> <team> --skip-confirm", "", 10);
        this.adminPermission = "iridiumSkyblock.clearData";
    }

    @Override
    public boolean execute(User user, String[] args, IridiumTeams<Island, User> iridiumTeams) {

        Player player = user.getPlayer();

        if(!player.hasPermission(adminPermission)) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noPermission));
        }

        // COMMAND MAP >> ------------ 0      1     2
        // SCENARIO 0 >> /is cleardata
        // SCENARIO 1 >> /is cleardata table
        // SCENARIO 2 >> /is cleardata table --skip-confirm
        // SCENARIO 3 >> /is cleardata table team
        // SCENARIO 4 >> /is cleardata table team --skip-confirm

        // SCENARIO 0 >> /is cleardata
        if(args.length == 0 || deleteTables.stream().noneMatch(table -> table.equalsIgnoreCase(args[0]))) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().specifyData
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        // SCENARIO 1 >> /is cleardata table
        if(args.length == 1) {
            return confirmDataDeletion(user, args[0]);
        }

        // SCENARIO 2 >> /is cleardata table --skip-confirm
        if(args.length == 2 && args[1].equalsIgnoreCase("--skip-confirm")) {
            return deleteData(user, args[0]);
        }

        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getTeamViaNameOrPlayer(args[1]);
        if(!island.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teamDoesntExistByName
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        // SCENARIO 3 >> /is cleardata table team
        if(args.length == 2) {
            return confirmDataDeletion(user, island.get(), args[0]);
        }

        // SCENARIO 4 >> /is cleardata table team --skip-confirm
        if(args.length == 3 && args[2].equalsIgnoreCase("--skip-confirm")) {
            return deleteData(user, island.get(), args[0]);
        }

        player.sendMessage(StringUtils.color(syntax
                .replace("%prefix%", iridiumTeams.getConfiguration().prefix)
        ));

        return false;

    }

    private boolean confirmDataDeletion(User user, String table) {
        Player player = user.getPlayer();
        player.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().confirmDataDeletion
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                .replace("%table%", table)
                .replace("%island%", "all")));

        Bukkit.getScheduler().scheduleSyncDelayedTask(IridiumSkyblock.getInstance(), () ->
                player.openInventory(new ConfirmationGUI<>(() -> {
                    if (!deleteData(user, table)) return;
                }, IridiumSkyblock.getInstance()).getInventory()), 100);

        return true;
    }

    private boolean deleteData(User user, String table) {
        Player player = user.getPlayer();
        boolean clearAllData = false;

        try {
            switch (table) {
                case "all": {
                    clearAllData = true;
                }
                case "team": {
                    IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().delete(
                            IridiumSkyblock.getInstance().getIslandManager().getTeams());
                    if (!clearAllData) break;
                }
                case "team_bank": {
                    for(TeamBank bank : IridiumSkyblock.getInstance().getDatabaseManager().getBankTableManager().getEntries()) {
                        IridiumSkyblock.getInstance().getDatabaseManager().getBankTableManager().delete(bank);
                    }
                    if (!clearAllData) break;
                }
                case "team_blocks": {
                    for (TeamBlock block : IridiumSkyblock.getInstance().getDatabaseManager().getTeamBlockTableManager().getEntries()) {
                        IridiumSkyblock.getInstance().getDatabaseManager().getTeamBlockTableManager().delete(block);
                    }
                    if (!clearAllData) break;
                }
                case "team_enhancements": {
                    for (TeamEnhancement enhancement : IridiumSkyblock.getInstance().getDatabaseManager().getEnhancementTableManager().getEntries()) {
                        IridiumSkyblock.getInstance().getDatabaseManager().getEnhancementTableManager().delete(enhancement);
                    }
                    if (!clearAllData) break;
                }
                case "team_invites": {
                    for (TeamInvite invite : IridiumSkyblock.getInstance().getDatabaseManager().getInvitesTableManager().getEntries()) {
                        IridiumSkyblock.getInstance().getIslandManager().deleteTeamInvite(invite);
                    }
                    if (!clearAllData) break;
                }
                case "team_missions": {
                    for (TeamMission mission : IridiumSkyblock.getInstance().getDatabaseManager().getTeamMissionTableManager().getEntries()) {
                        IridiumSkyblock.getInstance().getIslandManager().deleteTeamMission(mission);
                    }
                    if (!clearAllData) break;
                }
                case "team_permissions": {
                    for (TeamPermission permission : IridiumSkyblock.getInstance().getDatabaseManager().getPermissionsTableManager().getEntries()) {
                        IridiumSkyblock.getInstance().getDatabaseManager().getPermissionsTableManager().delete(permission);
                    }
                    if (!clearAllData) break;
                }
                case "team_settings": {
                    for (TeamSetting setting : IridiumSkyblock.getInstance().getDatabaseManager().getTeamSettingsTableManager().getEntries()) {
                        IridiumSkyblock.getInstance().getDatabaseManager().getTeamSettingsTableManager().delete(setting);
                    }
                    if (!clearAllData) break;
                }
                case "team_spawners": {
                    for (TeamSpawners spawner : IridiumSkyblock.getInstance().getDatabaseManager().getTeamSpawnerTableManager().getEntries()) {
                        IridiumSkyblock.getInstance().getDatabaseManager().getTeamSpawnerTableManager().delete(spawner);
                    }
                    if (!clearAllData) break;
                }
                case "team_trusts": {
                    for (TeamTrust trust : IridiumSkyblock.getInstance().getDatabaseManager().getTrustTableManager().getEntries()) {
                        IridiumSkyblock.getInstance().getIslandManager().deleteTeamTrust(trust);
                    }
                    if (!clearAllData) break;
                }
                case "team_warps": {
                    for (TeamWarp warp : IridiumSkyblock.getInstance().getDatabaseManager().getTeamWarpTableManager().getEntries()) {
                        IridiumSkyblock.getInstance().getIslandManager().deleteWarp(warp);
                    }
                    if (!clearAllData) break;
                }
                case "lost_items": {
                    for (LostItems lostItems : IridiumSkyblock.getInstance().getDatabaseManager().getLostItemsTableManager().getEntries()) {
                        IridiumSkyblock.getInstance().getDatabaseManager().getLostItemsTableManager().delete(lostItems);
                    }
                    if (!clearAllData) break;
                }
            }
        } catch (Exception e) {
            IridiumSkyblock.getInstance().getLogger().warning(e.getMessage());
            return false;
        }

        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dataDeletion
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                .replace("%player%", player.getName())));

        return true;
    }

    private boolean confirmDataDeletion(User user, Island island, String table) {
        Player player = user.getPlayer();
        player.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().confirmDataDeletion
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                .replace("%table%", table)
                .replace("%island%", island.getName())));

        Bukkit.getScheduler().scheduleSyncDelayedTask(IridiumSkyblock.getInstance(), () ->
                player.openInventory(new ConfirmationGUI<>(() -> {
                    if (!deleteData(user, island, table)) return;
                }, IridiumSkyblock.getInstance()).getInventory()), 100);

        return true;
    }

    private boolean deleteData(User user, Island island, String table) {
        Player player = user.getPlayer();
        boolean clearAllData = false;

        try {
            switch (table) {
                case "all": {
                    clearAllData = true;
                }
                case "team": {
                    IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().delete(island);
                    if (!clearAllData) break;
                }
                case "team_bank": {
                    for (String bankItem : IridiumSkyblock.getInstance().getBankItemList().stream().map(BankItem::getName).collect(Collectors.toList())) {
                        IridiumSkyblock.getInstance().getDatabaseManager().getBankTableManager().delete(
                                IridiumSkyblock.getInstance().getIslandManager().getTeamBank(island, bankItem));
                    }
                    if (!clearAllData) break;
                }
                case "team_blocks": {
                    for (XMaterial material : XMaterial.values()) {
                        IridiumSkyblock.getInstance().getDatabaseManager().getTeamBlockTableManager().delete(
                                IridiumSkyblock.getInstance().getIslandManager().getTeamBlock(island, material));
                    }
                    if (!clearAllData) break;
                }
                case "team_enhancements": {
                    for (TeamEnhancement enhancement : IridiumSkyblock.getInstance().getDatabaseManager().getEnhancementTableManager().getEntries(island)) {
                        IridiumSkyblock.getInstance().getDatabaseManager().getEnhancementTableManager().delete(enhancement);
                    }
                    if (!clearAllData) break;
                }
                case "team_invites": {
                    for (TeamInvite invite : IridiumSkyblock.getInstance().getDatabaseManager().getInvitesTableManager().getEntries(island)) {
                        IridiumSkyblock.getInstance().getIslandManager().deleteTeamInvite(invite);
                    }
                    if (!clearAllData) break;
                }
                case "team_missions": {
                    for (TeamMission mission : IridiumSkyblock.getInstance().getIslandManager().getTeamMissions(island)) {
                        IridiumSkyblock.getInstance().getIslandManager().deleteTeamMission(mission);
                    }
                    if (!clearAllData) break;
                }
                case "team_permissions": {
                    for (TeamPermission permission : IridiumSkyblock.getInstance().getDatabaseManager().getPermissionsTableManager().getEntries(island)) {
                        IridiumSkyblock.getInstance().getDatabaseManager().getPermissionsTableManager().delete(permission);
                    }
                    if (!clearAllData) break;
                }
                case "team_settings": {
                    for (TeamSetting setting : IridiumSkyblock.getInstance().getDatabaseManager().getTeamSettingsTableManager().getEntries(island)) {
                        IridiumSkyblock.getInstance().getDatabaseManager().getTeamSettingsTableManager().delete(setting);
                    }
                    if (!clearAllData) break;
                }
                case "team_spawners": {
                    for (TeamSpawners spawner : IridiumSkyblock.getInstance().getDatabaseManager().getTeamSpawnerTableManager().getEntries(island)) {
                        IridiumSkyblock.getInstance().getDatabaseManager().getTeamSpawnerTableManager().delete(spawner);
                    }
                    if (!clearAllData) break;
                }
                case "team_trusts": {
                    for (TeamTrust trust : IridiumSkyblock.getInstance().getIslandManager().getTeamTrusts(island)) {
                        IridiumSkyblock.getInstance().getIslandManager().deleteTeamTrust(trust);
                    }
                    if (!clearAllData) break;
                }
                case "team_warps": {
                    for (TeamWarp warp : IridiumSkyblock.getInstance().getIslandManager().getTeamWarps(island)) {
                        IridiumSkyblock.getInstance().getIslandManager().deleteWarp(warp);
                    }
                    if (!clearAllData) break;
                }
                case "lost_items": {
                    for(User member : IridiumSkyblock.getInstance().getIslandManager().getTeamMembers(island)) {
                        IridiumSkyblock.getInstance().getDatabaseManager().getLostItemsTableManager().getEntries()
                                .stream().filter(lostItems -> lostItems.getUuid().equals(member.getUuid()))
                                .forEach(lostItems -> IridiumSkyblock.getInstance().getDatabaseManager().getLostItemsTableManager().delete(lostItems));
                    }
                    if (!clearAllData) break;
                }
            }
        } catch (Exception e) {
            IridiumSkyblock.getInstance().getLogger().warning(e.getMessage());
            return false;
        }

        player.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dataDeletion
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                .replace("%player%", player.getName())));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args, IridiumTeams<Island, User> iridiumTeams) {
        if (!commandSender.hasPermission(adminPermission)) return Collections.emptyList();

        switch (args.length) {
            case 1:
                return deleteTables;
            case 2: {
                List<String> fullTabComplete = new ArrayList<>();
                fullTabComplete.addAll(IridiumSkyblock.getInstance().getIslandManager().getTeams().stream().map(Team::getName).collect(Collectors.toList()));
                fullTabComplete.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
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