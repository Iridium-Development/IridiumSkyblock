package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class IslandCreateGUI extends SchematicGUI {
    private final @NotNull Player player;
    private final @NotNull String islandName;

    public IslandCreateGUI(@NotNull Player player, @NotNull String islandName) {
        this.player = player;
        this.islandName = islandName;
    }

    @Override
    public void selectSchematic(Schematics.SchematicConfig schematicConfig) {
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        if (user.getIsland() != null) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }
        if (IridiumSkyblock.getInstance().getDatabaseManager().getIslandByName(islandName).isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandWithNameAlreadyExists.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().creatingIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        IridiumSkyblock.getInstance().getIslandManager().createIsland(player, islandName, schematicConfig).thenAccept(island -> player.teleport(island.getHome()));
    }
}
