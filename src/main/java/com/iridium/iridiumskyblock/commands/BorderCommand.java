package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.Color;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.BorderGUI;
import com.iridium.iridiumteams.IridiumTeams;
import com.iridium.iridiumteams.commands.Command;
import com.iridium.iridiumteams.database.IridiumUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Command which changes the Island's Border Color.
 */
public class BorderCommand extends Command<Island, User> {

    /**
     * The default constructor.
     */
    public BorderCommand() {
        super(Collections.singletonList("border"), "Change the Island Border", "%prefix% &7/is border <color>", "", 0);
    }

    @Override
    public boolean execute(User user, Island island, String[] args, IridiumTeams<Island, User> iridiumTeams) {
        Player player = user.getPlayer();
        if (!IridiumSkyblock.getInstance().getIslandManager().getTeamPermission(island, IridiumSkyblock.getInstance().getUserManager().getUser(player), "border")) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotManageBorder.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (args.length == 0) {
            player.openInventory(new BorderGUI(player).getInventory());
            return false;
        }

        Color color = Color.getColor(args[0]);
        if (color == null) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notAColor.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (!IridiumSkyblock.getInstance().getConfiguration().enabledBorders.getOrDefault(color, true)) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().borderColorDisabled.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        island.setColor(color);
        IridiumSkyblock.getInstance().getTeamManager().getTeamMembers(island).stream()
                .map(IridiumUser::getPlayer)
                .filter(Objects::nonNull)
                .forEach(islandPlayer ->
                        islandPlayer.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandBorderChanged
                                .replace("%player%", player.getName())
                                .replace("%color%", color.toString())
                                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                        ))
                );

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args, IridiumTeams<Island, User> iridiumTeams) {
        return Arrays.stream(Color.values())
                .map(Color::name)
                .collect(Collectors.toList());
    }

}
