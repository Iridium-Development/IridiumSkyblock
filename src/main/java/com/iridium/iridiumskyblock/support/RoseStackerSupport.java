package com.iridium.iridiumskyblock.support;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.database.Island;
import dev.rosewood.rosestacker.api.RoseStackerAPI;
import dev.rosewood.rosestacker.stack.StackedSpawner;
import org.bukkit.block.CreatureSpawner;

import java.util.List;
import java.util.stream.Collectors;

public class RoseStackerSupport implements SpawnerStackerSupport, BlockStackerSupport {

    public int getSpawnerAmount(CreatureSpawner spawner) {
        StackedSpawner stackedSpawner = RoseStackerAPI.getInstance().getStackedSpawner(spawner.getBlock());
        if (stackedSpawner == null) return 1;
        return stackedSpawner.getStackSize();
    }

    @Override
    public List<BlockAmount> getBlockAmounts(Island island) {
        return RoseStackerAPI.getInstance().getStackedBlocks().values().stream()
                .filter(stackedBlock -> island.isInIsland(stackedBlock.getLocation()))
                .map(stackedBlock ->
                        new BlockAmount(stackedBlock.getStackSize() - 1, XMaterial.matchXMaterial(stackedBlock.getBlock().getType()))
                ).collect(Collectors.toList());
    }
}
