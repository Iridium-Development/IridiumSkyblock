package com.iridium.iridiumskyblock.commands.booster;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.commands.Command;
import com.iridium.iridiumskyblock.database.Island;
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

public class RemoveCommand extends Command {

    /**
     * The default constructor.
     */
    public RemoveCommand() {
        super(Collections.singletonList("remove"), "Removes the player's booster.", "%prefix% &7/is booster remove <player> <type>", "iridiumskyblock.booster.remove", false, Duration.ZERO);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 4) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
            User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
            Optional<Island> island = user.getIsland();
            if (island.isPresent()) {
                String boosterName = args[3];
                if (IridiumSkyblock.getInstance().getBoosterList().containsKey(boosterName)) {
                    IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island.get(), boosterName).setTime(LocalDateTime.now());
                    sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().removeBooster
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                            .replace("%booster%", boosterName)
                            .replace("%player%", args[2])));
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
