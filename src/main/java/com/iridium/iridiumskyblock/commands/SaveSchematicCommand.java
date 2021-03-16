package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.SchematicData;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SaveSchematicCommand extends Command {

    public SaveSchematicCommand() {
        super(Collections.singletonList("saveschematic"), "Creates a schematic out of a selected area", "iridiumskyblock.schematic", true);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     *
     * @param sender    The CommandSender which executes this command
     * @param arguments The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] arguments) {
        Player player = (Player) sender;
        if (arguments.length < 2) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().invalidSaveSchematicCommandSyntax.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }

        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Location schematicPos1 = user.getSchematicPos1();
        Location schematicPos2 = user.getSchematicPos2();
        if (schematicPos1 == null || schematicPos2 == null || schematicPos1.getWorld() != schematicPos2.getWorld()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().invalidSchematicPositions.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }

        String schematicName = arguments[1];
        boolean existsAlready = IridiumSkyblock.getInstance().getDatabaseManager().getSchematicDataList().stream()
                .map(SchematicData::getId)
                .anyMatch(schematic -> schematic.equals(schematicName));

        if (existsAlready && (arguments.length == 2 || !arguments[2].equalsIgnoreCase("confirm"))) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().missingSchematicConfirmation.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }

        IridiumSkyblock.getInstance().getSchematicManager().addSchematic(schematicName, schematicPos1, schematicPos2);
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().addedSchematic.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
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
        return Collections.emptyList();
    }
}
