package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.LogAction;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandLog;
import com.iridium.iridiumskyblock.database.IslandTrusted;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UnTrustCommand extends Command {

    /**
     * The default constructor.
     */
    public UnTrustCommand() {
        super(Collections.singletonList("untrust"), "Revoke a trusted user from your Island", "", true);
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
            sender.sendMessage("/is untrust <name>");
            return;
        }
        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        User targetUser = IridiumSkyblock.getInstance().getUserManager().getUser(Bukkit.getServer().getOfflinePlayer(args[1]));

        if (island.isPresent()) {
            if (IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(player), IridiumSkyblock.getInstance().getPermissions().trust)) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotManageTrusts.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                return;
            }
            Optional<IslandTrusted> islandTrusted =
                    IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager().getEntries(island.get()).stream().filter(islandTrusted1 ->
                            islandTrusted1.getUser().getUuid().equals(targetUser.getUuid())
                    ).findFirst();
            if (islandTrusted.isPresent()) {
                IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager().delete(islandTrusted.get());
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().trustRevoked.replace("%player%", targetUser.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                IslandLog islandLog = new IslandLog(island.get(), LogAction.USER_UNTRUSTED, user, targetUser, 0, 0, 0, "");
                IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().addEntry(islandLog);
            } else {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().trustDoesntExist.replace("%player%", targetUser.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
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
