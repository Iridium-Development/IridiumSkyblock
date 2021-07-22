package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandTrusted;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.VisitorsGUI;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExpelCommand extends Command {

    /**
     * The default constructor.
     */
    public ExpelCommand() {
        super(Collections.singletonList("expel"), "Kick a visitor", "%prefix% &7/is expel [<name>]", "", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Kicks a visitor from an Island.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if ( args.length != 1 && args.length != 2) {
            sender.sendMessage(StringUtils.color(syntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }
        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();

        if (island.isPresent()) {
            if (args.length == 1) {
                player.openInventory(new VisitorsGUI(1, island.get()).getInventory());
                return true;
            }
            Player targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer != null) {
                User targetUser = IridiumSkyblock.getInstance().getUserManager().getUser(targetPlayer);
                List<IslandTrusted> islandTrusted = IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager().getEntries(island.get());
                if (!island.get().equals(targetUser.getIsland().orElse(null)) && islandTrusted.stream().noneMatch(trustedIsland -> trustedIsland.getIsland().isPresent() && trustedIsland.getIsland().get().equals(targetUser.getIsland().orElse(null)))) {
                    if (island.get().isInIsland(targetPlayer.getLocation())) {
                        if (targetUser.isBypass() || targetPlayer.hasPermission("iridiumskyblock.visitbypass")) {
                            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotExpelPlayer.replace("%player%", targetUser.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                        } else if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, PermissionType.EXPEL)) {
                            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noPermission.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                        } else {
                            targetPlayer.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youHaveBeenExpelled.replace("%player%", user.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().expelledVisitor.replace("%player%", targetUser.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                            PlayerUtils.teleportSpawn(targetPlayer);
                            return true;
                        }
                    } else {
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userNotVisitingYourIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    }
                } else {
                    sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().inYourTeam.replace("%player%", targetUser.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            } else {
                sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notAPlayer.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }

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
        Player player = (Player) commandSender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        return island.map(value -> value.getPlayersOnIsland().stream().filter(visitor -> !value.equals(IridiumSkyblock.getInstance().getUserManager().getUser(visitor).getIsland().orElse(null))).map(Player::getName).collect(Collectors.toList())).orElse(Collections.emptyList());
    }
}