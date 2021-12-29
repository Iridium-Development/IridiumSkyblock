package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBan;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Command which un-bans a visitor from your island
 */
public class UnBanCommand extends Command {

    /**
     * The default constructor.
     */
    public UnBanCommand() {
        super(Collections.singletonList("unban"), "Un-ban a player visiting from your island", "%prefix% &7/is unban <player>", "", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * UnBans a player visiting from an island
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(StringUtils.color(syntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        if (!island.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, PermissionType.UNBAN)) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noPermission.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null) {
            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notAPlayer.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        User targetUser = IridiumSkyblock.getInstance().getUserManager().getUser(targetPlayer);
        Optional<IslandBan> islandBan = IridiumSkyblock.getInstance().getIslandManager().getIslandBan(island.get(), targetUser);
        if (!islandBan.isPresent()) {
            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notBanned.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> IridiumSkyblock.getInstance().getDatabaseManager().getIslandBanTableManager().delete(islandBan.get()));
        targetPlayer.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youHaveBeenUnBanned.replace("%player%", user.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().playerUnBanned.replace("%player%", targetUser.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
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

        return island.map(value -> IridiumSkyblock.getInstance().getDatabaseManager().getIslandBanTableManager().getEntries(value).stream()
                .map(islandBan -> islandBan.getBannedUser().getName())
                .collect(Collectors.toList())
        ).orElse(Collections.emptyList());
    }

}
