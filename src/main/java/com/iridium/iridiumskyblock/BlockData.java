package com.iridium.iridiumskyblock;

import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class BlockData {
    private Material material;
    private byte data;
    private List<String> inventory;

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
        //We gotta create a new blockstate because the old one is still air and wont be instance of container
        if (block.getState() instanceof Container && inventory != null) {
            Container container = (Container) block.getState();
            container.getInventory().setContents(inventory.stream().map(item -> item != null ? ItemStackUtils.deserialize(item) : null).toArray(ItemStack[]::new));
        }
    }

    public BlockData(Block block) {
        this.material = block.getType();
        this.data = block.getData();
        if (block.getState() instanceof Container) {
            Container container = (Container) block.getState();
            this.inventory = Arrays.stream(container.getInventory().getContents()).map(item -> item != null ? ItemStackUtils.serialize(item) : null).collect(Collectors.toList());
        }
    }

}
