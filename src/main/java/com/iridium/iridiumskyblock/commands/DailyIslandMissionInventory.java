package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.gui.DailyIslandMissionsGUI;
import org.bukkit.entity.Player;

import java.util.Optional;

public class DailyIslandMissionInventory extends AbstarctOpenInventory {
    public DailyIslandMissionInventory() {
    }

    boolean openPlayerInventory(Player player, Optional<Island> island) {
        player.openInventory(new DailyIslandMissionsGUI(island.get(), player.getOpenInventory().getTopInventory()).getInventory());
        return true;
    }
}