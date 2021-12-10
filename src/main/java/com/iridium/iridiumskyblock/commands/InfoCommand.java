package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Command which shows infos about an Island.
 */
public class InfoCommand extends Command {

    /**
     * The default constructor.
     */
    public InfoCommand() {
        super(Collections.singletonList("info"), "Show infos about this Island.", "%prefix% &7/is info <player>", "", false, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Shows infos about an Island.
     *
     * @param sender    The CommandSender which executes this command
     * @param arguments The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] arguments) {
        if (arguments.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().mustBeAPlayer.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                return false;
            }

            Player player = (Player) sender;
            User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
            Optional<Island> userIsland = user.getIsland();
            if (!userIsland.isPresent()) {
                sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                return false;
            }

            sendInfo(sender, userIsland.get(), user);
            return true;
        }

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(arguments[1]);
        User targetUser = IridiumSkyblock.getInstance().getUserManager().getUser(targetPlayer);
        Optional<Island> targetIsland = targetUser.getIsland();
        if (!targetIsland.isPresent()) {
            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userNoIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        sendInfo(sender, targetIsland.get(), targetUser);
        return true;
    }

    /**
     * Sends infos about the specified island to the sender.
     *
     * @param sender        The CommandSender who invoked the command
     * @param island        The Island whose data was requested
     * @param requestedUser The User whose data was requested
     */
    private void sendInfo(CommandSender sender, Island island, User requestedUser) {
        String members = island.getMembers().stream()
                .filter(user -> user != island.getOwner())
                .map(User::getName)
                .collect(Collectors.joining(", "));

        if (members.isEmpty()) {
            members = IridiumSkyblock.getInstance().getMessages().none;
        }


        sender.sendMessage(StringUtils.getCenteredMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().infoTitle
                .replace("%player%", requestedUser.getName())
                .replace("%island_name%", island.getName())
                .replace("%owner%", island.getOwner().getName())
                .replace("%members%", members)
                .replace("%level%", String.valueOf(island.getLevel()))
                .replace("%value%", String.valueOf(island.getValue()))
                .replace("%visitable%", island.isVisitable() ? IridiumSkyblock.getInstance().getMessages().visitable : IridiumSkyblock.getInstance().getMessages().notVisitable)
        ), StringUtils.color(IridiumSkyblock.getInstance().getMessages().infoFiller)));

        for (String infoLine : IridiumSkyblock.getInstance().getMessages().infoCommand) {
            sender.sendMessage(StringUtils.color(infoLine
                    .replace("%player%", requestedUser.getName())
                    .replace("%island_name%", island.getName())
                    .replace("%owner%", island.getOwner().getName())
                    .replace("%members%", members)
                    .replace("%level%", String.valueOf(island.getLevel()))
                    .replace("%value%", String.valueOf(island.getValue()))
                    .replace("%visitable%", island.isVisitable() ? IridiumSkyblock.getInstance().getMessages().visitable : IridiumSkyblock.getInstance().getMessages().notVisitable)
            ));
        }
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
        return PlayerUtils.getOnlinePlayerNames();
    }

}
