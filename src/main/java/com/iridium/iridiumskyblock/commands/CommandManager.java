package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {

    public List<Command> commands = new ArrayList<>();
    private final IridiumSkyblock iridiumSkyblock;

    public CommandManager(String command, IridiumSkyblock iridiumSkyblock) {
        this.iridiumSkyblock = iridiumSkyblock;
        iridiumSkyblock.getCommand(command).setExecutor(this);
        iridiumSkyblock.getCommand(command).setTabCompleter(this);
    }

    public void registerCommands() {
    }

    public void registerCommand(Command command) {
        commands.add(command);
    }

    public void unRegisterCommand(Command command) {
        commands.remove(command);
    }

    @Override
    public boolean onCommand(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        if (args.length != 0) {
            for (Command command : commands) {
                if (command.aliases.contains(args[0]) && command.enabled) {
                    if (command.player && !(cs instanceof Player)) {
                        // Must be a player
                        cs.sendMessage(StringUtils.color(iridiumSkyblock.getMessages().mustBeAPlayer
                                .replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)));
                        return true;
                    }
                    if ((cs.hasPermission(command.permission) || command.permission
                            .equalsIgnoreCase("") || command.permission
                            .equalsIgnoreCase("iridiumskyblock.")) && command.enabled) {
                        command.execute(cs, args);
                    } else {
                        // No permission
                        cs.sendMessage(StringUtils.color(iridiumSkyblock.getMessages().noPermission
                                .replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)));
                    }
                    return true;
                }
            }
        } else {
            if (cs instanceof Player) {
                Player p = (Player) cs;
                return true;
            }
        }
        cs.sendMessage(StringUtils.color(iridiumSkyblock.getMessages().unknownCommand
                .replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        if (args.length == 1) {
            ArrayList<String> result = new ArrayList<>();
            for (Command command : commands) {
                for (String alias : command.aliases) {
                    if (alias.toLowerCase().startsWith(args[0].toLowerCase()) && (
                            command.enabled && (cs.hasPermission(command.permission)
                                    || command.permission.equalsIgnoreCase("") || command.permission
                                    .equalsIgnoreCase("iridiumskyblock.")))) {
                        result.add(alias);
                    }
                }
            }
            return result;
        }
        for (Command command : commands) {
            if (command.aliases.contains(args[0]) && (command.enabled && (
                    cs.hasPermission(command.permission) || command.permission.equalsIgnoreCase("")
                            || command.permission.equalsIgnoreCase("iridiumskyblock.")))) {
                return command.TabComplete(cs, cmd, s, args);
            }
        }
        return null;
    }
}
