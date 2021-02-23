package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class AboutCommand extends Command {
    /**
     * The default constructor.
     */
    public AboutCommand() {
        super(Collections.singletonList("about"), "Displays plugin info", "", false);
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        sender.sendMessage(StringUtils.color("&7Plugin Name: &bIridiumSkyblock"));
        sender.sendMessage(StringUtils.color("&7Plugin Version: &b" + IridiumSkyblock.getInstance().getDescription().getVersion()));
        sender.sendMessage(StringUtils.color("&7Plugin Author: &bPeaches_MLG"));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return null;
    }
}
