package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.VisitGUI;
import com.iridium.iridiumskyblock.utils.PlayerUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command which opens visitable islands or visits an island.
 */
public class VisitCommand extends Command {

    /**
     * The default constructor.
     */
    public VisitCommand() {
        super(Collections.singletonList("visit"), "Visit another players Island", "%prefix% &7/is visit <player>", "", true, Duration.ZERO);
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
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        if (args.length != 2) {
            player.sendMessage("Le GUI est temporairement désactivé");
            //player.openInventory(new VisitGUI(1, user).getInventory());
            return true;
        }

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
        User targetUser = IridiumSkyblock.getInstance().getUserManager().getUser(targetPlayer);
        if (targetUser.getIsland().isEmpty()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIslandFound.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (!targetUser.getIsland().get().isVisitable() && !player.hasPermission("iridiumskyblock.visitbypass")) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandIsPrivate.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        IridiumSkyblock.getInstance().getIslandManager().teleportHome(player, user, targetUser.getIsland().get(), IridiumSkyblock.getInstance().getConfiguration().teleportDelay);
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
