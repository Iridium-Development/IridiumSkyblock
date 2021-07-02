package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.LogAction;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.database.*;
import com.iridium.iridiumskyblock.gui.InvitesGUI;
import java.time.Duration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Command which invites a user to the Island.
 */
public class InviteCommand extends Command {

    /**
     * The default constructor.
     */
    public InviteCommand() {
        super(Arrays.asList("invite", "invites"), "Invite a user to your Island", "", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Invites a user to the Island.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();

        if (island.isPresent()) {
            if (args.length == 1) {
                player.openInventory(new InvitesGUI(island.get()).getInventory());
                return true;
            } else {
                OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);
                User offlinePlayerUser = IridiumSkyblock.getInstance().getUserManager().getUser(offlinePlayer);
                List<User> islandMembers = IridiumSkyblock.getInstance().getIslandManager().getIslandMembers(island.get());
                IslandUpgrade islandUpgrade = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(island.get(), "member");
                int memberLimit = IridiumSkyblock.getInstance().getUpgrades().memberUpgrade.upgrades.get(islandUpgrade.getLevel()).amount;

                if (islandMembers.contains(offlinePlayerUser)) {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyInYourIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                } else if (IridiumSkyblock.getInstance().getIslandManager().getIslandInvite(island.get(), offlinePlayerUser).isPresent()) {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyInvited.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                } else if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, PermissionType.INVITE)) {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotInviteMember.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                } else if (islandMembers.size() >= memberLimit) {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandMemberLimitReached.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                } else {
                    IslandInvite islandInvite = new IslandInvite(island.get(), offlinePlayerUser, user);
                    IridiumSkyblock.getInstance().getDatabaseManager().getIslandInviteTableManager().addEntry(islandInvite);
                    IslandLog islandLog = new IslandLog(island.get(), LogAction.USER_INVITED, user, offlinePlayerUser, 0, "");
                    IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().addEntry(islandLog);
                    String playerName = offlinePlayer.getName() != null ? offlinePlayerUser.getName() : args[1];
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().invitedPlayer.replace("%player%", playerName).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));

                    // Send a message to all other members
                    for (User member : islandMembers) {
                        Player islandMember = Bukkit.getPlayer(member.getUuid());
                        if (islandMember == null || islandMember.equals(player)) continue;
                        islandMember.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userInvitedPlayer.replace("%inviter%", player.getName()).replace("%player%", playerName).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    }

                    // Send a message to the user if he is online
                    if (offlinePlayer instanceof Player) {
                        ((Player) offlinePlayer).sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youHaveBeenInvited.replace("%inviter%", player.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    }

                    return true;
                }
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
        return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).filter(s -> s.contains(args[1])).collect(Collectors.toList());
    }

}
