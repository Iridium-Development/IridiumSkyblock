package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TransferCommand extends Command {
    /**
     * The default constructor.
     */
    public TransferCommand() {
        super(Collections.singletonList("transfer"), "Transfer Island ownership to another player", "", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getConfiguration().prefix + " /is transfer <player>"));
            return;
        }
        Player player = (Player) sender;
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = user.getIsland();
        if (island.isPresent()) {
            Optional<User> islandOwner = island.get().getOwner();
            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);
            User offlinePlayerUser = IridiumSkyblockAPI.getInstance().getUser(offlinePlayer);
            if (user.getIslandRank().equals(IslandRank.OWNER) || user.isBypass()) {
                if (island.get().equals(offlinePlayerUser.getIsland().orElse(null))) {
                    if (islandOwner.isPresent() && !islandOwner.get().getUuid().equals(offlinePlayer.getUniqueId())) {
                        islandOwner.ifPresent(owner -> owner.setIslandRank(IslandRank.CO_OWNER));
                        offlinePlayerUser.setIslandRank(IslandRank.OWNER);
                        for (User member : island.get().getMembers()) {
                            Player p = Bukkit.getPlayer(member.getUuid());
                            if (p != null) {
                                p.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().transferredOwnership.replace("%oldowner%", user.getName()).replace("%newowner%", offlinePlayerUser.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                            }
                        }
                    } else {
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotTransferYourself.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    }
                } else {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userNotInYourIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            } else {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotTransferOwnership.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return null;
    }
}
