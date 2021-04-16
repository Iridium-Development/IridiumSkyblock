package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.ConfirmationInventoryConfig;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * GUI which executes code upon confirmation.
 */
public class ConfirmationGUI implements GUI {

    private final @NotNull Runnable runnable;

    /**
     * The default constructor.
     *
     * @param runnable The code that should be run when the user confirms his action
     */
    public ConfirmationGUI(@NotNull Runnable runnable) {
        this.runnable = runnable;
    }

    /**
     * Builds and returns this inventory.
     *
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        ConfirmationInventoryConfig confirmationInventoryConfig = IridiumSkyblock.getInstance().getInventories().confirmationGUI;
        Inventory inventory = Bukkit.createInventory(this, confirmationInventoryConfig.size, StringUtils.color(confirmationInventoryConfig.title));

        InventoryUtils.fillInventory(inventory);

        inventory.setItem(11, ItemStackUtils.makeItem(confirmationInventoryConfig.no));
        inventory.setItem(15, ItemStackUtils.makeItem(confirmationInventoryConfig.yes));
        return inventory;
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() == 11) {
            event.getWhoClicked().closeInventory();
        } else if (event.getSlot() == 15) {
            runnable.run();
            event.getWhoClicked().closeInventory();
        }
    }
}
