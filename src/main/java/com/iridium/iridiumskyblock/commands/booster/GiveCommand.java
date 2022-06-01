package com.iridium.iridiumskyblock.commands.booster;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.Booster;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.commands.Command;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GiveCommand extends Command {

    /**
     * The default constructor.
     */
    public GiveCommand() {
        super(Collections.singletonList("give"), "Give players a booster", "%prefix% &7/is booster give <player> <type> <seconds>", "iridiumskyblock.booster.give", false, Duration.ZERO);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 5) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
            User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
            Optional<Island> island = user.getIsland();
            if (island.isPresent()) {
                String boosterName = args[3];
                if (IridiumSkyblock.getInstance().getBoosterList().containsKey(boosterName)) {
                    Booster booster = IridiumSkyblock.getInstance().getBoosterList().get(boosterName);
                    IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island.get(), boosterName);
                    islandBooster.setTime(LocalDateTime.now().plusSeconds(Long.parseLong(args[4]) + (islandBooster.isActive() && booster.stackable ? islandBooster.getRemainingTime() : 0)));
                    sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().giveBooster)
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                            .replace("%booster%", boosterName)
                            .replace("%player%", args[2]));

                    island.get().getMembers()
                            .stream()
                            .filter(member -> member.getPlayer() != null)
                            .forEach(member -> member.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().receiveBooster
                                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                                    .replace("%booster%", boosterName))));
                    return true;
                } else {
                    sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().unknownBooster.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            } else {
                sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userNoIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        } else {
            sender.sendMessage(StringUtils.color(syntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 3) {
            return PlayerUtils.getOnlinePlayerNames();
        }

        if (args.length == 4) {
            return new ArrayList<>(IridiumSkyblock.getInstance().getBoosterList().keySet());
        }

        return Collections.emptyList();
    }
}
