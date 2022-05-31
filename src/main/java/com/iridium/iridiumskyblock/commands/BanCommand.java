package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBan;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.IslandBansGUI;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Command which bans a visitor from your Island.
 */
public class BanCommand extends Command {

    /**
     * The default constructor.
     */
    public BanCommand() {
        super(Arrays.asList("ban", "bans"), "Bans a player from visiting your Island", "%prefix% &7/is ban <player>", "", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Bans a player visiting from an island
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length > 2) {
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

        if (args.length == 1) {
            Inventory previousInventory = IridiumSkyblock.getInstance().getConfiguration().backButtons ? player.getOpenInventory().getTopInventory() : null;
            player.openInventory(new IslandBansGUI(island.get(), previousInventory).getInventory());
            return false;
        }

        if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), user, PermissionType.BAN)) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noPermission.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null) {
            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notAPlayer.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        User targetUser = IridiumSkyblock.getInstance().getUserManager().getUser(targetPlayer);
        if (island.get().equals(targetUser.getIsland().orElse(null)) || IridiumSkyblock.getInstance().getIslandManager().getIslandTrusted(island.get(), targetUser).isPresent()) {
            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().inYourTeam.replace("%player%", targetUser.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (targetUser.isBypassing() || targetPlayer.hasPermission("iridiumskyblock.visitbypass")) {
            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotBanned.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (IridiumSkyblock.getInstance().getIslandManager().isBannedOnIsland(island.get(), targetUser)) {
            sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyBanned.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        IridiumSkyblock.getInstance().getDatabaseManager().getIslandBanTableManager().addEntry(new IslandBan(island.get(), user, targetUser));
        if (island.get().isInIsland(targetPlayer.getLocation())) {
            PlayerUtils.teleportSpawn(targetPlayer);
            targetPlayer.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youHaveBeenBanned.replace("%player%", user.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }

        sender.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().playerBanned.replace("%player%", targetUser.getName()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
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
        return PlayerUtils.getOnlinePlayerNames();
    }

}
