package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.shop.ShopCategory;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * GUI which shows all categories of the shop.
 */
public class ShopOverviewGUI extends GUI {

    /**
     * Get the object's inventory.
     *
     * @return The inventory.
     */

    public ShopOverviewGUI(){
        super(islandMenu);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getShop().overviewSize, StringUtils.color(IridiumSkyblock.getInstance().getShop().overviewTitle));

        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> addContent(inventory));

        return inventory;
    }

    /**
     * Called when updating the Inventories contents
     */
    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();

        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getShop().overviewBackground);
        backItem(this, inventory);
        for (ShopCategory category : IridiumSkyblock.getInstance().getShopManager().getCategories()) {
            inventory.setItem(category.item.slot, ItemStackUtils.makeItem(category.item));
        }
    }

    /**
     * Called when there is a click in this GUI. Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        backItem(this, event);
        IridiumSkyblock.getInstance().getShopManager().getCategoryBySlot(event.getSlot()).ifPresent(shopCategory -> {
            String command = IridiumSkyblock.getInstance().getCommands().shopCommand.aliases.get(0);
            Bukkit.dispatchCommand(event.getWhoClicked(), "is " + command + " " + shopCategory.name);

        });
    }

}
