package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PermissionType;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.IslandRegenGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.*;

/**
 * Command which resets the Island of a user.
 */
public class RegenCommand extends Command {

    /**
     * The default constructor.
     */
    public RegenCommand() {
        super(Collections.singletonList("regen"), "Regenerate your Island", "%prefix% &7/is regen <schematic>", "", true, Duration.ofMinutes(5));
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Resets the Island of a user.
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

        if (args.length == 1) {
            if (!IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblock.getInstance().getUserManager().getUser(player), PermissionType.REGEN)) {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotRegenIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                return false;
            }

            player.openInventory(new IslandRegenGUI(player, getCooldownProvider()).getInventory());
            return false;
        }

        Optional<Schematics.SchematicConfig> schematicConfig = IridiumSkyblock.getInstance().getSchematics().schematics.entrySet().stream().filter(entry -> entry.getKey().equalsIgnoreCase(args[1])).map(Map.Entry::getValue).findFirst();
        if (!schematicConfig.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().unknownSchematic.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        IridiumSkyblock.getInstance().getIslandManager().regenerateIsland(island.get(), user, schematicConfig.get());
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
        return new ArrayList<>(IridiumSkyblock.getInstance().getSchematics().schematics.keySet());
    }

}
