package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.LogAction;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.api.UserKickEvent;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandLog;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Command which kicks a user from an Island.
 */
public class KickCommand extends Command {

    /**
     * The default constructor.
     */
    public KickCommand() {
        super(Collections.singletonList("kick"), "Kick a player", "%prefix% &7/is kick <name>", "",true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Kicks a user from an Island.
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

        if (island.isPresent()) {
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
            User targetUser = IridiumSkyblock.getInstance().getUserManager().getUser(targetPlayer);

            if (island.get().equals(targetUser.getIsland().orElse(null))) {
                if (targetUser.getIslandRank().getLevel() >= user.getIslandRank().getLevel() || !IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, PermissionType.KICK)) {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotKickUser.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                } else {
                    UserKickEvent userKickEvent = new UserKickEvent(island.get(), targetUser, user);
                    Bukkit.getPluginManager().callEvent(userKickEvent);
                    if (userKickEvent.isCancelled()) return false;

                    if (targetPlayer instanceof Player) {
                        ((Player) targetPlayer).sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youHaveBeenKicked.replace("%player%", player.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                        PlayerUtils.teleportSpawn((Player) targetPlayer);
                    }

                    targetUser.setIsland(null);

                    // Send a message to all other members
                    for (User member : island.get().getMembers()) {
                        Player p = Bukkit.getPlayer(member.getUuid());
                        if (p != null) {
                            if (!p.equals(player)) {
                                p.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().kickedPlayer.replace("%kicker%", player.getName()).replace("%player%", targetUser.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                            } else {
                                p.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youKickedPlayer.replace("%player%", targetUser.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                            }
                        }
                    }

                    IslandLog islandLog = new IslandLog(island.get(), LogAction.USER_KICKED, user, targetUser, 0, "");
                    IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().addEntry(islandLog);
                    return true;
                }
            } else {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userNotInYourIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }

        return false;
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
        return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).filter(s -> s.toLowerCase().contains(args[1].toLowerCase())).collect(Collectors.toList());
    }

}
