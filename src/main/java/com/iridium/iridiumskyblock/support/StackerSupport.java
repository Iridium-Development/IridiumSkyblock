package com.iridium.iridiumskyblock.support;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.entity.EntityType;

public interface StackerSupport {
    int getExtraBlocks(Island island, XMaterial material);

    int getExtraSpawners(Island island, EntityType entityType);
}
