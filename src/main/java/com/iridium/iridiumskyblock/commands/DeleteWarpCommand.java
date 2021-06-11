package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandWarp;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeleteWarpCommand extends Command {

    /**
     * The default constructor.
     */
    public DeleteWarpCommand() {
        super(Arrays.asList("delwarp", "deletewarp"), "Deletes an Island warp", "", true);
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
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();

        if (island.isPresent()) {
            if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(player), PermissionType.MANAGE_WARPS)) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotManageWarps.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                return;
            }
            List<IslandWarp> islandWarps = IridiumSkyblock.getInstance().getDatabaseManager().getIslandWarpTableManager().getEntries(island.get());
            Optional<IslandWarp> islandWarp = islandWarps.stream().filter(warp -> warp.getName().equalsIgnoreCase(args[1])).findFirst();
            if (islandWarp.isPresent()) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().deletingWarp
                        .replace("%name%", islandWarp.get().getName())
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
                );
                IridiumSkyblock.getInstance().getDatabaseManager().getIslandWarpTableManager().delete(islandWarp.get());
            } else {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().unknownWarp.replace("%prefix%",
                        IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
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
        Optional<Island> island = IridiumSkyblock.getInstance().getUserManager().getUser((OfflinePlayer) commandSender).getIsland();
        List<IslandWarp> islandWarps =
                island.isPresent() ? IridiumSkyblock.getInstance().getDatabaseManager().getIslandWarpTableManager().getEntries(island.get()) :
                        Collections.emptyList();
        if (args.length == 2) {
            return islandWarps.stream().map(IslandWarp::getName).filter(s -> s.contains(args[1])).collect(Collectors.toList());
        }
        if (args.length == 3) {
            return Stream.of("icon", "description").filter(s -> s.contains(args[2])).collect(Collectors.toList());
        }
        if (args.length == 4) {
            if (args[2].equalsIgnoreCase("icon")) {
                return Arrays.stream(XMaterial.values()).map(XMaterial::name).filter(s -> s.toLowerCase().contains(args[3].toLowerCase())).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

}
