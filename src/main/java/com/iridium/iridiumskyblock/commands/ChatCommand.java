package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.api.UserChatToggleEvent;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command which allows users to chat with their Island members.
 */
public class ChatCommand extends Command {

    /**
     * The default constructor.
     */
    public ChatCommand() {
        super(Collections.singletonList("chat"), "Chat with online Island members", "%prefix% &7/is chat <message>", "", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Chats with the sender's island members {@link Island}.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Optional<Island> island = IridiumSkyblock.getInstance().getUserManager().getUser(player).getIsland();
        if (island.isEmpty()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (args.length > 1) {
            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            for (User user : island.get().getMembers()) {
                Player recipient = Bukkit.getPlayer(user.getUuid());
                if (recipient != null) {
                    recipient.sendMessage(StringUtils.color(
                            StringUtils.processMultiplePlaceholders(IridiumSkyblock.getInstance().getMessages().islandMemberChat, new PlaceholderBuilder().applyIslandPlaceholders(island.get()).build())
                                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                                    .replace("%player%", player.getName())
                                    .replace("%message%", message))
                    );
                }
            }
        } else {
            User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
            UserChatToggleEvent userChatToggleEvent = new UserChatToggleEvent(user, !user.isIslandChat());
            Bukkit.getPluginManager().callEvent(userChatToggleEvent);
            if (userChatToggleEvent.isCancelled()) return false;

            user.setIslandChat(!user.isIslandChat());
            player.sendMessage(StringUtils.color((user.isIslandChat() ? IridiumSkyblock.getInstance().getMessages().islandChatEnabled : IridiumSkyblock.getInstance().getMessages().islandChatDisabled)
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
            );
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
