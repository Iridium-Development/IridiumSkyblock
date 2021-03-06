package com.iridium.iridiumskyblock;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

@Getter
@NoArgsConstructor
public class BlockData {
    private Material material;
    private byte data;

    /**
     * Applies block data of the schematic to the specified block.
     *
     * @param block The block whose data should be updated
     */
    public void setBlock(Block block) {
        BlockState blockState = block.getState();
        blockState.setType(material);
        blockState.setRawData(data);
        blockState.update(true, false);
    }

    public BlockData(Block block) {
        this.material = block.getType();
        this.data = block.getData();
    }

}
