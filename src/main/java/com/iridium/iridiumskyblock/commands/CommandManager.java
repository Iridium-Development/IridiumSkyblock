package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Commands;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.InventoryConfigGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Handles command executions and tab-completions for all commands of this plugin.
 */
public class CommandManager implements CommandExecutor, TabCompleter {

    public final List<Command> commands = new ArrayList<>();

    /**
     * The default constructor.
     *
     * @param command The base command of the plugin
     */
    public CommandManager(String command) {
        IridiumSkyblock.getInstance().getCommand(command).setExecutor(this);
        IridiumSkyblock.getInstance().getCommand(command).setTabCompleter(this);
        registerCommands();
    }

    /**
     * Registers all commands of this plugin.
     */
    public void registerCommands() {
        Commands commands = IridiumSkyblock.getInstance().getCommands();
        registerCommand(commands.reloadCommand);
        registerCommand(commands.createCommand);
        registerCommand(commands.deleteCommand);
        registerCommand(commands.regenCommand);
        registerCommand(commands.homeCommand);
        registerCommand(commands.aboutCommand);
        registerCommand(commands.setHomeCommand);
        registerCommand(commands.inviteCommand);
        registerCommand(commands.unInviteCommand);
        registerCommand(commands.joinCommand);
        registerCommand(commands.leaveCommand);
        registerCommand(commands.membersCommand);
        registerCommand(commands.kickCommand);
        registerCommand(commands.visitCommand);
        registerCommand(commands.publicCommand);
        registerCommand(commands.privateCommand);
        registerCommand(commands.promoteCommand);
        registerCommand(commands.demoteCommand);
        registerCommand(commands.transferCommand);
        registerCommand(commands.permissionsCommand);
        registerCommand(commands.bypassCommand);
        registerCommand(commands.helpCommand);
        registerCommand(commands.valueCommand);
        registerCommand(commands.topCommand);
        registerCommand(commands.bankCommand);
        registerCommand(commands.withdrawCommand);
        registerCommand(commands.depositCommand);
        registerCommand(commands.positionCommand);
        registerCommand(commands.saveSchematicCommand);
        registerCommand(commands.infoCommand);
        registerCommand(commands.missionCommand);
        registerCommand(commands.blockValueCommand);
        registerCommand(commands.borderCommand);
        registerCommand(commands.rewardsCommand);
        registerCommand(commands.upgradesCommand);
        registerCommand(commands.trustCommand);
        registerCommand(commands.unTrustCommand);
        registerCommand(commands.boostersCommand);
        registerCommand(commands.warpsCommand);
        registerCommand(commands.setWarpCommand);
        registerCommand(commands.editWarpCommand);
        registerCommand(commands.deleteWarpCommand);
        registerCommand(commands.flyCommand);
        registerCommand(commands.levelCommand);
        registerCommand(commands.logsCommand);
        registerCommand(commands.clearDataCommand);
        registerCommand(commands.shopCommand);
        registerCommand(commands.recalculateCommand);
        registerCommand(commands.renameCommand);
        registerCommand(commands.chatCommand);
    }

    /**
     * Registers a single command in the command system.
     *
     * @param command The command which should be registered
     */
    public void registerCommand(Command command) {
        if (command.enabled) {
            int index = Collections.binarySearch(commands, command, Comparator.comparing(cmd -> cmd.aliases.get(0)));
            commands.add(index < 0 ? -(index + 1) : index, command);
        }
    }

    /**
     * Unregisters a single command in the command system.
     *
     * @param command The command which should be unregistered
     */
    public void unregisterCommand(Command command) {
        commands.remove(command);
    }

    /**
     * Reloads all commands
     */
    public void reloadCommands() {
        commands.clear();
        registerCommands();
    }

    /**
     * Method which handles command execution for all sub-commands.
     * Automatically checks if a User can execute the command.
     * All parameters are provided by Bukkit.
     *
     * @param commandSender The sender which executes this command
     * @param cmd           The Bukkit {@link org.bukkit.command.Command} representation
     * @param label         The label of this command. Not used.
     * @param args          The arguments of this command
     * @return true if this command was executed successfully
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
                Optional<Island> island = user.getIsland();
                if (island.isPresent()) {
                    player.openInventory(new InventoryConfigGUI(IridiumSkyblock.getInstance().getInventories().islandMenu).getInventory());
                } else {
                    String command = IridiumSkyblock.getInstance().getCommands().createCommand.aliases.get(0);
                    Bukkit.dispatchCommand(player, "is " + command);
                }
            } else {
                String command = IridiumSkyblock.getInstance().getCommands().helpCommand.aliases.get(0);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "is " + command);
            }

            return true;
        }

        for (Command command : commands) {
            // We don't want to execute other commands or ones that are disabled
            if (!(command.aliases.contains(args[0]))) {
                continue;
            }

            // Check if this command is only for players
            if (command.onlyForPlayers && !(commandSender instanceof Player)) {
                // Must be a player
                commandSender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().mustBeAPlayer
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                return false;
            }

            // Check permissions
            if (!((commandSender.hasPermission(command.permission) || command.permission
                    .equalsIgnoreCase("") || command.permission
                    .equalsIgnoreCase("iridiumskyblock.")))) {
                // No permissions
                commandSender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noPermission
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                return false;
            }

            command.execute(commandSender, args);
            return true;
        }

        // Unknown command message
        commandSender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().unknownCommand.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        return false;
    }

    /**
     * Method which handles tab-completion of the main command and all sub-commands.
     *
     * @param commandSender The CommandSender which tries to tab-complete
     * @param cmd           The command
     * @param label         The label of the command
     * @param args          The arguments already provided by the sender
     * @return The list of tab completions for this command
     */
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command cmd, @NotNull String label, String[] args) {
        // Handle the tab completion if it's the sub-command selection
        if (args.length == 1) {
            ArrayList<String> result = new ArrayList<>();
            for (Command command : commands) {
                for (String alias : command.aliases) {
                    if (alias.toLowerCase().startsWith(args[0].toLowerCase()) && (
                            (commandSender.hasPermission(command.permission)
                                    || command.permission.equalsIgnoreCase("") || command.permission
                                    .equalsIgnoreCase("iridiumskyblock.")))) {
                        result.add(alias);
                    }
                }
            }
            return result;
        }

        // Let the sub-command handle the tab completion
        for (Command command : commands) {
            if (command.aliases.contains(args[0]) && (
                    commandSender.hasPermission(command.permission) || command.permission.equalsIgnoreCase("")
                            || command.permission.equalsIgnoreCase("iridiumskyblock."))) {
                return command.onTabComplete(commandSender, cmd, label, args);
            }
        }

        // We currently don't want to tab-completion here
        // Return a new List so it isn't a list of online players
        return Collections.emptyList();
    }

}
