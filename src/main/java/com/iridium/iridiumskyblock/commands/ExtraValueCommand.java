package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.*;

public class ExtraValueCommand extends Command {

    /**
     * The default constructor.
     */
    public ExtraValueCommand() {
        super(Collections.singletonList("extravalue"), "Set extra values of island", "%prefix% &7/is extravalue <add/remove/set> <player> <amount>", "iridiumskyblock.extravalue", false, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Gives extra values to specific player's island
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(StringUtils.color(syntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        Player player = Bukkit.getPlayer(args[2]);
        if (player != null) {
            User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
            Optional<Island> island = user.getIsland();
            if (island.isPresent()) {
                try {
                    double amount = Double.parseDouble(args[3]);
                    switch (args[1].toUpperCase(Locale.ENGLISH)) {
                        case "ADD":
                            island.get().setExtraValue(island.get().getExtraValue() + amount);
                            break;
                        case "REMOVE":
                            island.get().setExtraValue(island.get().getExtraValue() - amount);
                            break;
                        case "SET":
                            island.get().setExtraValue(amount);
                            break;
                        default:
                            sender.sendMessage(StringUtils.color(syntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                            return false;

                    }
                    sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().setExtraValue.replace("%player%", player.getName()).replace("%amount%", String.valueOf(island.get().getExtraValue())).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    return true;
                } catch (NumberFormatException e) {
                    sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notANumber.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            } else {
                sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userNoIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        } else {
            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notAPlayer.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
        return false;
    }

    /**
     * Handles tab-completion for this command.
     *
     * @param commandSender The CommandSender which tries to tab-complete
     * @param command       The command
     * @param label         The label of the command
     * @param args          The arguments already provided by the sender
     * @return The list of tab completions for this command
     */
    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 3) {
            return PlayerUtils.getOnlinePlayerNames();
        }

        if (args.length == 2) {
            return Arrays.asList("add", "remove", "set");
        }

        return Collections.emptyList();
    }
    
}
