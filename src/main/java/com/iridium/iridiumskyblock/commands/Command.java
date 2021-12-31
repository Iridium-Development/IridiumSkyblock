package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnore;
import com.iridium.iridiumskyblock.managers.CooldownProvider;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract commands used to easily create subcommands.
 */
public abstract class Command {

    public final @NotNull List<String> aliases;
    @JsonIgnore
    public final @NotNull List<Command> childs;
    public final @NotNull String description;
    public final @NotNull String permission;
    public static String syntax;
    @JsonIgnore
    public final boolean onlyForPlayers;
    public final boolean enabled;
    public static long cooldownInSeconds;
    @JsonIgnore
    private static CooldownProvider<CommandSender> cooldownProvider;

    /**
     * The default constructor.
     *
     * @param aliases        The list of aliases for this command, can be empty. Also contains the command name.
     * @param description    The description of this command
     * @param syntax         The specified syntax for this command
     * @param permission     The permission required for this command. Empty string will mean no permission
     * @param onlyForPlayers true if this command is only for Players
     * @param cooldown       The cooldown for non-bypassing players
     */
    public Command(@NotNull List<String> aliases, @NotNull String description, @NotNull String syntax, @NotNull String permission, boolean onlyForPlayers, Duration cooldown) {
        this.aliases = aliases;
        this.childs = new ArrayList<>();
        this.description = description;
        Command.syntax = syntax;
        this.permission = permission;
        this.onlyForPlayers = onlyForPlayers;
        this.enabled = true;
        Command.cooldownInSeconds = cooldown.getSeconds();
    }

    /**
     * The default constructor.
     *
     * @param aliases        The list of aliases for this command, can be empty. Also contains the command name.
     * @param description    The description of this command
     * @param permission     The permission required for this command. Empty string will mean no permission
     * @param onlyForPlayers true if this command is only for Players
     * @param cooldown       The cooldown for non-bypassing players
     */
    public Command(@NotNull List<String> aliases, @NotNull String description, @NotNull String permission, boolean onlyForPlayers, Duration cooldown) {
        this.aliases = aliases;
        this.childs = new ArrayList<>();
        this.description = description;
        Command.syntax = "";
        this.permission = permission;
        this.onlyForPlayers = onlyForPlayers;
        this.enabled = true;
        Command.cooldownInSeconds = cooldown.getSeconds();
    }

    public static CooldownProvider<CommandSender> getCooldownProvider() {
        if (cooldownProvider == null) {
            cooldownProvider = CooldownProvider.newInstance(Duration.ofSeconds(cooldownInSeconds));
        }

        return cooldownProvider;
    }

    public void addChilds(Command... newChilds) {
        childs.addAll(Arrays.asList(newChilds));
    }

    Optional<Command> getChildByName(String name) {
        return childs.stream()
            .filter(command -> command.aliases.contains(name.toLowerCase()))
            .findAny();
    }

    public List<String> getChildNames() {
        return childs.stream()
            .map(command -> command.aliases.get(0))
            .collect(Collectors.toList());
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     *
     * @param sender    The CommandSender which executes this command
     * @param arguments The arguments used with this command. They contain the sub-command
     */
    public abstract boolean execute(CommandSender sender, String[] arguments);

    /**
     * Handles tab-completion for this command.
     *
     * @param commandSender The CommandSender which tries to tab-complete
     * @param command       The command
     * @param label         The label of the command
     * @param args          The arguments already provided by the sender
     * @return The list of tab completions for this command
     */
    public abstract List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args);

}
