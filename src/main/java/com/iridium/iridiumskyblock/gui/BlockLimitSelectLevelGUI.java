package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.upgrades.BlockLimitUpgrade;
import com.iridium.iridiumskyblock.upgrades.BlockLimitUpgrade.LimitedBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;

/**
 * GUI which shows the block limits.
 *
 * @see LimitedBlock
 */
public class BlockLimitSelectLevelGUI extends GUI {

    private int page = 1;

    /**
     * The default constructor.
     */
    public BlockLimitSelectLevelGUI(Inventory previousInventory) {
        super(IridiumSkyblock.getInstance().getInventories().blockLimit, previousInventory);
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();

        InventoryUtils.fillInventory(inventory, getNoItemGUI().background);

        inventory.setItem(inventory.getSize() - 3, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().nextPage));
        inventory.setItem(inventory.getSize() - 7, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().previousPage));

        IridiumSkyblock.getInstance().getUpgrades().blockLimitUpgrade.upgrades.values().stream()
                .filter(limitBlockEntry -> (page == 1 ? limitBlockEntry.page <= 1 : limitBlockEntry.page == page))
                .forEach(value -> {
                    ItemStack item = ItemStackUtils.makeItem(value.item);
                    inventory.setItem(value.item.slot, item);
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
        Optional<Map.Entry<Integer, BlockLimitUpgrade>> level = IridiumSkyblock.getInstance().getUpgrades().blockLimitUpgrade.upgrades.entrySet().stream()
                .filter(levelEntry -> levelEntry.getValue().item.slot == event.getSlot())
                .filter(levelEntry -> (page == 1 ? levelEntry.getValue().page <= 1 : levelEntry.getValue().page == page))
                .findAny();
        if (level.isPresent()) {
            player.openInventory(new BlockLimitGUI(level.get().getKey(), player.getOpenInventory().getTopInventory()).getInventory());
            return;
        }
        if (event.getSlot() == getNoItemGUI().size - 7 && page > 1) {
            page--;
            player.openInventory(getInventory());
            return;
        }
        boolean nextPage = IridiumSkyblock.getInstance().getUpgrades().blockLimitUpgrade.upgrades.values().stream().anyMatch(valueEntry -> valueEntry.page == page + 1);
        if (event.getSlot() == getNoItemGUI().size - 3 && nextPage) {
            page++;
            player.openInventory(getInventory());
        }
    }

}