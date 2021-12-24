package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IslandCreateEvent;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.configs.Schematics.SchematicConfig;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.IslandCreateGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.*;

/**
 * Command which creates a new Island for a user.
 */
public class CreateCommand extends Command {

    /**
     * The default constructor.
     */
    public CreateCommand() {
        super(Collections.singletonList("create"), "Create an Island", "%prefix% &7/is create <name> <schematic>", "", true, Duration.ofSeconds(5));
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
            case 0:
            case 1:
                createIsland(player, null);
                break;
            case 2:
                createIsland(player, args[1]);
                break;
            case 3:
                Optional<Schematics.SchematicConfig> schematicConfig = IridiumSkyblock.getInstance().getSchematics().schematics.entrySet().stream().filter(entry -> entry.getKey().equalsIgnoreCase(args[2])).map(Map.Entry::getValue).findFirst();
                if (schematicConfig.isPresent()) {
                    return createIsland(player, args[1], schematicConfig.get());
                } else {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandSchematicNotFound.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * Opens the island schematic picker GUI.
     *
     * @param player The player who performed this command
     * @param name   The name of the new island
     */
    private void createIsland(Player player, @Nullable String name) {
        if (IridiumSkyblock.getInstance().getSchematics().schematics.size() == 1) {
            SchematicConfig schematicConfig = IridiumSkyblock.getInstance().getSchematics().schematics.values().stream().findFirst().get();
            createIsland(player, name, schematicConfig);
            return;
        }

        player.openInventory(new IslandCreateGUI(player, name).getInventory());
    }

    /**
     * Creates an island for a specific Player and then teleports them to the island home.
     *
     * @param player          The owner of the island
     * @param name            The name of  the island
     * @param schematicConfig The schematic of the island
     * @return True if the island has been created successfully
     */
    private boolean createIsland(Player player, String name, Schematics.SchematicConfig schematicConfig) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        if (user.getIsland().isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        if (name != null && IridiumSkyblock.getInstance().getIslandManager().getIslandByName(name).isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandWithNameAlreadyExists.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        }

        IslandCreateEvent islandCreateEvent = new IslandCreateEvent(user, name, schematicConfig);
        Bukkit.getPluginManager().callEvent(islandCreateEvent);
        if (islandCreateEvent.isCancelled()) return false;

        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().creatingIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        IridiumSkyblock.getInstance().getIslandManager().createIsland(player, islandCreateEvent.getIslandName(), islandCreateEvent.getSchematicConfig()).thenAccept(island ->
                Bukkit.getScheduler().runTask(IridiumSkyblock.getInstance(), () -> {
                    IridiumSkyblock.getInstance().getIslandManager().teleportHome(player, island, 0);
                    IridiumSkyblock.getInstance().getNms().sendTitle(player, IridiumSkyblock.getInstance().getConfiguration().islandCreateTitle, IridiumSkyblock.getInstance().getConfiguration().islandCreateSubTitle, 20, 40, 20);
                })
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
        if (args.length == 3) {
            return new ArrayList<>(IridiumSkyblock.getInstance().getSchematics().schematics.keySet());
        }

        // We currently don't want to tab-completion here
        // Return a new List, so it isn't a list of online players
        return Collections.emptyList();
    }

}
