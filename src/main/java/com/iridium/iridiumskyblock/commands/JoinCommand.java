package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandInvite;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Command which creates a new island for an user.
 */
public class JoinCommand extends Command {

    /**
     * The default constructor.
     */
    public JoinCommand() {
        super(Collections.singletonList("join"), "Join an Island", "", true);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Tries to create a new island for the user.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        if (user.getIsland().isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);
            User offlinePlayerUser = IridiumSkyblockAPI.getInstance().getUser(offlinePlayer);
            Optional<Island> island = offlinePlayerUser.getIsland();
            if (island.isPresent()) {
                Optional<IslandInvite> islandInvite = IridiumSkyblock.getInstance().getIslandManager().getIslandInvite(island.get(), user);
                if (islandInvite.isPresent() || user.isBypass()) {
                    for (User member : island.get().getMembers()) {
                        Player pl = Bukkit.getPlayer(member.getName());
                        if (pl != null) {
                            pl.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().playerJoinedYourIsland.replace("%player%", player.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                        }
                    }
                    user.setIsland(island.get());
                    user.setIslandRank(IslandRank.MEMBER);
                    islandInvite.ifPresent(invite -> IridiumSkyblock.getInstance().getDatabaseManager().deleteInvite(invite));
                    IridiumSkyblock.getInstance().getIslandManager().teleportHome(player, island.get());
                } else {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noInvite.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            } else {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userNoIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
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
