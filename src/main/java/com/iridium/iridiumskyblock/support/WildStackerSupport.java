package com.iridium.iridiumskyblock.support;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.bgsoftware.wildstacker.api.objects.StackedBarrel;
import com.bgsoftware.wildstacker.api.objects.StackedSpawner;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.entity.EntityType;

public class WildStackerSupport implements StackerSupport {

    @Override
    public int getExtraBlocks(Island island, XMaterial material) {
        int stackedBlocks = 0;
        for (StackedBarrel stackedBarrel : WildStackerAPI.getWildStacker().getSystemManager().getStackedBarrels()) {
            if (!island.isInIsland(stackedBarrel.getLocation())) continue;
            if (material != XMaterial.matchXMaterial(stackedBarrel.getType())) continue;

            if (material == XMaterial.matchXMaterial(stackedBarrel.getType())) {
                stackedBlocks--;
            } else {
                stackedBlocks += stackedBarrel.getStackAmount();
            }
        }
        return stackedBlocks;
    }

    @Override
    public int getExtraSpawners(Island island, EntityType entityType) {
        int stackedSpawners = 0;
        for (StackedSpawner stackedSpawner : WildStackerAPI.getWildStacker().getSystemManager().getStackedSpawners()) {
            if (!island.isInIsland(stackedSpawner.getLocation())) continue;
            if (stackedSpawner.getSpawnedType() != entityType) continue;
            stackedSpawners += stackedSpawner.getStackAmount();
        }
        return stackedSpawners;
    }
}
