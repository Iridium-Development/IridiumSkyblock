package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LevelCommand extends Command {

    /**
     * The default constructor.
     */
    public LevelCommand() {
        super(Arrays.asList("level", "experience"), "Displays Island Level/Experience", "", true);
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
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandLevel
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                    .replace("%island_level%", String.valueOf(island.get().getLevel()))
                    .replace("%island_experience%", String.valueOf(island.get().getExperience()))
                    .replace("%island_experienceRequired%", String.valueOf(island.get().getExperienceRequiredToLevelUp()))
            ));
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notOnAnIsland.replace("%prefix%",
                    IridiumSkyblock.getInstance().getConfiguration().prefix)));
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
        return Stream.of("enable", "disable", "on", "off").filter(s -> s.contains(args[1])).collect(Collectors.toList());
    }

}
