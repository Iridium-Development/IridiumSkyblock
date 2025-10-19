package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.managers.CommandManager;
import lombok.Getter;
import org.bukkit.Bukkit;
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
        Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () -> runRegenCommand(schematic), 1L);

    }

    private void runRegenCommand(String schematic) {
        IridiumSkyblock plugin = IridiumSkyblock.getInstance();
        plugin.getCommandManager().executeCommand(player,
                IridiumSkyblock.getInstance().getCommands().regenCommand, new String[]{schematic}
        );
    }
}
