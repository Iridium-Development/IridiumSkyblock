package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.upgrades.BlockLimitUpgrade.LimitedBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

/**
 * GUI which shows block limits.
 *
 * @see LimitedBlock
 */
public class BlockLimitGUI extends GUI {

    private final int level;
    private int page = 1;

    /**
     * The default constructor.
     */
    public BlockLimitGUI(int level, Inventory previousInventory) {
        super(IridiumSkyblock.getInstance().getInventories().blockLimit, previousInventory);
        this.level = level;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();

        InventoryUtils.fillInventory(inventory, getNoItemGUI().background);

        inventory.setItem(inventory.getSize() - 3, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().nextPage));
        inventory.setItem(inventory.getSize() - 7, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().previousPage));

        IridiumSkyblock.getInstance().getUpgrades().blockLimitUpgrade.upgrades.get(level).limits.entrySet().stream()
                .filter(limitedBlockEntry -> (page == 1 ? limitedBlockEntry.getValue().page <= 1 : limitedBlockEntry.getValue().page == page))
                .forEachOrdered(limitedBlock -> {
                    XMaterial material = limitedBlock.getKey();
                    LimitedBlock limitInfo = limitedBlock.getValue();
                    ItemStack blockItem = ItemStackUtils.makeItem(material, 1, StringUtils.color(limitInfo.name), getColoredValueLore(limitInfo.value));
                    inventory.setItem(limitInfo.slot, blockItem);
                });

        if (IridiumSkyblock.getInstance().getConfiguration().backButtons && getPreviousInventory() != null) {
            inventory.setItem(inventory.getSize() + IridiumSkyblock.getInstance().getInventories().backButton.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().backButton));
        }
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
            page--;
            player.openInventory(getInventory());
            return;
        }
        boolean nextPage = IridiumSkyblock.getInstance().getUpgrades().blockLimitUpgrade.upgrades.get(level).limits.values().stream().anyMatch(valueEntry -> valueEntry.page == page +1);
        if (event.getSlot() == getNoItemGUI().size - 3 && nextPage) {
            page++;
            player.openInventory(getInventory());
        }
    }

    private List<String> getColoredValueLore(int value) {
        return IridiumSkyblock.getInstance().getInventories().blockLimit.lore.stream()
                .map(StringUtils::color)
                .map(line -> line.replace("%value%", String.valueOf(value)))
                .collect(Collectors.toList());
    }

}
