package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.LogAction;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandInvite;
import com.iridium.iridiumskyblock.database.IslandLog;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Command which un-invites a User from an Island.
 */
public class UnInviteCommand extends Command {

    /**
     * The default constructor.
     */
    public UnInviteCommand() {
        super(Collections.singletonList("uninvite"), "Revoke an invitation to your Island", "", true);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Un-Invites a User from an Island.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("/is uninvite <name>");
            return;
        }
        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        User targetUser = IridiumSkyblock.getInstance().getUserManager().getUser(Bukkit.getServer().getOfflinePlayer(args[1]));

        if (island.isPresent()) {
            Optional<IslandInvite> islandInvite = IridiumSkyblock.getInstance().getIslandManager().getIslandInvite(island.get(), targetUser);
            if (islandInvite.isPresent()) {
                IridiumSkyblock.getInstance().getDatabaseManager().getIslandInviteTableManager().delete(islandInvite.get());
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().inviteRevoked.replace("%player%", targetUser.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                IslandLog islandLog = new IslandLog(island.get(), LogAction.USER_UNINVITED, user, targetUser,0, "");
                IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().addEntry(islandLog);
            } else {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noActiveInvite.replace("%player%", targetUser.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
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
