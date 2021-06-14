package com.iridium.iridiumskyblock.support;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.database.Island;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public interface BlockStackerSupport {
    List<BlockAmount> getBlockAmounts(Island island);

    @AllArgsConstructor
    @Getter
    public static class BlockAmount {
        private final int amount;
        private final XMaterial material;
    }
}