package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Command which transfers Island ownership.
 */
public class TransferCommand extends Command {

    /**
     * The default constructor.
     */
    public TransferCommand() {
        super(Collections.singletonList("transfer"), "Transfer Island ownership to another player", "%prefix% &7/is transfer <name>", "", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Transfers Island ownership.
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

        User islandOwner = island.get().getOwner();
        OfflinePlayer targetPlayer = Bukkit.getServer().getOfflinePlayer(args[1]);
        User targetUser = IridiumSkyblock.getInstance().getUserManager().getUser(targetPlayer);

        if (!user.getIslandRank().equals(IslandRank.OWNER) && !user.isBypass()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotTransferOwnership.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (!island.get().equals(targetUser.getIsland().orElse(null))) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userNotInYourIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (islandOwner.getUuid().equals(targetPlayer.getUniqueId())) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotTransferYourself.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        islandOwner.setIslandRank(IslandRank.CO_OWNER);
        targetUser.setIslandRank(IslandRank.OWNER);
        for (User member : island.get().getMembers()) {
            Player islandMember = Bukkit.getPlayer(member.getUuid());
            if (islandMember == null) continue;
            islandMember.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().transferredOwnership
                    .replace("%oldowner%", user.getName())
                    .replace("%newowner%", targetUser.getName())
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));
        }

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
        return Bukkit.getOnlinePlayers().stream()
            .map(Player::getName)
            .filter(playerName -> playerName.toLowerCase().contains(args[1].toLowerCase()))
            .collect(Collectors.toList());
    }

}
