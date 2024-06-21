package com.iridium.iridiumskyblock.commands.supersecretcommands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.IridiumTeams;
import com.iridium.iridiumteams.commands.Command;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class IsMyComputerOnFire extends Command<Island, User> {
    public IsMyComputerOnFire() {
        super(Collections.singletonList("my"), "Super Secret Command shhh", "", "", 0);
    }

    @Override
    public boolean execute(CommandSender sender, String[] arguments, IridiumTeams<Island, User> iridiumTeams) {
        if (arguments.length != 3 || !arguments[0].equalsIgnoreCase("computer") || !arguments[1].equalsIgnoreCase("on") || !arguments[2].equalsIgnoreCase("fire")) {
            sender.sendMessage(StringUtils.color(iridiumTeams.getMessages().unknownCommand
                    .replace("%prefix%", iridiumTeams.getConfiguration().prefix)
            ));
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("No.");
            return true;
        } else {
            return this.execute(iridiumTeams.getUserManager().getUser((OfflinePlayer) sender), arguments, iridiumTeams);
        }
    }

    @Override
    public boolean execute(User user, String[] arguments, IridiumTeams<Island, User> iridiumTeams) {
        IridiumSkyblock.getInstance().getNms().sendTitle(user.getPlayer(), StringUtils.color("&9&lNo."), "", 20, 40, 20);
        return true;
    }

    @Override
    public boolean isSuperSecretCommand() {
        return true;
    }
}
