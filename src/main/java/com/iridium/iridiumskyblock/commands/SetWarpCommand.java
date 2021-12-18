package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandUpgrade;
import com.iridium.iridiumskyblock.database.IslandWarp;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.LocationUtils;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand extends Command {

    /**
     * The default constructor.
     */
    public SetWarpCommand() {
        super(Arrays.asList("setwarp", "createwarp"), "Creates an Island warp", "%prefix% &7/is setwarp <name> <password>", "", true, Duration.ZERO);
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
        if (args.length < 2) {
            sender.sendMessage(StringUtils.color(syntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        if (island.isEmpty()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(player), PermissionType.MANAGE_WARPS)) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotManageWarps.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        List<IslandWarp> islandWarps = IridiumSkyblock.getInstance().getDatabaseManager().getIslandWarpTableManager().getEntries(island.get());
        if (islandWarps.stream().anyMatch(islandWarp -> islandWarp.getName().equalsIgnoreCase(args[1]))) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().warpAlreadyExists.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        IslandUpgrade islandUpgrade = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(island.get(), "warp");
        if (islandWarps.size() >= IridiumSkyblock.getInstance().getUpgrades().warpsUpgrade.upgrades.get(islandUpgrade.getLevel()).amount) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().warpLimitReached.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (!island.get().isInIsland(player.getLocation())) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().onlySetWarpOnIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (!LocationUtils.isSafe(player.getLocation())) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notSafe.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        IslandWarp islandWarp = new IslandWarp(island.get(), player.getLocation(), args[1]);
        if (args.length == 3) {
            islandWarp.setPassword(args[2]);
        }

        IridiumSkyblock.getInstance().getDatabaseManager().getIslandWarpTableManager().addEntry(islandWarp);
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().createdWarp
                .replace("%name%", args[1])
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
        ));
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
