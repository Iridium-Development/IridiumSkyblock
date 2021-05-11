package com.iridium.iridiumskyblock.commands;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandWarp;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SetWarpCommand extends Command {

    /**
     * The default constructor.
     */
    public SetWarpCommand() {
        super(Arrays.asList("setwarp", "createwarp"), "Creates an Island warp", "", true);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Shows an overview over the members of the Island and allows quick rank management.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("/is setwarp <name>");
        }
        Player player = (Player) sender;
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = user.getIsland();

        if (island.isPresent()) {
            List<IslandWarp> islandWarps = IridiumSkyblock.getInstance().getDatabaseManager().getIslandWarpTableManager().getEntries(island.get());
            if (islandWarps.stream().anyMatch(islandWarp -> islandWarp.getName().equalsIgnoreCase(args[1]))) {
                IslandWarp islandWarp = islandWarps.stream().filter(warp -> warp.getName().equals(args[1])).findFirst().get();
                if (args.length == 2) {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().warpAlreadyExists.replace("%prefix%",
                            IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    return;
                }
                switch (args[2]) {
                    case "icon":
                        if (args.length != 4) {
                            sender.sendMessage("/is setwarp <name> icon <icon>");
                        }
                        Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(args[3]);
                        if (xMaterial.isPresent()) {
                            islandWarp.setIcon(xMaterial.get());
                            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().warpIconSet.replace("%prefix%",
                                    IridiumSkyblock.getInstance().getConfiguration().prefix)));
                        } else {
                            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().materialDoesntExist.replace("%prefix%",
                                    IridiumSkyblock.getInstance().getConfiguration().prefix)));
                        }
                        break;
                    case "description":
                        if (args.length < 4) {
                            sender.sendMessage("/is setwarp <name> description <description>");
                        }
                        String description = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                        islandWarp.setDescription(description);
                        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().warpDescriptionSet.replace("%prefix%",
                                IridiumSkyblock.getInstance().getConfiguration().prefix)));
                        break;
                }
            } else {
                if (islandWarps.size() < IridiumSkyblock.getInstance().getConfiguration().islandWarpSlots.size()) {
                    IslandWarp islandWarp = new IslandWarp(island.get(), player.getLocation(), args[1]);
                    IridiumSkyblock.getInstance().getDatabaseManager().getIslandWarpTableManager().addEntry(islandWarp);
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().createdWarp
                            .replace("%name%", args[1])
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
                    );
                } else {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().warpLimitReached.replace("%prefix%",
                            IridiumSkyblock.getInstance().getConfiguration().prefix)));
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
        if (args.length == 3) {
            return Arrays.asList("icon", "description");
        }
        if (args.length == 4) {
            if (args[2].equalsIgnoreCase("icon")) {
                return Arrays.stream(XMaterial.values()).map(XMaterial::toString).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

}
