package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.LogAction;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandLog;
import com.iridium.iridiumskyblock.database.IslandTrusted;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnTrustCommand extends Command {

    /**
     * The default constructor.
     */
    public UnTrustCommand() {
        super(Collections.singletonList("untrust"), "Revoke a trusted user from your Island", "%prefix% &7/is untrust <player>", "", true, Duration.ZERO);
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
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(StringUtils.color(syntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        User targetUser = IridiumSkyblock.getInstance().getUserManager().getUser(Bukkit.getServer().getOfflinePlayer(args[1]));
        if (island.isEmpty()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(player), PermissionType.TRUST)) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotManageTrusts.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        Optional<IslandTrusted> islandTrusted = IridiumSkyblock.getInstance().getIslandManager().getIslandTrusted(island.get(), targetUser);
        if (islandTrusted.isEmpty()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notTrusted.replace("%player%", targetUser.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager().delete(islandTrusted.get());
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().trustRevoked.replace("%player%", targetUser.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        IslandLog islandLog = new IslandLog(island.get(), LogAction.USER_UNTRUSTED, user, targetUser, 0, "");
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().addEntry(islandLog);
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
