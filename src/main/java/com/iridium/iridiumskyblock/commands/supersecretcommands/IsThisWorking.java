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

public class IsThisWorking extends Command<Island, User> {
    public IsThisWorking() {
        super(Collections.singletonList("this"), "Super Secret Command shhh", "", "", 0);
    }

    @Override
    public boolean execute(CommandSender sender, String[] arguments, IridiumTeams<Island, User> iridiumTeams) {
        if(arguments.length != 1 || !arguments[0].equalsIgnoreCase("working")){
            sender.sendMessage(StringUtils.color(iridiumTeams.getMessages().unknownCommand
                    .replace("%prefix%", iridiumTeams.getConfiguration().prefix)
            ));
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("Yes.");
            return true;
        } else {
            return this.execute(iridiumTeams.getUserManager().getUser((OfflinePlayer)sender), arguments, iridiumTeams);
        }
    }

    @Override
    public boolean execute(User user, String[] arguments, IridiumTeams<Island, User> iridiumTeams) {
        IridiumSkyblock.getInstance().getNms().sendTitle(user.getPlayer(), StringUtils.color("&9&lYes."), "", 20, 40, 20);
        return true;
    }

    @Override
    public boolean isSuperSecretCommand() {
        return true;
    }
}
