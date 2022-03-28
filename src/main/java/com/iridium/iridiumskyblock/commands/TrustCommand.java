package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.LogAction;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandLog;
import com.iridium.iridiumskyblock.database.IslandTrusted;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.IslandTrustedGUI;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TrustCommand extends Command {

    /**
     * The default constructor.
     */
    public TrustCommand() {
        super(Arrays.asList("trust", "trusted"), "View your Trusted members", "%prefix% &7/is trust <player>", "", true, Duration.ZERO);
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
        if (!island.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(player), PermissionType.TRUST)) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotManageTrusts.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (args.length != 2) {
            Inventory previousInventory = IridiumSkyblock.getInstance().getConfiguration().backButtons ? player.getOpenInventory().getTopInventory() : null;
            player.openInventory(new IslandTrustedGUI(island.get(), previousInventory).getInventory());
            return true;
        }

        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notAPlayer.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        User targetUser = IridiumSkyblock.getInstance().getUserManager().getUser(targetPlayer);
        if (targetUser.getIsland().map(Island::getId).orElse(0) == island.get().getId()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyInYourIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (IridiumSkyblock.getInstance().getIslandManager().getIslandTrusted(island.get(), targetUser).isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyTrusted.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        IslandTrusted islandTrusted = new IslandTrusted(island.get(), targetUser, user);
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager().addEntry(islandTrusted);

        island.get().getMembers().stream().map(user1 -> Bukkit.getPlayer(user1.getUuid())).filter(Objects::nonNull).forEach(onlineMember ->
                onlineMember.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().trustedPlayer
                        .replace("%truster%", player.getName())
                        .replace("%player%", targetPlayer.getName())
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)))
        );
        targetPlayer.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().playerTrustedYou
                .replace("%truster%", player.getName())
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
        );

        IslandLog islandLog = new IslandLog(island.get(), LogAction.USER_TRUSTED, user, targetUser, 0, "");
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().addEntry(islandLog);
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
