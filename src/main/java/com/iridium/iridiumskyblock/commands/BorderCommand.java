package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.Color;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.InventoryConfigGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Command which changes the Island's Border Color.
 */
public class BorderCommand extends Command {

    /**
     * The default constructor.
     */
    public BorderCommand() {
        super(Collections.singletonList("border"), "Change the Island Border", "%prefix% &7/is border <color>", "", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Changes the Island Border
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();
        if (island.isEmpty()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(player), PermissionType.BORDER)) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotManageBorder.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (args.length != 2) {
            player.openInventory(new InventoryConfigGUI(IridiumSkyblock.getInstance().getInventories().islandBorder, player.getOpenInventory().getTopInventory()).getInventory());
            return true;
        }

        Color color = Color.getColor(args[1]);
        if (color == null) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notAColor.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (!IridiumSkyblock.getInstance().getBorder().enabled.getOrDefault(color, true)) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().borderColorDisabled.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        island.get().setColor(color);
        island.get().getMembers().stream()
            .map(islandUser -> Bukkit.getPlayer(islandUser.getUuid()))
            .filter(Objects::nonNull)
            .forEach(islandPlayer -> islandPlayer.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandBorderChanged.replace("%player%", player.getName()).replace("%color%", color.toString()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)))
        );
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
        return Arrays.stream(Color.values())
            .map(Color::name)
            .collect(Collectors.toList());
    }

}
