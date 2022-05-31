package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.api.IslandRenameEvent;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RenameCommand extends Command {

    public RenameCommand() {
        super(Collections.singletonList("rename"), "Change your island name", "%prefix% &7/is rename <name>", "", true, Duration.ofMinutes(1));
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Allows a user to change the island name.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length < 2) {
            sender.sendMessage(StringUtils.color(syntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        if (!island.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        String name = String.join(" ", Arrays.asList(args).subList(1, args.length));
        if (!user.getIslandRank().equals(IslandRank.OWNER)) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotChangeName.replace("%prefix%", (IridiumSkyblock.getInstance().getConfiguration()).prefix)));
            return false;
        }

        if (name.length() > (IridiumSkyblock.getInstance().getConfiguration()).maxIslandName) {
            player.sendMessage(StringUtils.color((IridiumSkyblock.getInstance().getMessages()).islandNameTooLong
                    .replace("%prefix%", (IridiumSkyblock.getInstance().getConfiguration()).prefix)
                    .replace("%name%", name)
                    .replace("%max_length%", String.valueOf(IridiumSkyblock.getInstance().getConfiguration().maxIslandName))
            ));
            return false;
        }

        if (name.length() < (IridiumSkyblock.getInstance().getConfiguration()).minIslandName) {
            player.sendMessage(StringUtils.color((IridiumSkyblock.getInstance().getMessages()).islandNameTooShort
                    .replace("%prefix%", (IridiumSkyblock.getInstance().getConfiguration()).prefix)
                    .replace("%name%", name)
                    .replace("%min_length%", String.valueOf(IridiumSkyblock.getInstance().getConfiguration().minIslandName))
            ));
            return false;
        }
        
        if (IridiumSkyblock.getInstance().getIslandManager().getIslandByName(name).isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandWithNameAlreadyExists.replace("%prefix%", (IridiumSkyblock.getInstance().getConfiguration()).prefix)));
            return false;
        }

        IslandRenameEvent islandRenameEvent = new IslandRenameEvent(user, name);
        Bukkit.getPluginManager().callEvent(islandRenameEvent);
        if (islandRenameEvent.isCancelled()) return false;

        final String finalName = islandRenameEvent.getIslandName();
        island.get().setName(finalName);
        island.get().getMembers().forEach(member -> {
            Player islandMember = Bukkit.getPlayer(member.getUuid());
            if (islandMember != null) {
                islandMember.sendMessage(StringUtils.color((IridiumSkyblock.getInstance().getMessages()).islandNameChanged
                        .replace("%prefix%", (IridiumSkyblock.getInstance().getConfiguration()).prefix)
                        .replace("%player%", player.getName())
                        .replace("%name%", finalName == null ? player.getName() : finalName)
                ));
            }
        });
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
