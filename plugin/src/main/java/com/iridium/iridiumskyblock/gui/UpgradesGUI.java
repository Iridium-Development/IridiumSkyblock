package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Item;
import com.iridium.iridiumskyblock.Upgrade;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.upgrades.UpgradeData;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.Placeholder;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GUI which allows users to manage the Island Upgrades.
 */
public class UpgradesGUI implements GUI {

    private final Island island;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public UpgradesGUI(@NotNull Island island) {
        this.island = island;
    }

    /**
     * Builds and returns this inventory.
     *
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().upgradesGUI.size,
                StringUtils.color(IridiumSkyblock.getInstance().getInventories().upgradesGUI.title));

        addContent(inventory);

        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().upgradesGUI.background);

        for (Map.Entry<String, Upgrade> upgrade : IridiumSkyblock.getInstance().getUpgradesList().entrySet()) {
            Item item = upgrade.getValue().item;
            int level = IridiumSkyblock.getInstance().getIslandManager().getIslandUpgrade(island, upgrade.getKey()).getLevel();
            List<Placeholder> placeholderList = new ArrayList<>();
            placeholderList.add(new Placeholder("level", String.valueOf(level)));

            if (upgrade.getValue().upgrades.get(level) instanceof UpgradeData) {
                UpgradeData upgradeData = (UpgradeData) upgrade.getValue().upgrades.get(level);
                placeholderList.addAll(upgradeData.getPlaceholders());
            }
            if (upgrade.getValue().upgrades.get(level + 1) instanceof UpgradeData) {
                UpgradeData upgradeData = (UpgradeData) upgrade.getValue().upgrades.get(level + 1);
                placeholderList.add(new Placeholder("crystalscost", String.valueOf(upgradeData.crystals)));
                placeholderList.add(new Placeholder("vaultcost", String.valueOf(upgradeData.money)));
            } else if (!upgrade.getValue().upgrades.containsKey(level + 1)) {
                placeholderList.add(new Placeholder("crystalscost", IridiumSkyblock.getInstance().getConfiguration().placeholderDefaultValue));
                placeholderList.add(new Placeholder("vaultcost", IridiumSkyblock.getInstance().getConfiguration().placeholderDefaultValue));

            }
            inventory.setItem(item.slot, ItemStackUtils.makeItem(item, placeholderList));
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
        for (Map.Entry<String, Upgrade> upgrade : IridiumSkyblock.getInstance().getUpgradesList().entrySet()) {
            if (event.getSlot() == upgrade.getValue().item.slot) {
                String command = IridiumSkyblock.getInstance().getCommands().upgradesCommand.aliases.get(0);
                Bukkit.dispatchCommand(event.getWhoClicked(), "is " + command + " " + upgrade.getKey());
            }
        }
    }
}
