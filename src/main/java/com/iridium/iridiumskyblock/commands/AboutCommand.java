package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Display plugin information to the player
 */
public class AboutCommand extends Command {

    /*
    Please don't add yourself to this list, if you contribute enough I (Peaches) will add you.
    */
    private final List<String> contributors = Arrays.asList("das_", "SlashRemix");

    /**
     * The default constructor.
     */
    public AboutCommand() {
        super(Collections.singletonList("about"), "Display plugin info", "", false);
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
        sender.sendMessage(StringUtils.color("&7Plugin Contributors: &b" + String.join(", ", contributors)));
    }

    /**
     * Handles tab-completion for this command.
     *
     * @param commandSender The CommandSender which tries to tab-complete
     * @param command       The command
     * @param label         The label of the command
     * @param args          The arguments already provided by the sender
     * @return The list of tab completions for this command
     */
    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        // We currently don't want to tab-completion here
        // Return a new List so it isn't a list of online players
        return Collections.emptyList();
    }

}
