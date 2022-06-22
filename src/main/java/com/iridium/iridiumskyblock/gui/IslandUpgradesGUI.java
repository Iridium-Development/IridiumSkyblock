package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Upgrade;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.upgrades.UpgradeData;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GUI which allows users to manage the Island Upgrades.
 */
public class IslandUpgradesGUI extends IslandGUI {

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public IslandUpgradesGUI(@NotNull Island island, Inventory previousInventory) {
        super(IridiumSkyblock.getInstance().getInventories().upgradesGUI, previousInventory, island);
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().upgradesGUI.background);

        for (Map.Entry<String, Upgrade<?>> upgrade : IridiumSkyblock.getInstance().getUpgradesList().entrySet()) {
            Item item = upgrade.getValue().item;
            int level = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(getIsland(), upgrade.getKey()).getLevel();
            List<Placeholder> placeholderList = new ArrayList<>();
            placeholderList.add(new Placeholder("level", String.valueOf(level)));

            if (upgrade.getValue().upgrades.get(level) != null) {
                UpgradeData upgradeData = upgrade.getValue().upgrades.get(level);
                placeholderList.addAll(upgradeData.getPlaceholders());
            }
            if (upgrade.getValue().upgrades.get(level + 1) != null) {
                UpgradeData upgradeData = upgrade.getValue().upgrades.get(level + 1);
                placeholderList.add(new Placeholder("crystalscost", String.valueOf(upgradeData.crystals)));
                placeholderList.add(new Placeholder("vaultcost", String.valueOf(upgradeData.money)));
            } else if (!upgrade.getValue().upgrades.containsKey(level + 1)) {
                placeholderList.add(new Placeholder("crystalscost", IridiumSkyblock.getInstance().getPlaceholders().crystalCost));
                placeholderList.add(new Placeholder("vaultcost", IridiumSkyblock.getInstance().getPlaceholders().vaultCost));
            }

            inventory.setItem(item.slot, ItemStackUtils.makeItem(item, placeholderList));
        }

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
        for (Map.Entry<String, Upgrade<?>> upgrade : IridiumSkyblock.getInstance().getUpgradesList().entrySet()) {
            if (event.getSlot() == upgrade.getValue().item.slot) {
                if (upgrade.getKey().equals("blocklimit") && event.getClick().isRightClick()) {
                    event.getWhoClicked().openInventory(new BlockLimitSelectLevelGUI(event.getWhoClicked().getOpenInventory().getTopInventory()).getInventory());
                } else {
                    IridiumSkyblock.getInstance().getCommands().upgradesCommand.execute(event.getWhoClicked(), new String[]{"", upgrade.getKey()});
                    addContent(event.getInventory());
                }
            }
        }
    }
}
