package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * GUI which executes code upon confirmation.
 */
public class ConfirmationGUI extends GUI {

    private final @NotNull Runnable runnable;

    /**
     * The default constructor.
     *
     * @param runnable The code that should be run when the user confirms his action
     */
    public ConfirmationGUI(@NotNull Runnable runnable) {
        super(IridiumSkyblock.getInstance().getInventories().confirmationGUI);
        this.runnable = runnable;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, getNoItemGUI().background);

        inventory.setItem(11, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().confirmationGUI.no));
        inventory.setItem(15, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().confirmationGUI.yes));
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
