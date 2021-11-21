package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.Mission.MissionType;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.DailyIslandMissionsGUI;
import com.iridium.iridiumskyblock.gui.InventoryConfigGUI;
import com.iridium.iridiumskyblock.gui.IslandMissionsGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MissionCommand extends Command {

    /**
     * The default constructor.
     */
    public MissionCommand() {
        super(Collections.singletonList("missions"), "View your Island missions", "%prefix% &7/is mission <type>", "", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
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

        if (args.length != 2) {
            player.openInventory(new InventoryConfigGUI(IridiumSkyblock.getInstance().getInventories().missionSelectGUI, player.getOpenInventory().getTopInventory()).getInventory());
            return true;
        }
        switch (Mission.MissionType.getMission(args[1])) {
            case ONCE:
                player.openInventory(new IslandMissionsGUI(island.get(), player.getOpenInventory().getTopInventory()).getInventory());
                return true;
            case DAILY:
                player.openInventory(new DailyIslandMissionsGUI(island.get(), player.getOpenInventory().getTopInventory()).getInventory());
                return true;
            default:
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().invalidMissionType.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                return false;
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
        // Return a new List, so it isn't a list of online players
        return Arrays.stream(Mission.MissionType.values())
                .map(MissionType::name)
                .collect(Collectors.toList());
    }

}
