package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.StringUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        List<Command> availableCommands = IridiumSkyblock.getInstance().getCommandManager().commands.stream()
                .filter(command -> command.enabled)
                .filter(command -> sender.hasPermission(command.permission) || command.permission.isEmpty())
                .collect(Collectors.toList());

        int page = 1;
        int maxPage = (int) Math.ceil(availableCommands.size() / 8.0);

        // Read optional page argument
        if (arguments.length > 1) {
            String pageArgument = arguments[1];
            if (pageArgument.matches("[0-9]+")) {
                page = Integer.parseInt(pageArgument);
            }
        }

        // Correct requested page if it's out of bounds
        if (page > maxPage) {
            page = maxPage;
        } else if (page < 1) {
            page = 1;
        }

        // Prepare the footer
        TextComponent footerText = new TextComponent(StringUtils.color(IridiumSkyblock.getInstance().getMessages().helpCommandFooter
                .replace("%page%", String.valueOf(page))
                .replace("%max_page%", String.valueOf(maxPage))));
        TextComponent previousButton = new TextComponent(StringUtils.color(IridiumSkyblock.getInstance().getMessages().helpCommandPreviousPage));
        TextComponent nextButton = new TextComponent(StringUtils.color(IridiumSkyblock.getInstance().getMessages().helpCommandNextPage));
        if (page != 1) {
            previousButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/is help " + (page - 1)));
            previousButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(StringUtils.color(IridiumSkyblock.getInstance().getMessages().helpCommandPreviousPageHover)).create()));
        }
        if (page != maxPage) {
            nextButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/is help " + (page + 1)));
            nextButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(StringUtils.color(IridiumSkyblock.getInstance().getMessages().helpCommandNextPageHover)).create()));
        }

        // Send all messages
        sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().helpCommandHeader));
        availableCommands.stream()
                .skip((page - 1) * 8L)
                .limit(8)
                .map(command -> StringUtils.color(IridiumSkyblock.getInstance().getMessages().helpCommandMessage
                        .replace("%command%", command.aliases.get(0))
                        .replace("%description%", command.description)))
                .forEach(sender::sendMessage);
        sender.spigot().sendMessage(previousButton, footerText, nextButton);
    }

    /**
     * Handles tab-completion for this command.
     *
     * @param commandSender The CommandSender which tries to tab-complete
     * @param cmd           The command
     * @param label         The label of the command
     * @param args          The arguments already provided by the sender
     * @return The list of tab completions for this command
     */
    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command cmd, String label, String[] args) {
        int availableCommandAmount = (int) IridiumSkyblock.getInstance().getCommandManager().commands.stream()
                .filter(command -> command.enabled)
                .filter(command -> commandSender.hasPermission(command.permission) || command.permission.isEmpty())
                .count();

        return IntStream.rangeClosed(1, (int) Math.ceil(availableCommandAmount / 8.0)) // Convert to page numbers
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

}
