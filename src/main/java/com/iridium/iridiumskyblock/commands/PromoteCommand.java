package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.LogAction;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.api.UserPromoteEvent;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandLog;
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

/**
 * Command which promotes a user in the Island rank system.
 */
public class PromoteCommand extends Command {

    /**
     * The default constructor.
     */
    public PromoteCommand() {
        super(Collections.singletonList("promote"), "Promote a user", "%prefix% &7/is promote <player>", "", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Promotes a user in the Island rank system.
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
        if (!island.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        OfflinePlayer targetPlayer = Bukkit.getServer().getOfflinePlayer(args[1]);
        User targetUser = IridiumSkyblock.getInstance().getUserManager().getUser(targetPlayer);

        if (!island.get().equals(targetUser.getIsland().orElse(null))) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userNotInYourIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        IslandRank nextRank = IslandRank.getByLevel(targetUser.getIslandRank().getLevel() + 1);
        if (nextRank == null || nextRank.getLevel() >= user.getIslandRank().getLevel() || !IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(player), PermissionType.PROMOTE)) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotPromoteUser.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        UserPromoteEvent userPromoteEvent = new UserPromoteEvent(island.get(), user, nextRank);
        Bukkit.getPluginManager().callEvent(userPromoteEvent);
        if (userPromoteEvent.isCancelled()) return false;

        targetUser.setIslandRank(nextRank);

        for (User member : island.get().getMembers()) {
            Player islandMember = Bukkit.getPlayer(member.getUuid());
            if (islandMember != null) {
                if (islandMember.equals(player)) {
                    islandMember.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().promotedPlayer.replace("%player%", targetUser.getName()).replace("%rank%", nextRank.getDisplayName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                } else {
                    islandMember.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userPromotedPlayer.replace("%promoter%", player.getName()).replace("%player%", targetUser.getName()).replace("%rank%", nextRank.getDisplayName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            }
        }

        IslandLog islandLog = new IslandLog(island.get(), LogAction.USER_PROMOTED, user, targetUser, 0, nextRank.getDisplayName());
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
