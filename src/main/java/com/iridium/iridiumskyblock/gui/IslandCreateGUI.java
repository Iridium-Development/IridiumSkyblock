package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Schematics;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * GUI which shows the users all available schematics when creating a new Island.
 */
@Getter
public class IslandCreateGUI extends SchematicGUI {

    private final @NotNull Player player;
    private final String islandName;

    /**
     * The default constructor.
     *
     * @param player           The player who wants to create an Island
     * @param islandName       The name of the new Island
     */
    public IslandCreateGUI(@NotNull Player player, String islandName) {
        this.player = player;
        this.islandName = islandName;
    }

    /**
     * Executed when the player selects the Island schematic.
     *
     * @param schematicConfig The data of the selected schematic
     */
    @Override
    public void selectSchematic(Map.Entry<String, Schematics.SchematicConfig> schematicConfig) {
        IridiumSkyblock.getInstance().getCommands().createCommand.execute(player, new String[]{"create", islandName, schematicConfig.getKey()});
    }

}
