package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.Setting;
import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandSetting;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.IslandSettingsGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Commands which allows a user to manage the settings of his Island.
 */
public class SettingsCommand extends Command {

    /**
     * The default constructor.
     */
    public SettingsCommand() {
        super(Collections.singletonList("settings"), "Edit your Island settings", "", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Allows a user to manage his Island's permissions.
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

        player.openInventory(new IslandSettingsGUI(island.get(), player.getOpenInventory().getTopInventory()).getInventory());

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
        if (args.length == 2) {
            return IridiumSkyblock.getInstance().getSettingsList().entrySet().stream().filter(setting -> setting.getValue().isFeactureEnabled()).map(Map.Entry::getKey).collect(Collectors.toCollection(LinkedList::new));
        } else if (args.length == 3) {
            return Arrays.asList("true", "false");
        } else {
            return Collections.emptyList();
        }

    }

}
