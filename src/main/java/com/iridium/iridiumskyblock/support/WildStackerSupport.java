package com.iridium.iridiumskyblock.support;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.bgsoftware.wildstacker.api.objects.StackedBarrel;
import com.bgsoftware.wildstacker.api.objects.StackedSpawner;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBlocks;
import com.iridium.iridiumskyblock.database.IslandSpawners;

public class WildStackerSupport implements StackerSupport {

    @Override
    public void applyStackedBlockValue(Island island) {
        for (StackedBarrel stackedBarrel : WildStackerAPI.getWildStacker().getSystemManager().getStackedBarrels()) {
            if (!island.isInIsland(stackedBarrel.getLocation())) continue;

            IslandBlocks islandBlock = IridiumSkyblock.getInstance().getIslandManager().getIslandBlock(island, XMaterial.matchXMaterial(stackedBarrel.getBlock().getType()));
            islandBlock.setAmount(islandBlock.getAmount() - 1);

            IslandBlocks stackedBlock = IridiumSkyblock.getInstance().getIslandManager().getIslandBlock(island, XMaterial.matchXMaterial(stackedBarrel.getType()));
            stackedBlock.setAmount(islandBlock.getAmount() + stackedBlock.getAmount());
        }

        for (StackedSpawner stackedSpawner : WildStackerAPI.getWildStacker().getSystemManager().getStackedSpawners()) {
            if (!island.isInIsland(stackedSpawner.getLocation())) continue;

            IslandSpawners islandSpawners = IridiumSkyblock.getInstance().getIslandManager().getIslandSpawners(island, stackedSpawner.getSpawner().getSpawnedType());
            islandSpawners.setAmount(islandSpawners.getAmount() + stackedSpawner.getStackAmount() - 1);
        }
    }
}
