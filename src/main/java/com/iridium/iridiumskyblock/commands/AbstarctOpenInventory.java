package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.entity.Player;

import java.util.Optional;

public abstract class AbstarctOpenInventory {
    boolean openPlayerInventory(Player player, Optional<Island> island){
        return false;
    }
}
