package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandWarp;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.IslandWarpsGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WarpsCommand extends Command {

    /**
     * The default constructor.
     */
    public WarpsCommand() {
        super(Arrays.asList("warp", "warps"), "Open the Island Warps Menu", "%prefix% &7/is warp <name> <password>", "", true, Duration.ZERO);
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
        Optional<Island> island = user.getIsland();
        if (island.isEmpty()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (args.length != 2 && args.length != 3) {
            player.openInventory(new IslandWarpsGUI(island.get(), player.getOpenInventory().getTopInventory()).getInventory());
            return true;
        }

        List<IslandWarp> islandWarps = IridiumSkyblock.getInstance().getDatabaseManager().getIslandWarpTableManager().getEntries(island.get());
        Optional<IslandWarp> islandWarp = islandWarps.stream().filter(warp -> warp.getName().equalsIgnoreCase(args[1])).findFirst();
        if (islandWarp.isEmpty()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().unknownWarp.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (islandWarp.get().getPassword() != null) {
            if (args.length != 3) {
                sender.sendMessage(StringUtils.color(syntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix).replace("%warp%", args[1])));
                return false;
            }

            if (!islandWarp.get().getPassword().equals(args[2])) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().incorrectPassword.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                return false;
            }
        }

        IridiumSkyblock.getInstance().getIslandManager().teleportWarp(player, islandWarp.get(), IridiumSkyblock.getInstance().getConfiguration().teleportDelay);
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
        Player player = (Player) commandSender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        if (island.isPresent()) {
            List<IslandWarp> islandWarps = IridiumSkyblock.getInstance().getDatabaseManager().getIslandWarpTableManager().getEntries(island.get());
            return islandWarps.stream()
                    .map(IslandWarp::getName)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

}
