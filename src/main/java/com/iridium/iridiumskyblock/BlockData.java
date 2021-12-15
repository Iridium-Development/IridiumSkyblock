package com.iridium.iridiumskyblock;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the data of a block.
 * Used for our own schematic system.
 */
@Getter
@NoArgsConstructor
public class BlockData {

    private XMaterial material;
    private org.bukkit.block.data.BlockData data;
    private List<String> inventory;

    /**
     * The default constructor.
     *
     * @param block The block whose data should be represented by this class
     */
    public BlockData(Block block) {
        this.material = XMaterial.matchXMaterial(block.getType());
        this.data = block.getBlockData();

        if (block.getState() instanceof InventoryHolder inventoryHolder) {
            this.inventory = Arrays.stream(inventoryHolder.getInventory().getContents()).map(item -> item != null ? ItemStackUtils.serialize(item) : null).collect(Collectors.toList());
        }
    }

    /**
     * Applies block data of the schematic to the specified block.
     *
     * @param block The block whose data should be updated
     */
    public void setBlock(Block block) {
        BlockState blockState = block.getState();
        Material mat = material.parseMaterial();
        if (mat != null) {
            blockState.setType(mat);
            blockState.setBlockData(data);
            blockState.update(true, true);

            // We gotta create a new BlockState because the old one is still air and wont be instance of container
            if (block.getState() instanceof InventoryHolder inventoryHolder && inventory != null) {
                inventoryHolder.getInventory().setContents(inventory.stream().map(item -> item != null ? ItemStackUtils.deserialize(item) : null).toArray(ItemStack[]::new));
            }
        }
    }

}
