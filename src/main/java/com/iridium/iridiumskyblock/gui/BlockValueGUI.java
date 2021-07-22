package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.BlockValues.ValuableBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GUI which shows the value of valuable blocks.
 *
 * @see ValuableBlock
 */
public class BlockValueGUI extends GUI {

    private final BlockValueType guiType;
    private int page;


    /**
     * The default constructor.
     *
     * @param type The type of valuable block shown in this GUI
     */
    public BlockValueGUI(int page, BlockValueType type) {
        super(IridiumSkyblock.getInstance().getInventories().blockValue, null);
        this.page = page;
        this.guiType = type;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();

        InventoryUtils.fillInventory(inventory, getNoItemGUI().background);

        inventory.setItem(inventory.getSize() - 3, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().nextPage));
        inventory.setItem(inventory.getSize() - 7, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().previousPage));

        if (guiType == BlockValueType.BLOCK) {
            IridiumSkyblock.getInstance().getBlockValues().blockValues.entrySet().stream()
                    .filter(valuableBlockEntry -> (page == 1 ? valuableBlockEntry.getValue().page <= 1 : valuableBlockEntry.getValue().page == page))
                    .forEachOrdered(valuableBlock -> {
                        XMaterial material = valuableBlock.getKey();
                        ValuableBlock blockInfo = valuableBlock.getValue();
                        ItemStack blockItem = ItemStackUtils.makeItem(material, 1, StringUtils.color(blockInfo.name), getColoredValueLore(blockInfo.value));
                        inventory.setItem(blockInfo.slot, blockItem);
                    });

        } else if (guiType == BlockValueType.SPAWNER) {
            IridiumSkyblock.getInstance().getBlockValues().spawnerValues.entrySet().stream()
                    .filter(valuableSpawnerEntry -> (page == 1 ? valuableSpawnerEntry.getValue().page <= 1 : valuableSpawnerEntry.getValue().page == page))
                    .forEachOrdered(valuableSpawner -> {
                        ValuableBlock spawnerInfo = valuableSpawner.getValue();
                        ItemStack spawnerItem = ItemStackUtils.makeItem(XMaterial.SPAWNER, 1, StringUtils.color(spawnerInfo.name), getColoredValueLore(spawnerInfo.value));
                        inventory.setItem(spawnerInfo.slot, spawnerItem);
                    });
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
        Player player = (Player) event.getWhoClicked();
        if (event.getSlot() == getNoItemGUI().size - 7 && page > 1) {
            page-=1;
            player.openInventory(getInventory());
            return;
        }
        boolean nextPage = (guiType == BlockValueType.SPAWNER ? IridiumSkyblock.getInstance().getBlockValues().spawnerValues : IridiumSkyblock.getInstance().getBlockValues().blockValues).entrySet().stream().anyMatch(valueEntry -> valueEntry.getValue().page == page + 1);
        if (event.getSlot() == getNoItemGUI().size - 3 && nextPage) {
            page+=1;
            player.openInventory(getInventory());
        }
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
