package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Command which sets a corner position of a schematic in our own schematic system.
 */
public class PositionCommand extends Command {

    /**
     * The default constructor.
     */
    public PositionCommand() {
        super(Arrays.asList("pos", "position"), "Set the corner position of a schematic", "iridiumskyblock.schematic", true);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Sets the corner position of a schematic.
     *
     * @param sender    The CommandSender which executes this command
     * @param arguments The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] arguments) {
        Player player = (Player) sender;
        if (!(arguments.length >= 2 && arguments[1].matches("[1-2]"))) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().invalidPositionCommandSyntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }

        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        int positionType = Integer.parseInt(arguments[1]);
        if (positionType == 1) {
            user.setSchematicPos1(player.getLocation());
        } else if (positionType == 2) {
            user.setSchematicPos2(player.getLocation());
        }

        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().setSchematicPosition.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
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
        return Arrays.asList("1", "2");
    }

}
