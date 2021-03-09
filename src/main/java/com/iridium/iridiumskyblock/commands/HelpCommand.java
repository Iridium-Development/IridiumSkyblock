package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class HelpCommand extends Command {

    /**
     * The default constructor.
     */
    public HelpCommand() {
        super(Collections.singletonList("help"), "Show a list of all commands", "", false);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     *
     * @param sender    The CommandSender which executes this command
     * @param arguments The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] arguments) {
        String header = IridiumSkyblock.getInstance().getMessages().helpCommandHeader;
        String commandTemplate = IridiumSkyblock.getInstance().getMessages().helpCommandMessage;

        sender.sendMessage(StringUtils.color(header));
        IridiumSkyblock.getInstance().getCommandManager().commands.stream()
                .filter(command -> command.enabled)
                .filter(command -> sender.hasPermission(command.permission) || command.permission.equals(""))
                .map(command -> commandTemplate
                                .replace("%command%", command.aliases.get(0))
                                .replace("%description%", command.description))
                .map(StringUtils::color)
                .forEach(sender::sendMessage);
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
        return Collections.emptyList();
    }

}
