package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.IslandCreateGUI;
import java.time.Duration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Command which creates a new Island for a user.
 */
public class CreateCommand extends Command {

    /**
     * The default constructor.
     */
    public CreateCommand() {
        super(Collections.singletonList("create"), "Create an Island", "", true, Duration.ofSeconds(5));
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Creates a new Island for a user.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        switch (args.length) {
            case 1:
                createIsland(player, player.getName());
                break;
            case 2:
                createIsland(player, args[1]);
                break;
            case 3:
                Optional<Schematics.SchematicConfig> schematicConfig = IridiumSkyblock.getInstance().getSchematics().schematics.entrySet().stream().filter(entry -> entry.getKey().equalsIgnoreCase(args[2])).map(Map.Entry::getValue).findFirst();
                if (schematicConfig.isPresent()) {
                    return IridiumSkyblock.getInstance().getIslandManager().makeIsland(player, args[1], schematicConfig.get());
                } else {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandSchematicNotFound.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
                break;
        }

        // Always return false because the cooldown is set during Island creation
        return false;
    }

    /**
     * Opens the island schematic picker GUI.
     *
     * @param player The player who performed this command
     * @param name   The name of the new island
     */
    private void createIsland(Player player, String name) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();

        if (island.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }

        if (IridiumSkyblock.getInstance().getIslandManager().getIslandByName(name).isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandWithNameAlreadyExists.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }

        player.openInventory(new IslandCreateGUI(player, name, getCooldownProvider()).getInventory());
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
        if (args.length == 3) {
            return IridiumSkyblock.getInstance().getSchematics().schematics.keySet().stream()
                .filter(schematicName -> schematicName.contains(args[2]))
                .collect(Collectors.toList());
        }

        // We currently don't want to tab-completion here
        // Return a new List so it isn't a list of online players
        return Collections.emptyList();
    }

}
