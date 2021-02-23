package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.ConfirmationGUI;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * Command which deletes a User's Island'
 */
public class DeleteCommand extends Command {

    private final IridiumSkyblock iridiumSkyblock;

    /**
     * The default constructor.
     *
     * @param iridiumSkyblock The instance of IridiumSkyblock used by this plugin
     */
    public DeleteCommand(IridiumSkyblock iridiumSkyblock) {
        super(Collections.singletonList("delete"), "Delete an island", "", true);
        this.iridiumSkyblock = iridiumSkyblock;
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Tries to create a new island for the user.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        if (user.getIsland() == null) {
            player.sendMessage(StringUtils.color(iridiumSkyblock.getMessages().dontHaveIsland.replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)));
            return;
        }
        player.openInventory(new ConfirmationGUI(iridiumSkyblock, () -> iridiumSkyblock.getIslandManager().deleteIsland(user.getIsland())).getInventory());
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
        return null;
    }

}
