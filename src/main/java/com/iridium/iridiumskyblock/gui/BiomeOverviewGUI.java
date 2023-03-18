package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.biomes.BiomeCategory;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * GUI which shows all categories of the biomes menu.
 */
public class BiomeOverviewGUI extends GUI {

    public BiomeOverviewGUI(Inventory previousInventory) {
        super(previousInventory);
    }

    /**
     * Get the object's inventory.
     *
     * @return The inventory.
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getBiomes().overviewSize, StringUtils.color(IridiumSkyblock.getInstance().getBiomes().overviewTitle));

        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> addContent(inventory));

        return inventory;
    }

    /**
     * Called when updating the Inventories contents
     */
    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();

        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getBiomes().overviewBackground);

        for (BiomeCategory category : IridiumSkyblock.getInstance().getBiomesManager().getCategories()) {
            inventory.setItem(category.item.slot, ItemStackUtils.makeItem(category.item));
        }

        if (IridiumSkyblock.getInstance().getConfiguration().backButtons && getPreviousInventory() != null) {
            inventory.setItem(inventory.getSize() + IridiumSkyblock.getInstance().getInventories().backButton.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().backButton));
        }
    }

    /**
     * Called when there is a click in this GUI. Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        IridiumSkyblock.getInstance().getBiomesManager().getCategoryBySlot(event.getSlot()).ifPresent(biomeCategory ->
                IridiumSkyblock.getInstance().getCommands().biomeCommand.execute(event.getWhoClicked(), new String[]{"", biomeCategory.name})
        );
    }

}
