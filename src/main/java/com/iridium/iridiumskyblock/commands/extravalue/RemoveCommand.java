package com.iridium.iridiumskyblock.commands.extravalue;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.commands.Command;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RemoveCommand extends Command {

    /**
     * The default constructor.
     */
    public RemoveCommand() {
        super(Collections.singletonList("remove"), "Remove value from the extra value of a player", "%prefix% &7/is extravalue remove <player> <amount>", "iridiumskyblock.extravalue", false, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     *
     * @param sender    The CommandSender which executes this command
     * @param args      The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(StringUtils.color(syntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        User user = IridiumSkyblock.getInstance().getUserManager().getUser(args[2]);
        Optional<Island> optionalIsland = user.getIsland();
        if (!optionalIsland.isPresent()) {
            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userNoIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[3]);
        } catch (NumberFormatException exception) {
            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notANumber.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        Island island = optionalIsland.get();
        island.setExtraValue(island.getExtraValue() - amount);
        sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().extraValueSet
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                .replace("%player%", user.getName())
                .replace("%amount%", String.valueOf(island.getExtraValue()))));
        return true;
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

        return Collections.emptyList();
    }

}
