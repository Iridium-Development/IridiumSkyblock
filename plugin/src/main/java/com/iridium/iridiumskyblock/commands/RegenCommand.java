package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.IslandRegenGUI;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Command which resets the Island of a user.
 */
public class RegenCommand extends Command {

    /**
     * The default constructor.
     */
    public RegenCommand() {
        super(Collections.singletonList("regen"), "Regenerate your Island", "", true);
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
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = user.getIsland();

        if (island.isPresent()) {
            if (args.length == 1) {
                if (IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblockAPI.getInstance().getUser(player), IridiumSkyblock.getInstance().getPermissions().regen)) {
                    player.openInventory(new IslandRegenGUI(player).getInventory());
                } else {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotRegenIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            } else {
                Optional<Schematics.SchematicConfig> schematicConfig = IridiumSkyblock.getInstance().getSchematics().schematics.stream().filter(config -> config.name.equalsIgnoreCase(args[1])).findFirst();
                if (schematicConfig.isPresent()) {
                    IridiumSkyblock.getInstance().getIslandManager().regenerateIsland(island.get(), schematicConfig.get());
                } else {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().unknownSchematic.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            }
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
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
        if (args.length == 2) {
            return IridiumSkyblock.getInstance().getSchematics().schematics.stream().map(schematicConfig -> schematicConfig.name).collect(Collectors.toList());
        }

        // We currently don't want to tab-completion here
        // Return a new List so it isn't a list of online players
        return Collections.emptyList();
    }

}
