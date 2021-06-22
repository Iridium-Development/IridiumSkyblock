package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.LogAction;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.api.UserDemoteEvent;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandLog;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Command which demotes a user in the Island rank system.
 */
public class DemoteCommand extends Command {

    /**
     * The default constructor.
     */
    public DemoteCommand() {
        super(Collections.singletonList("demote"), "Demote a user", "%prefix% &7/is demote <name>", "", true);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Demotes a user in the Island rank system.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(StringUtils.color(syntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }
        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();

        if (island.isPresent()) {
            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);
            User offlinePlayerUser = IridiumSkyblock.getInstance().getUserManager().getUser(offlinePlayer);

            if (island.get().equals(offlinePlayerUser.getIsland().orElse(null))) {
                IslandRank nextRank = IslandRank.getByLevel(offlinePlayerUser.getIslandRank().getLevel() - 1);
                if (nextRank != null && offlinePlayerUser.getIslandRank().getLevel() < user.getIslandRank().getLevel() && IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(player), PermissionType.DEMOTE)) {
                    if (nextRank.equals(IslandRank.VISITOR)) {
                        String command = IridiumSkyblock.getInstance().getCommands().kickCommand.aliases.get(0);
                        Bukkit.getServer().dispatchCommand(player, "is " + command + " " + args[1]);
                    } else {
                        UserDemoteEvent userDemoteEvent = new UserDemoteEvent(island.get(), user, nextRank);
                        Bukkit.getPluginManager().callEvent(userDemoteEvent);
                        if (userDemoteEvent.isCancelled()) return;

                        offlinePlayerUser.setIslandRank(nextRank);
                        for (User member : island.get().getMembers()) {
                            Player p = Bukkit.getPlayer(member.getUuid());
                            if (p != null) {
                                if (p.equals(player)) {
                                    p.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().demotedPlayer.replace("%player%", offlinePlayerUser.getName()).replace("%rank%", nextRank.name()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                                } else {
                                    p.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userDemotedPlayer.replace("%promoter%", player.getName()).replace("%player%", offlinePlayerUser.getName()).replace("%rank%", nextRank.name()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                                }
                            }
                        }
                        IslandLog islandLog = new IslandLog(island.get(), LogAction.USER_DEMOTED, user, offlinePlayerUser, 0, nextRank.name());
                        IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().addEntry(islandLog);
                    }
                } else {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotDemoteUser.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            } else {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userNotInYourIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
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
        return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).filter(s -> s.contains(args[1])).collect(Collectors.toList());
    }

}
