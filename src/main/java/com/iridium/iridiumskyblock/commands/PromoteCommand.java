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

public class PromoteCommand extends Command {
    /**
     * The default constructor.
     */
    public PromoteCommand() {
        super(Collections.singletonList("promote"), "Promote a user", "", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = user.getIsland();
        if (island.isPresent()) {
            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);
            User offlinePlayerUser = IridiumSkyblockAPI.getInstance().getUser(offlinePlayer);
            if (island.get().equals(offlinePlayerUser.getIsland().orElse(null))) {
                IslandRank nextRank = IslandRank.getByLevel(offlinePlayerUser.getIslandRank().getLevel() + 1);
                if (nextRank != null && nextRank.getLevel() < user.getIslandRank().getLevel() && IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblockAPI.getInstance().getUser(player), IridiumSkyblock.getInstance().getPermissions().promote)) {
                    offlinePlayerUser.setIslandRank(nextRank);
                    for (User member : island.get().getMembers()) {
                        Player p = Bukkit.getPlayer(member.getUuid());
                        if (p != null) {
                            if (p.equals(player)) {
                                p.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().promotedPlayer.replace("%player%", offlinePlayerUser.getName()).replace("%rank%", nextRank.name()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                            } else {
                                p.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userPromotedPlayer.replace("%promoter%", player.getName()).replace("%player%", offlinePlayerUser.getName()).replace("%rank%", nextRank.name()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                            }
                        }
                    }
                } else {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotPromoteUser.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            } else {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userNotInYourIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
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
