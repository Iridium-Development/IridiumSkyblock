package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Commands which shows a list of the main contributors.
 */
public class CreditsCommand extends Command {

    /*
    Please dont add yourself to this list, if you contribute enough I will add you
     */
    private final List<String> contributors = Arrays.asList("Peaches_MLG", "das_", "SlashRemix");

    /**
     * The default constructor.
     */
    public CreditsCommand() {
        super(Arrays.asList("credits", "contributors"), "A list of players who helped make IridiumSkyblock", "", false);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Sends a list of all main contributors of this plugin to player.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        for (String name : contributors) {
            Role role = name.equalsIgnoreCase("Peaches_MLG") ? Role.Owner : Role.Contributor;
            sender.sendMessage(StringUtils.color("&7 - &b" + name + " (" + role.name() + ")"));
        }
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
        // Return a new ArrayList so it isn't a list of online players
        return Collections.emptyList();
    }

    /**
     * Represents a role of a contributor in the credits.
     */
    private enum Role {
        Owner, Contributor
    }

}
