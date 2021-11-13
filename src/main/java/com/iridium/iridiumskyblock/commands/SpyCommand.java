package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

public class SpyCommand extends Command {

    /**
     * The default constructor.
     */
    public SpyCommand() {
        super(Collections.singletonList("spy"), "Read the chats of other Islands", "iridiumskyblock.spy", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     *
     * Allows players to read the Island chat of other Islands.
     *
     * @param sender    The CommandSender which executes this command
     * @param arguments The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] arguments) {
        Player player = (Player) sender;
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        boolean newSpyStatus = !user.isIslandChatSpying();

        user.setIslandChatSpying(newSpyStatus);
        if (newSpyStatus) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandChatSpyEnabled.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandChatSpyDisabled.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }

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
        // We currently don't want to tab-completion here
        // Return a new List, so it isn't a list of online players
        return Collections.emptyList();
    }

}
