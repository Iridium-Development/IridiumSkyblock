package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.configs.Commands;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.InventoryConfigGUI;
import com.iridium.iridiumskyblock.managers.CooldownProvider;
import com.iridium.iridiumskyblock.utils.TimeUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

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

        // This code registers all commands from the Commands config automatically
        for (Field field : commands.getClass().getFields()) {
            try {
                Command command = (Command) field.get(commands);
                registerCommand(command);
            } catch (IllegalAccessException exception) {
                exception.printStackTrace();
            }
        }
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
                    if (IridiumSkyblock.getInstance().getConfiguration().islandMenu) {
                        player.openInventory(new InventoryConfigGUI(IridiumSkyblock.getInstance().getInventories().islandMenu, player.getOpenInventory().getTopInventory()).getInventory());
                    } else {
                        IridiumSkyblock.getInstance().getCommands().helpCommand.execute(player, new String[]{});
                    }
                } else {
                    IridiumSkyblock.getInstance().getCommands().createCommand.execute(player, new String[]{});
                }
            } else {
                IridiumSkyblock.getInstance().getCommands().helpCommand.execute(commandSender, new String[]{});
            }

            return true;
        }

        for (Command command : commands) {
            // We don't want to execute other commands or ones that are disabled
            if (!(command.aliases.contains(args[0]))) {
                continue;
            }

            Command executingCommand = findExecutingCommand(command, args);
            if (executionBlocked(executingCommand, commandSender)) {
                return false;
            }

            boolean success = executingCommand.execute(commandSender, args);
            if (success) executingCommand.getCooldownProvider().applyCooldown(commandSender);
            return true;
        }

        // Unknown command message
        commandSender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().unknownCommand.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        return false;
    }

    /**
     * Checks if a commandSender can execute a command or not
     *
     * @param command       The specified Command
     * @param commandSender The command sender
     * @return a true/false boolean
     */
    private boolean executionBlocked(Command command, CommandSender commandSender) {
        // Check if this command is only for players
        if (command.onlyForPlayers && !(commandSender instanceof Player)) {
            // Must be a player
            commandSender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().mustBeAPlayer.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return true;
        }

        // Check permissions
        if (!hasPermissions(commandSender, command)) {
            // No permissions
            commandSender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noPermission.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return true;
        }

        // Check cooldown
        CooldownProvider<CommandSender> cooldownProvider = command.getCooldownProvider();
        boolean bypassing = commandSender instanceof Player && IridiumSkyblockAPI.getInstance().getUser((Player) commandSender).isBypassing();
        if (commandSender instanceof Player && !bypassing && cooldownProvider.isOnCooldown(commandSender)) {
            Duration remainingTime = cooldownProvider.getRemainingTime(commandSender);
            String formattedTime = TimeUtils.formatDuration(IridiumSkyblock.getInstance().getMessages().activeCooldown, remainingTime);

            commandSender.sendMessage(StringUtils.color(formattedTime.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return true;
        }

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
                    if (alias.toLowerCase().startsWith(args[0].toLowerCase()) && hasPermissions(commandSender, command)) {
                        result.add(alias);
                    }
                }
            }

            return result;
        }

        // Let the sub-command handle the tab completion
        for (Command command : commands) {
            if (command.aliases.contains(args[0])) {
                Command executingCommand = findExecutingCommand(command, args);
                if (hasPermissions(commandSender, executingCommand)) {
                    List<String> tabCompletion = new ArrayList<>(executingCommand.onTabComplete(commandSender, cmd, label, args));

                    // Add all child commands the CommandSender has permissions for
                    executingCommand.childs.stream()
                            .filter(subCommand -> hasPermissions(commandSender, subCommand))
                            .map(subCommand -> subCommand.aliases.get(0))
                            .forEach(tabCompletion::add);

                    return filterTabCompletionResults(tabCompletion, args);
                }
            }
        }

        // Return a new List, so it isn't a list of online players
        return Collections.emptyList();
    }

    private boolean hasPermissions(@NotNull CommandSender commandSender, Command command) {
        return commandSender.hasPermission(command.permission)
                || command.permission.equalsIgnoreCase("")
                || command.permission.equalsIgnoreCase("iridiumskyblock.");
    }

    public Optional<Command> findExecutingCommand(String[] arguments) {
        if (arguments.length == 0) {
            return Optional.empty();
        }

        for (Command command : commands) {
            if (command.aliases.contains(arguments[0].toLowerCase())) {
                return Optional.of(findExecutingCommand(command, arguments));
            }
        }

        return Optional.empty();
    }

    private Command findExecutingCommand(Command baseCommand, String[] args) {
        Command executingCommand = baseCommand;

        // Check for each argument if it's a child of the previous command
        for (int currentArgument = 1; currentArgument < args.length; currentArgument++) {
            Optional<Command> child = executingCommand.getChildByName(args[currentArgument]);
            if (!child.isPresent()) break;

            executingCommand = child.get();
        }

        return executingCommand;
    }

    private List<String> filterTabCompletionResults(List<String> tabCompletion, String[] arguments) {
        return tabCompletion.stream()
                .filter(completion -> completion.toLowerCase().contains(arguments[arguments.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }

}
