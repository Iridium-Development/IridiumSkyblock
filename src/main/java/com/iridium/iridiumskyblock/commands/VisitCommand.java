package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.VisitGUI;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Command which opens visitable islands or visits an island.
 */
public class VisitCommand extends Command {

    /**
     * The default constructor.
     */
    public VisitCommand() {
        super(Collections.singletonList("visit"), "Visit another players Island", "", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Opens visitable islands or visits an island.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length != 2) {
            player.openInventory(new VisitGUI(1).getInventory());
            return true;
        }

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
        User targetUser = IridiumSkyblock.getInstance().getUserManager().getUser(targetPlayer);
        if (!targetUser.getIsland().isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIslandFound.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (!targetUser.getIsland().get().isVisitable() && !player.hasPermission("iridiumskyblock.visitbypass")) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandIsPrivate.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        IridiumSkyblock.getInstance().getIslandManager().teleportHome(player, targetUser.getIsland().get(), IridiumSkyblock.getInstance().getConfiguration().teleportDelay);
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
        return PlayerUtils.getOnlinePlayerNames();
    }

}
