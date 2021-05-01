package com.iridium.iridiumskyblock.multiversion;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChunkSnapshot;

public interface Multiversion {
    XMaterial getBlock(ChunkSnapshot chunk, int x, int y, int z);
}
