package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.IslandTopGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Command which opens the {@link IslandTopGUI}.
 */
public class ChatCommand extends Command {
    private final IridiumSkyblockAPI iridiumSkyblockAPI = IridiumSkyblockAPI.getInstance();
    private final IridiumSkyblock iridiumSkyblock = IridiumSkyblock.getInstance();

    /**
     * The default constructor.
     */
    public ChatCommand() {
        super(Collections.singletonList("chat"), "Chat with online Island members", "", true);
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
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        String senderName = player.getName();
        String message = String.join(" ", args);

        Optional<Island> island = iridiumSkyblockAPI.getUser(player).getIsland();

        if (!island.isPresent()) return;

        for (User user : island.get().getMembers()) {
            Player recipient = Bukkit.getPlayer(user.getUuid());

            if (recipient != null) {
                recipient.sendMessage(iridiumSkyblock.getMessages().islandMemberChat
                    .replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)
                    .replace("%player%", senderName)
                    .replace("%message%", message));
            }
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
        // We currently don't want to tab-completion here
        // Return a new List so it isn't a list of online players
        return Collections.emptyList();
    }

}
