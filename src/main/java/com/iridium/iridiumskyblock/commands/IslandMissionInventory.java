package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.gui.IslandMissionsGUI;
import org.bukkit.entity.Player;

import java.util.Optional;

public class IslandMissionInventory extends AbstarctOpenInventory{
    public IslandMissionInventory() {
    }

    boolean openPlayerInventory(Player player, Optional<Island> island) {
        player.openInventory(new IslandMissionsGUI(island.get(), player.getOpenInventory().getTopInventory()).getInventory());

        return true;


    }
}