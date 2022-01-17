package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.commands.home.CurrentOffsetCommand;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command which teleports users to their Island home.
 */
public class HomeCommand extends Command {

    public CurrentOffsetCommand currentOffsetCommand = new CurrentOffsetCommand();

    /**
     * The default constructor.
     */
    public HomeCommand() {
        super(Arrays.asList("home", "go"), "Teleport to your Island home", "", true, Duration.ofSeconds(3));

        addChilds(currentOffsetCommand);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Teleports users to their Island home.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();

        if (island.isPresent()) {
            IridiumSkyblock.getInstance().getIslandManager().teleportHome(player, island.get(), IridiumSkyblock.getInstance().getConfiguration().teleportDelay);
            return true;
        } else if (IridiumSkyblock.getInstance().getConfiguration().createIslandOnHome) {
            IridiumSkyblock.getInstance().getCommands().createCommand.execute(player, new String[]{});
            return true;
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
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
        // Return a new List, so it isn't a list of online players
        return Collections.emptyList();
    }

}
