package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.dependencies.iridiumcolorapi.IridiumColorAPI;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command which shows users a list of all IridiumSkyblock commands.
 */
public class HelpCommand extends Command {

    /**
     * The default constructor.
     */
    public HelpCommand() {
        super(Collections.singletonList("help"), "Show a list of all commands", "%prefix% &7/is help <page/command>", "", false, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Shows a list of all IridiumSkyblock commands.
     *
     * @param sender    The CommandSender which executes this command
     * @param arguments The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] arguments) {
        List<Command> availableCommands = IridiumSkyblock.getInstance().getCommandManager().commands.stream()
                .filter(command -> sender.hasPermission(command.permission) || command.permission.isEmpty())
                .collect(Collectors.toList());

        int page = 1;
        int maxPage = (int) Math.ceil(availableCommands.size() / 8.0);

        // Read optional page argument
        if (arguments.length > 1) {
            String pageArgument = arguments[1];
            if (pageArgument.matches("[0-9]+")) {
                page = Integer.parseInt(pageArgument);
            } else {
                return showCommandHelp(sender, arguments);
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
            previousButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/is " + IridiumSkyblock.getInstance().getCommands().helpCommand.aliases.get(0) + " " + (page - 1)));
            previousButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(StringUtils.color(IridiumSkyblock.getInstance().getMessages().helpCommandPreviousPageHover)).create()));
        }

        if (page != maxPage) {
            nextButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/is " + IridiumSkyblock.getInstance().getCommands().helpCommand.aliases.get(0) + " " + (page + 1)));
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

        if (sender instanceof Player) {
            ((Player) sender).spigot().sendMessage(previousButton, footerText, nextButton);
        }

        return true;
    }

    private boolean showCommandHelp(CommandSender commandSender, String[] arguments) {
        String[] newArguments = Arrays.copyOfRange(arguments, 1, arguments.length);
        Optional<Command> commandOptional = IridiumSkyblock.getInstance().getCommandManager().findExecutingCommand(newArguments);
        if (!commandOptional.isPresent()) {
            execute(commandSender, new String[0]);
            return false;
        }

        Command executingCommand = commandOptional.get();
        if (!commandSender.hasPermission(executingCommand.permission) && !executingCommand.permission.isEmpty()) {
            execute(commandSender, new String[0]);
            return false;
        }

        String syntax = IridiumColorAPI.stripColorFormatting(executingCommand.syntax.replaceAll("%prefix%\\s*", "").replace("&", "§"));
        if (syntax.isEmpty()) {
            syntax = IridiumSkyblock.getInstance().getConfiguration().defaultCommandSyntax.replace("%command%", executingCommand.aliases.get(0));
        }

        String finalSyntax = syntax;
        String subCommands = executingCommand.childs.stream()
            .filter(command -> commandSender.hasPermission(command.permission) || command.permission.isEmpty())
            .map(command -> command.aliases.get(0))
            .collect(Collectors.joining("/", "<", ">"));

        IridiumSkyblock.getInstance().getMessages().commandHelpMessage.stream()
            .map(line -> line.replace("%description%", executingCommand.description))
            .map(line -> line.replace("%syntax%", finalSyntax))
            .map(line -> line.replace("%subcommands%", subCommands.equals("<>") ? "" : subCommands))
            .map(StringUtils::color)
            .forEach(commandSender::sendMessage);

        return true;
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
        if (args.length == 2) {
            int availableCommandAmount = (int) IridiumSkyblock.getInstance().getCommandManager().commands.stream()
                    .filter(command -> commandSender.hasPermission(command.permission) || command.permission.isEmpty())
                    .count();

            // Return all numbers from 1 to the max page
            return IntStream.rangeClosed(1, (int) Math.ceil(availableCommandAmount / 8.0))
                    .boxed()
                    .map(String::valueOf)
                    .collect(Collectors.toList());
        }

        // We currently don't want to tab-completion here
        // Return a new List, so it isn't a list of online players
        return Collections.emptyList();
    }

}
