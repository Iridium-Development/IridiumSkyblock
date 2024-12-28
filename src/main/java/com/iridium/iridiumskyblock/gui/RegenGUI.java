package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class RegenGUI extends SchematicGUI {
    private final Player player;

    public RegenGUI(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public void selectSchematic(String schematic) {
        IridiumSkyblock.getInstance().getCommandManager().executeCommand(player, IridiumSkyblock.getInstance().getCommands().regenCommand, new String[]{schematic});
    }
}
