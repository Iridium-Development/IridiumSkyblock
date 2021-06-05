package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.commands.subcommands.BankGive;
import com.iridium.iridiumskyblock.commands.subcommands.BankRemove;
import com.iridium.iridiumskyblock.commands.subcommands.BankSet;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.BankGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Command which opens the Island bank GUI.
 */
public class BankCommand extends Command {

    public BankGive bankGive;
    public BankSet bankSet;
    public BankRemove bankRemove;

    /**
     * The default constructor.
     */
    public BankCommand() {
        super(Collections.singletonList("bank"), "Open your Island bank", "", false);
        this.bankGive = new BankGive();
        this.bankSet = new BankSet();
        this.bankRemove = new BankRemove();
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Opens the Island bank GUI.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            for (Command command : Arrays.asList(bankGive, bankSet, bankRemove)) {
                if (command.aliases.contains(args[1])) {
                    // Check if this command is only for players
                    if (command.onlyForPlayers && !(sender instanceof Player)) {
                        // Must be a player
                        sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().mustBeAPlayer
                                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                        return;
                    }
                    // Check permissions
                    if (!((sender.hasPermission(command.permission) || command.permission
                            .equalsIgnoreCase("") || command.permission
                            .equalsIgnoreCase("iridiumskyblock.")))) {
                        // No permissions
                        sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noPermission
                                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                        return;
                    }
                    command.execute(sender, args);
                    return;
                }
            }
        } else if (sender instanceof Player) {
            Player player = (Player) sender;
            User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
            Optional<Island> island = user.getIsland();

            if (island.isPresent()) {
                player.openInventory(new BankGUI(island.get()).getInventory());
            } else {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        } else {
            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().mustBeAPlayer
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
    }

    /**
     * Handles tab-completion for this command.
     *
     * @param commandSender The CommandSender which tries to tab-complete
     * @param cmd           The command
     * @param label         The label of the command
     * @param args          The arguments already provided by the sender
     * @return The list of tab completions for this command
     */
    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command cmd, String label, String[] args) {
        // We currently don't want to tab-completion here
        // Return a new List so it isn't a list of online players
        if (args.length == 2) {
            return Stream.of(bankGive, bankSet, bankRemove).map(command -> command.aliases.get(0)).collect(Collectors.toList());
        }
        // Let the sub-command handle the tab completion
        for (Command command : Arrays.asList(bankGive, bankSet, bankRemove)) {
            if (command.aliases.contains(args[1]) && (
                    commandSender.hasPermission(command.permission) || command.permission.equalsIgnoreCase("")
                            || command.permission.equalsIgnoreCase("iridiumskyblock."))) {
                return command.onTabComplete(commandSender, cmd, label, args);
            }
        }
        return Collections.emptyList();
    }

}
