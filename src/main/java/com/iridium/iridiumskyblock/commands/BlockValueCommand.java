package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.gui.BlockValueGUI;
import com.iridium.iridiumskyblock.gui.InventoryConfigGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Command which shows all the valuable blocks and spawners.
 *
 * @see com.iridium.iridiumskyblock.configs.BlockValues
 */
public class BlockValueCommand extends Command {

    /**
     * The default constructor.
     */
    public BlockValueCommand() {
        super(Collections.singletonList("blockvalues"), "Show the values of blocks", "", true, Duration.ZERO);

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
            player.openInventory(new InventoryConfigGUI(IridiumSkyblock.getInstance().getInventories().blockValueSelectGUI).getInventory());
            return true;
        }
        BlockValueGUI.BlockValueType blockValueType = BlockValueGUI.BlockValueType.getType(arguments[1]);
        if (blockValueType == null) {
            player.openInventory(new InventoryConfigGUI(IridiumSkyblock.getInstance().getInventories().blockValueSelectGUI).getInventory());
            return true;
        }
        player.openInventory(new BlockValueGUI(blockValueType).getInventory());
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
        // Return a new List so it isn't a list of online players
        return Arrays.stream(BlockValueGUI.BlockValueType.values()).map(Enum::toString).filter(s -> s.toLowerCase().contains(args[1].toLowerCase())).collect(Collectors.toList());
    }

}
