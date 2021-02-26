package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * Display plugin information to the player
 */
public class AboutCommand extends Command {
    /**
     * The default constructor.
     */
    public AboutCommand() {
        super(Collections.singletonList("about"), "Displays plugin info", "", false);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Displays plugin info for the player
     *
     * @param sender The CommandSender which executes this command
     * @param args The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(StringUtils.color("&7Plugin Name: &bIridiumSkyblock"));
        sender.sendMessage(StringUtils.color("&7Plugin Version: &b" + IridiumSkyblock.getInstance().getDescription().getVersion()));
        sender.sendMessage(StringUtils.color("&7Plugin Author: &bPeaches_MLG"));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return null;
    }
}
