package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
        registerCommand(new ReloadCommand());
        registerCommand(new CreateCommand());
        registerCommand(new DeleteCommand());
        registerCommand(new RegenCommand());
        registerCommand(new HomeCommand());
        registerCommand(new AboutCommand());
        registerCommand(new SetHomeCommand());
        registerCommand(new InviteCommand());
        registerCommand(new UnInviteCommand());
        registerCommand(new JoinCommand());
        registerCommand(new LeaveCommand());
        registerCommand(new MembersCommand());
        registerCommand(new KickCommand());
        registerCommand(new VisitCommand());
        registerCommand(new PublicCommand());
        registerCommand(new PrivateCommand());
        registerCommand(new PromoteCommand());
        registerCommand(new DemoteCommand());
        registerCommand(new TransferCommand());
        registerCommand(new PermissionsCommand());
        registerCommand(new BypassCommand());
        registerCommand(new HelpCommand());
        registerCommand(new ValueCommand());
        registerCommand(new TopCommand());
        registerCommand(new PositionCommand());
        registerCommand(new SaveSchematicCommand());

        commands.sort(Comparator.comparing(command -> command.aliases.get(0)));
    }

    /**
     * Registers a single command in the command system.
     *
     * @param command The command which should be registered
     */
    public void registerCommand(Command command) {
        commands.add(command);
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
                Player p = (Player) commandSender;
                User user = IridiumSkyblockAPI.getInstance().getUser(p);
                Optional<Island> island = user.getIsland();
                if (island.isPresent()) {
                    IridiumSkyblock.getInstance().getIslandManager().teleportHome(p, island.get());
                } else {
                    Bukkit.getServer().dispatchCommand(p, "is create");
                }
                return true;
            }
        }

        for (Command command : commands) {
            // We don't want to execute other commands or ones that are disabled
            if (!(command.aliases.contains(args[0]) && command.enabled)) {
                continue;
            }

            if (command.onlyForPlayers && !(commandSender instanceof Player)) {
                // Must be a player
                commandSender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().mustBeAPlayer
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                return false;
            }

            if (!((commandSender.hasPermission(command.permission) || command.permission
                    .equalsIgnoreCase("") || command.permission
                    .equalsIgnoreCase("iridiumskyblock.")) && command.enabled)) {
                // No permissions
                commandSender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noPermission
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                return false;
            }

            command.execute(commandSender, args);
            return true;
        }
        // Unknown command message
        commandSender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().unknownCommand
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
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
