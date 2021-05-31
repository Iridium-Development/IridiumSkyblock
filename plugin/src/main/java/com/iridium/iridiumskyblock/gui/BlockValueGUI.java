package com.iridium.iridiumskyblock.gui;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.BlockValues.ValuableBlock;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GUI which shows the value of valuable blocks.
 *
 * @see ValuableBlock
 */
public class BlockValueGUI implements GUI {

    private final BlockValueType guiType;

    /**
     * The default constructor.
     *
     * @param type The type of valuable block shown in this GUI
     */
    public BlockValueGUI(BlockValueType type) {
        this.guiType = type;
    }

    /**
     * Builds and returns this inventory.
     *
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().blockValue.size, StringUtils.color(IridiumSkyblock.getInstance().getInventories().blockValue.title));

        addContent(inventory);

        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();

        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().blockValue.background);

        if (guiType == BlockValueType.BLOCK) {
            for (Map.Entry<XMaterial, ValuableBlock> valuableBlock : IridiumSkyblock.getInstance().getBlockValues().blockValues.entrySet()) {
                XMaterial material = valuableBlock.getKey();
                ValuableBlock blockInfo = valuableBlock.getValue();
                ItemStack blockItem = ItemStackUtils.makeItem(material, 1, StringUtils.color(blockInfo.name), getColoredValueLore(blockInfo.value));
                inventory.setItem(blockInfo.slot, blockItem);
            }
        } else if (guiType == BlockValueType.SPAWNER) {
            for (Map.Entry<EntityType, ValuableBlock> valuableSpawner : IridiumSkyblock.getInstance().getBlockValues().spawnerValues.entrySet()) {
                EntityType spawnerType = valuableSpawner.getKey();
                ValuableBlock spawnerInfo = valuableSpawner.getValue();
                ItemStack spawnerItem = ItemStackUtils.makeItem(XMaterial.SPAWNER, 1, StringUtils.color(spawnerInfo.name), getColoredValueLore(spawnerInfo.value));
                inventory.setItem(spawnerInfo.slot, spawnerItem);
            }
        }
    }

    private List<String> getColoredValueLore(double value) {
        return IridiumSkyblock.getInstance().getInventories().blockValue.lore.stream()
                .map(StringUtils::color)
                .map(line -> line.replace("%value%", String.valueOf(value)))
                .collect(Collectors.toList());
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        // Don't do anything here, it gets cancelled automatically
    }

    /**
     * Represents a category of blocks that are valuable.
     */
    public enum BlockValueType {
        BLOCK,
        SPAWNER;

        /**
         * Returns the category of valuable blocks with the provided name, null if there is none.
         * Case insensitive.
         *
         * @param type The type name which should be parsed
         * @return The block value type, null if there is none
         */
        public static BlockValueType getType(String type) {
            return Arrays.stream(BlockValueType.values()).filter(blockValueType -> blockValueType.name().equalsIgnoreCase(type)).findFirst().orElse(null);
        }
    }

}
