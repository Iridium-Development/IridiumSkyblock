package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CreateCommand extends Command {

    private final IridiumSkyblock iridiumSkyblock;

    public CreateCommand(IridiumSkyblock iridiumSkyblock) {
        super(Collections.singletonList("create"), "Create an island", "", true);
        this.iridiumSkyblock = iridiumSkyblock;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length == 1) {
            createIsland(player, player.getName());
        } else {
            createIsland(player, args[1]);
        }
    }

    private void createIsland(Player player, String name) {
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
        iridiumSkyblock.getIslandManager().createIsland(player, name).thenAccept(island -> iridiumSkyblock.getSchematicManager().pasteSchematic(island, IridiumSkyblockAPI.getInstance().getWorld(), "test").thenRun(() ->
                player.teleport(island.getCenter(IridiumSkyblockAPI.getInstance().getWorld()))
        ));
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
