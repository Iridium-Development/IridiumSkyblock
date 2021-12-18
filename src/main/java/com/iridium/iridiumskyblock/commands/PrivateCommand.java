package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Command which makes the user's Island unvisitable.
 */
public class PrivateCommand extends Command {

    /**
     * The default constructor.
     */
    public PrivateCommand() {
        super(Collections.singletonList("private"), "Make your Island private", "", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Makes the user's Island unvisitable.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        if (island.isEmpty()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        island.get().setVisitable(false);
        int visitorCount = 0;
        for (User visitor : IridiumSkyblock.getInstance().getIslandManager().getPlayersOnIsland(island.get())) {
            if (visitor.isBypassing() || visitor.getIsland().map(Island::getId).orElse(0) == island.get().getId() || IridiumSkyblock.getInstance().getIslandManager().getIslandTrusted(island.get(), visitor).isPresent()) {
                continue;
            }

            PlayerUtils.teleportSpawn(visitor.getPlayer());
            visitor.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().expelledIslandLocked.replace("%player%", user.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            visitorCount++;
        }

        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandNowPrivate
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                .replace("%amount%", String.valueOf(visitorCount)))
        );
        return true;
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
