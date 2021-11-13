package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.ConfirmationGUI;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command which deletes a User's Island.
 */
public class DeleteCommand extends Command {

    /**
     * The default constructor.
     */
    public DeleteCommand() {
        super(Collections.singletonList("delete"), "Delete your Island", "", true, Duration.ofMinutes(5));
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Deletes a User's Island.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        if (!island.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (!user.getIslandRank().equals(IslandRank.OWNER)) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotDeleteIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        player.openInventory(new ConfirmationGUI(() -> {
            IridiumSkyblock.getInstance().getIslandManager().deleteIsland(island.get(), user);
            user.setIsland(null);
            user.setIslandRank(IslandRank.VISITOR);
            if (IridiumSkyblock.getInstance().getConfiguration().debug) {
                System.out.println("Player: " + user.getName() + "\n" +
                        "UUID: " + user.getUuid() + "\n" +
                        "Event: DeleteCommand");
            }
            IridiumSkyblock.getInstance().saveDataPlayer(user).join();
        }, getCooldownProvider()).getInventory());

        // Always return false because the cooldown is set by the ConfirmationGUI
        return false;
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
