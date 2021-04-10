package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.VisitGUI;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * Command which opens visitable islands or visits an island.
 */
public class VisitCommand extends Command {

    /**
     * The default constructor.
     */
    public VisitCommand() {
        super(Collections.singletonList("visit"), "Visit another players Island", "", true);
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
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;

        if (args.length != 2) {
            p.openInventory(new VisitGUI(0).getInventory());
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
        User user = IridiumSkyblockAPI.getInstance().getUser(player);

        if (user.getIsland().isPresent()) {
            if ((user.getIsland()).get().isVisitable() || p.hasPermission("iridiumskyblock.visitbypass")) {
                IridiumSkyblock.getInstance().getIslandManager().teleportHome(p, user.getIsland().get());
            } else {
                p.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandIsPrivate.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        } else {
            p.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
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
        return null;
    }

}
