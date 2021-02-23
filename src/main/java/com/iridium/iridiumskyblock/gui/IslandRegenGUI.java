package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class IslandRegenGUI extends SchematicGUI {
    private final @NotNull Player player;

    public IslandRegenGUI(@NotNull Player player) {
        this.player = player;
    }

    @Override
    public void selectSchematic(Schematics.SchematicConfig schematicConfig) {
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        if (user.getIsland() == null) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return;
        }
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().regeneratingIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        IridiumSkyblock.getInstance().getIslandManager().regenerateIsland(user.getIsland(), schematicConfig.name);
    }
}
