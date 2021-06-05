package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnore;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Abstract commands used to easily create subcommands.
 */
public abstract class Command {

    public final @NotNull List<String> aliases;
    public final @NotNull String description;
    public final @NotNull String permission;
    @JsonIgnore
    public final boolean onlyForPlayers;
    public final boolean enabled;

    /**
     * The default constructor.
     *
     * @param aliases        The list of aliases for this command, can be empty. Also contains the command name.
     * @param description    The description of this command
     * @param permission     The permission required for this command. Empty string will mean no permission
     * @param onlyForPlayers true if this command is only for Players
     */
    public Command(@NotNull List<String> aliases, @NotNull String description, @NotNull String permission, boolean onlyForPlayers) {
        this.aliases = aliases;
        this.description = description;
        this.permission = permission;
        this.onlyForPlayers = onlyForPlayers;
        this.enabled = true;
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     *
     * @param sender    The CommandSender which executes this command
     * @param arguments The arguments used with this command. They contain the sub-command
     */
    public abstract void execute(CommandSender sender, String[] arguments);

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
