package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Schematics;
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
        IridiumSkyblock.getInstance().getIslandManager().makeIsland(player, islandName, schematicConfig);
    }
}
