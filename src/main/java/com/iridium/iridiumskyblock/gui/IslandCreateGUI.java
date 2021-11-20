package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.Schematics;
import com.iridium.iridiumskyblock.managers.CooldownProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * GUI which shows the users all available schematics when creating a new Island.
 */
public class IslandCreateGUI extends SchematicGUI {

    private final @NotNull Player player;
    private final @NotNull String islandName;
    private final @NotNull CooldownProvider<CommandSender> cooldownProvider;

    /**
     * The default constructor.
     *
     * @param player           The player who wants to create an Island
     * @param islandName       The name of the new Island
     * @param cooldownProvider The provider for cooldowns that should be started on success
     */
    public IslandCreateGUI(@NotNull Player player, @NotNull String islandName, @NotNull CooldownProvider<CommandSender> cooldownProvider) {
        this.player = player;
        this.islandName = islandName;
        this.cooldownProvider = cooldownProvider;
    }

    /**
     * Executed when the player selects the Island schematic.
     *
     * @param schematicConfig The data of the selected schematic
     */
    @Override
    public void selectSchematic(Schematics.SchematicConfig schematicConfig) {
        boolean success = IridiumSkyblock.getInstance().getIslandManager().makeIsland(player, islandName, schematicConfig);
        if (success) cooldownProvider.applyCooldown(player);
    }

}
