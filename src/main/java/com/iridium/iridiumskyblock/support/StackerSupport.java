package com.iridium.iridiumskyblock.support;

import com.iridium.iridiumskyblock.support.material.IridiumMaterial;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.entity.EntityType;

public interface StackerSupport {
    int getExtraBlocks(Island island, IridiumMaterial material);

    int getExtraSpawners(Island island, EntityType entityType);
}
