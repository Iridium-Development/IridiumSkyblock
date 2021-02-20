package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles command executions and tab-completions for all commands of this plugin.
 */
public class CommandManager implements CommandExecutor, TabCompleter {

    private final IridiumSkyblock iridiumSkyblock;
    public final List<Command> commands = new ArrayList<>();

    /**
     * The default constructor.
     * @param command The base command of the plugin
     * @param iridiumSkyblock The instance of IridiumSkyblock used by this plugin
     */
    public CommandManager(String command, IridiumSkyblock iridiumSkyblock) {
        this.iridiumSkyblock = iridiumSkyblock;
        iridiumSkyblock.getCommand(command).setExecutor(this);
        iridiumSkyblock.getCommand(command).setTabCompleter(this);
    }

    /**
     * Registers all commands of this plugin.
     */
    public void registerCommands() {
        // TODO: Add and register commands
    }

    /**
     * Registers a single command in the command system.
     * @param command The command which should be registered
     */
    public void registerCommand(Command command) {
        commands.add(command);
    }

    /**
     * Unregisters a single command in the command system.
     * @param command The command which should be registered
     */
    public void unregisterCommand(Command command) {
        commands.remove(command);
    }

    /**
     * Method which handles command execution for all sub-commands.
     * Automatically checks if a User can execute the command.
     * All parameters are provided by Bukkit.
     *
     * @param commandSender The sender which executes this command
     * @param cmd The Bukkit {@link org.bukkit.command.Command} representation
     * @param label The label of this command. Not used.
     * @param args The arguments of this command
     * @return true if this command was executed successfully
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {
            if (commandSender instanceof Player) {
                Player p = (Player) commandSender;
                // TODO: Send message
                return true;
            }
        }

        for (Command command : commands) {
            // We don't want to execute other commands or ones that are disabled
            if (!(command.aliases.contains(args[0]) && command.enabled)) {
                continue;
            }

            if (command.player && !(commandSender instanceof Player)) {
                // Must be a player
                commandSender.sendMessage(StringUtils.color(iridiumSkyblock.getMessages().mustBeAPlayer
                        .replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)));
                return false;
            }

            if (!((commandSender.hasPermission(command.permission) || command.permission
                    .equalsIgnoreCase("") || command.permission
                    .equalsIgnoreCase("iridiumskyblock.")) && command.enabled)) {
                // No permissions
                commandSender.sendMessage(StringUtils.color(iridiumSkyblock.getMessages().noPermission
                        .replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)));
                return false;
            }

            command.execute(commandSender, args);
            return true;
        }
        // Unknown command message
        commandSender.sendMessage(StringUtils.color(iridiumSkyblock.getMessages().unknownCommand
                .replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)));
        return false;
    }

    /**
     * Method which handles tab-completion of the main command and all sub-commands.
     *
     * @param commandSender The CommandSender which tries to tab-complete
     * @param cmd The command
     * @param label The label of the command
     * @param args The arguments already provided by the sender
     * @return The list of tab completions for this command
     */
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command cmd, @NotNull String label, String[] args) {
        // Handle the tab completion if it's a sub-command.
        if (args.length == 1) {
            ArrayList<String> result = new ArrayList<>();
            for (Command command : commands) {
                for (String alias : command.aliases) {
                    if (alias.toLowerCase().startsWith(args[0].toLowerCase()) && (
                            command.enabled && (commandSender.hasPermission(command.permission)
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
            if (command.aliases.contains(args[0]) && (command.enabled && (
                    commandSender.hasPermission(command.permission) || command.permission.equalsIgnoreCase("")
                            || command.permission.equalsIgnoreCase("iridiumskyblock.")))) {
                return command.onTabComplete(commandSender, cmd, label, args);
            }
        }

        return null;
    }

}
