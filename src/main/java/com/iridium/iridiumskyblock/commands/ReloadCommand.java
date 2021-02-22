package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * Command which allows the Admins to quickly reload the configuration files without a restart.
 */
public class ReloadCommand extends Command {

    private final IridiumSkyblock iridiumSkyblock;

    /**
     * The default constructor.
     *
     * @param iridiumSkyblock The instance of IridiumSkyblock used by this plugin
     */
    public ReloadCommand(IridiumSkyblock iridiumSkyblock) {
        super(Collections.singletonList("reload"), "Reload your configurations", "iridiumskyblock.reload", false);
        this.iridiumSkyblock = iridiumSkyblock;
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Reloads all configuration files.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        iridiumSkyblock.loadConfigs();
        sender.sendMessage(StringUtils.color(iridiumSkyblock.getMessages().reloaded.replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)));
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

}
