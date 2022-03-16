package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.gui.BlockLimitGUI;
import com.iridium.iridiumskyblock.gui.BlockLimitSelectLevelGUI;
import com.iridium.iridiumskyblock.upgrades.BlockLimitUpgrade;
import com.iridium.iridiumskyblock.upgrades.BlockLimitUpgrade.LimitedBlock;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Command which shows all limited blocks.
 *
 * @see LimitedBlock
 */
public class BlockLimitCommand extends Command {

    /**
     * The default constructor.
     */
    public BlockLimitCommand() {
        super(Collections.singletonList("blocklimits"), "Show block limits", "", true, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Shows all the valuable blocks and spawners.
     *
     * @param sender    The CommandSender which executes this command
     * @param arguments The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] arguments) {
        Player player = (Player) sender;
        if (arguments.length != 2) {
            player.openInventory(new BlockLimitSelectLevelGUI(player.getOpenInventory().getTopInventory()).getInventory());
            return true;
        }

        Optional<Map.Entry<Integer, BlockLimitUpgrade>> level = IridiumSkyblock.getInstance().getUpgrades().blockLimitUpgrade.upgrades.entrySet().stream().filter(upgrades -> upgrades.getKey().equals(Integer.valueOf(arguments[1]))).findAny();
        if (!level.isPresent()) {
            player.openInventory(new BlockLimitSelectLevelGUI(player.getOpenInventory().getTopInventory()).getInventory());
            return true;
        }

        player.openInventory(new BlockLimitGUI(level.get().getKey(), player.getOpenInventory().getTopInventory()).getInventory());
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
        return IridiumSkyblock.getInstance().getUpgrades().blockLimitUpgrade.upgrades.keySet().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

}