package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBooster;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FlyCommand extends Command {

    /**
     * The default constructor.
     */
    public FlyCommand() {
        super(Arrays.asList("fly", "flight"), "Toggle Island Flight", "%prefix% &7/is flight <enable/disable>", "", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Shows an overview over the members of the Island and allows quick rank management.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = IridiumSkyblock.getInstance().getIslandManager().getIslandViaPlayerLocation(player);
        if (!island.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notOnAnIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        boolean flight = !user.isFlying();
        if (args.length == 2) {
            if (!args[1].equalsIgnoreCase("enable") && !args[1].equalsIgnoreCase("disable") && !args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off")) {
                sender.sendMessage(StringUtils.color(syntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                return false;
            }

            flight = args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("on");
        }

        IslandBooster islandBooster = IridiumSkyblock.getInstance().getIslandManager().getIslandBooster(island.get(), "flight");
        if (!islandBooster.isActive() && !player.hasPermission("iridiumskyblock.fly")) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().flightBoosterNotActive.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        user.setFlying(flight);
        player.setAllowFlight(flight);
        player.setFlying(flight);

        if (flight) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().flightEnabled.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().flightDisabled.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
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
        if (args.length == 2) {
            return Arrays.asList("enable", "disable", "on", "off");
        }

        return Collections.emptyList();
    }

}
