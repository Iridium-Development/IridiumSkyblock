package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandInvite;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.InvitesGUI;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Command which invites a user to the Island.
 */
public class InviteCommand extends Command {

    /**
     * The default constructor.
     */
    public InviteCommand() {
        super(Arrays.asList("invite", "invites"), "Invite a user to your Island", "", true);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Invites a user to the Island.
     *
     * @param sender The CommandSender which executes this command
     * @param args The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = user.getIsland();

        if (island.isPresent()) {
            if (args.length == 1) {
                player.openInventory(new InvitesGUI(island.get()).getInventory());
            } else {
                OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);
                User offlinePlayerUser = IridiumSkyblockAPI.getInstance().getUser(offlinePlayer);
                List<User> islandMembers = IridiumSkyblock.getInstance().getIslandManager().getIslandMembers(island.get());

                if (islandMembers.contains(offlinePlayerUser)) {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyInYourIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                } else if (IridiumSkyblock.getInstance().getIslandManager().getIslandInvite(island.get(), offlinePlayerUser).isPresent()) {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyInvited.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                } else if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, IridiumSkyblock.getInstance().getPermissions().invite)) {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotInviteMember.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                } else {
                    IslandInvite islandInvite = new IslandInvite(island.get(), offlinePlayerUser, user);
                    IridiumSkyblock.getInstance().getDatabaseManager().getIslandInviteList().add(islandInvite);
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
                }
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
