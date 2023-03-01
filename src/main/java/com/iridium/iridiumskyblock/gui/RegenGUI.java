package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@Getter
public class RegenGUI extends SchematicGUI {
    private final Player player;

    public RegenGUI(Inventory previousInventory, Player player) {
        super(previousInventory);
        this.player = player;
    }

    @Override
    public void selectSchematic(String schematic) {
        IridiumSkyblock.getInstance().getCommands().regenCommand.execute(player, new String[]{schematic}, IridiumSkyblock.getInstance());
    }

}
