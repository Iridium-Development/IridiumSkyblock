package com.iridium.iridiumskyblock.support;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBlocks;
import com.iridium.iridiumskyblock.database.IslandSpawners;
import dev.rosewood.rosestacker.api.RoseStackerAPI;
import dev.rosewood.rosestacker.stack.StackedBlock;
import dev.rosewood.rosestacker.stack.StackedSpawner;
import org.bukkit.entity.EntityType;

public class RoseStackerSupport implements StackerSupport {

    @Override
    public int getExtraBlocks(Island island, XMaterial material) {
        IslandBlocks islandBlocks = IridiumSkyblock.getInstance().getIslandManager().getIslandBlock(island, material);
        int stackedBlocks = 0;
        for (StackedBlock stackedBlock : RoseStackerAPI.getInstance().getStackedBlocks().values()) {
            if (!island.isInIsland(stackedBlock.getLocation())) continue;
            if (material != XMaterial.matchXMaterial(stackedBlock.getBlock().getType())) continue;
            stackedBlocks += (stackedBlock.getStackSize() - 1);
        }
        islandBlocks.setExtraAmount(stackedBlocks);
        return stackedBlocks;
    }

    @Override
    public int getExtraSpawners(Island island, EntityType entityType) {
        IslandSpawners islandSpawners = IridiumSkyblock.getInstance().getIslandManager().getIslandSpawners(island, entityType);
        int stackedSpawners = 0;
        for (StackedSpawner stackedSpawner : RoseStackerAPI.getInstance().getStackedSpawners().values()) {
            if (!island.isInIsland(stackedSpawner.getLocation())) continue;
            if (stackedSpawner.getSpawner().getSpawnedType() != entityType) continue;
            stackedSpawners += (stackedSpawner.getStackSize() - 1);
        }
        islandSpawners.setExtraAmount(stackedSpawners);
        return stackedSpawners;
    }
}
