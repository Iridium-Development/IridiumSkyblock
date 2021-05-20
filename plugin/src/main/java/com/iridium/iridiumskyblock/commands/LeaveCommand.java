package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.LogAction;
import com.iridium.iridiumskyblock.api.UserLeaveEvent;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandLog;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.ConfirmationGUI;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Command which allows users to leave their Island.
 */
public class LeaveCommand extends Command {

    /**
     * The default constructor.
     */
    public LeaveCommand() {
        super(Collections.singletonList("leave"), "Leave your Island", "", true);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Allows users to leave their Island.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();

        if (island.isPresent()) {
            if (user.getIslandRank().equals(IslandRank.OWNER)) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotLeaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            } else {
                ConfirmationGUI confirmationGUI = new ConfirmationGUI(() -> {
                    UserLeaveEvent userLeaveEvent = new UserLeaveEvent(island.get(), user);
                    Bukkit.getPluginManager().callEvent(userLeaveEvent);
                    if (userLeaveEvent.isCancelled()) return;

                    user.setIsland(null);
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youHaveLeftIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    for (User member : island.get().getMembers()) {
                        Player p = Bukkit.getPlayer(member.getUuid());
                        if (p != null) {
                            p.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().playerLeftIsland.replace("%player%", player.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                        }
                    }
                    PlayerUtils.teleportSpawn(player);
                    IslandLog islandLog = new IslandLog(island.get(), LogAction.USER_LEFT, user, null, 0, "");
                    IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().addEntry(islandLog);
                });
                player.openInventory(confirmationGUI.getInventory());
            }
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
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
        // Return a new List so it isn't a list of online players
        return Collections.emptyList();
    }

}
