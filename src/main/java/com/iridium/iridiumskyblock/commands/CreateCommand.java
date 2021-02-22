package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Command which creates a new island for an user.
 */
public class CreateCommand extends Command {

    private final IridiumSkyblock iridiumSkyblock;

    /**
     * The default constructor.
     *
     * @param iridiumSkyblock The instance of IridiumSkyblock used by this plugin
     */
    public CreateCommand(IridiumSkyblock iridiumSkyblock) {
        super(Collections.singletonList("create"), "Create an island", "", true);
        this.iridiumSkyblock = iridiumSkyblock;
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Tries to create a new island for the user.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            createIsland(player, player.getName(), iridiumSkyblock.getSchematics().schematics.get(0));
        } else if (args.length == 2) {
            createIsland(player, args[1], iridiumSkyblock.getSchematics().schematics.get(0));
        } else if (args.length == 3) {
            Optional<Schematics.SchematicConfig> schematicConfig = iridiumSkyblock.getSchematics().schematics.stream().filter(config -> config.getName().equalsIgnoreCase(args[2])).findFirst();
            if (schematicConfig.isPresent()) {
                createIsland(player, args[1], schematicConfig.get());
            } else {
                player.sendMessage(StringUtils.color(iridiumSkyblock.getMessages().islandSchematicNotFound.replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)));
            }
        }
    }

    /**
     * Creates a new island for the specified Player.
     *
     * @param player The player who performed this command
     * @param name   The name of the new island
     */
    private void createIsland(Player player, String name, Schematics.SchematicConfig schematicConfig) {
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        if (user.getIsland() != null) {
            player.sendMessage(StringUtils.color(iridiumSkyblock.getMessages().alreadyHaveIsland.replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)));
            return;
        }
        if (iridiumSkyblock.getDatabaseManager().getIslandByName(name).isPresent()) {
            player.sendMessage(StringUtils.color(iridiumSkyblock.getMessages().islandWithNameAlreadyExists.replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)));
            return;
        }
        player.sendMessage(StringUtils.color(iridiumSkyblock.getMessages().creatingIsland.replace("%prefix%", iridiumSkyblock.getConfiguration().prefix)));
        iridiumSkyblock.getIslandManager().createIsland(player, name, schematicConfig).thenAccept(island -> player.teleport(island.getHome()));
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
            return iridiumSkyblock.getSchematics().schematics.stream().map(Schematics.SchematicConfig::getName).collect(Collectors.toList());
        }
        return null;
    }

}
