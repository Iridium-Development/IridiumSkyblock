package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.VisitGUI;
import com.iridium.iridiumteams.IridiumTeams;
import com.iridium.iridiumteams.commands.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VisitCommand extends Command<Island, User> {
    public VisitCommand() {
        super(Collections.singletonList("visit"), "Visit other islands", "%prefix% &7/is visit", "", 0);
    }

    @Override
    public boolean execute(User user, String[] args, IridiumTeams<Island, User> iridiumTeams) {
        Player player = user.getPlayer();
        if (args.length == 0) {
            player.openInventory(new VisitGUI(player, iridiumTeams).getInventory());
            return false;
        }
        Optional<Island> island = IridiumSkyblock.getInstance().getTeamManager().getTeamViaNameOrPlayer(args[0]);
        if (!island.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teamDoesntExistByName
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));
            return false;
        }
        if (!IridiumSkyblock.getInstance().getIslandManager().canVisit(player, island.get())) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotVisit
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
            ));
            return false;
        }
        player.teleport(island.get().getHome());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args, IridiumTeams<Island, User> iridiumTeams) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }
}
