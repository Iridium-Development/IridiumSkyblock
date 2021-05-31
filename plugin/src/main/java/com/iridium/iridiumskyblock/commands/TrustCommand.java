package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.LogAction;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandLog;
import com.iridium.iridiumskyblock.database.IslandTrusted;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.TrustedGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class TrustCommand extends Command {

    /**
     * The default constructor.
     */
    public TrustCommand() {
        super(Arrays.asList("trust", "trusted"), "View your Trusted members", "", true);
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
            if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(player), IridiumSkyblock.getInstance().getPermissions().trust, "trust")) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotManageTrusts.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                return;
            }
            if (args.length == 2) {
                Player p = Bukkit.getPlayer(args[1]);
                if (p != null) {
                    User u = IridiumSkyblock.getInstance().getUserManager().getUser(p);
                    if (u.getIsland().map(Island::getId).orElse(0) != island.get().getId()) {
                        if (IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager().getEntries(island.get()).stream().noneMatch(it -> it.getUser().getUuid().equals(p.getUniqueId()))) {
                            IslandTrusted islandTrusted = new IslandTrusted(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(p),
                                    IridiumSkyblock.getInstance().getUserManager().getUser(player));
                            IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager().addEntry(islandTrusted);

                            island.get().getMembers().stream().map(user1 -> Bukkit.getPlayer(user1.getUuid())).filter(Objects::nonNull).forEach(player1 ->
                                    player1.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().trustedPlayer
                                            .replace("%truster%", player.getName())
                                            .replace("%player%", p.getName())
                                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)))
                            );
                            p.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().playerTrustedYou
                                    .replace("%truster%", player.getName())
                                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
                            );
                            IslandLog islandLog = new IslandLog(island.get(), LogAction.USER_TRUSTED, user, u, 0, "");
                            IridiumSkyblock.getInstance().getDatabaseManager().getIslandLogTableManager().addEntry(islandLog);
                        } else {
                            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyTrusted.replace("%prefix%",
                                    IridiumSkyblock.getInstance().getConfiguration().prefix)));
                        }
                    } else {
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyInYourIsland.replace("%prefix%",
                                IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    }
                } else {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notAPlayer.replace("%prefix%",
                            IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            } else {
                player.openInventory(new TrustedGUI(island.get()).getInventory());
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
        // We currently don't want to tab-completion here
        // Return a new List so it isn't a list of online players
        if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}
