package com.iridium.iridiumskyblock.support;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBlocks;
import com.iridium.iridiumskyblock.database.IslandSpawners;
import dev.rosewood.rosestacker.api.RoseStackerAPI;
import dev.rosewood.rosestacker.stack.StackedBlock;
import dev.rosewood.rosestacker.stack.StackedSpawner;

public class RoseStackerSupport implements StackerSupport {
    @Override
    public void applyStackedBlockValue(Island island) {
        for (StackedBlock stackedBlock : RoseStackerAPI.getInstance().getStackedBlocks().values()) {
            if (!island.isInIsland(stackedBlock.getLocation())) continue;

            IslandBlocks islandBlock = IridiumSkyblock.getInstance().getIslandManager().getIslandBlock(island, XMaterial.matchXMaterial(stackedBlock.getBlock().getType()));
            islandBlock.setAmount(islandBlock.getAmount() + stackedBlock.getStackSize() - 1);
        }

        for (StackedSpawner stackedSpawner : RoseStackerAPI.getInstance().getStackedSpawners().values()) {
            if (!island.isInIsland(stackedSpawner.getLocation())) continue;

            IslandSpawners islandSpawners = IridiumSkyblock.getInstance().getIslandManager().getIslandSpawners(island, stackedSpawner.getSpawner().getSpawnedType());
            islandSpawners.setAmount(islandSpawners.getAmount() + stackedSpawner.getStackSize() - 1);
        }
    }
}
