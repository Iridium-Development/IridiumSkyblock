package com.iridium.iridiumskyblock.multiversion;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;

public class V1_11_R1 implements Multiversion {
    @Override
    public XMaterial getBlock(ChunkSnapshot chunk, int x, int y, int z) {
        return XMaterial.matchXMaterial(Material.getMaterial(chunk.getBlockTypeId(x, y, z)));
    }
}
