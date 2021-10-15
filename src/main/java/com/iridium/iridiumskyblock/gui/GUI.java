package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.inventories.NoItemGUI;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a clickable GUI.
 * Base for all other classes in this package.
 */
@NoArgsConstructor
public abstract class GUI implements InventoryHolder {

    private NoItemGUI noItemGUI;

    /**
     * The default constructor.
     *
     * @param noItemGUI The NoItemGUI of this GUI
     */
    public GUI(@NotNull NoItemGUI noItemGUI) {
        this.noItemGUI = noItemGUI;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, noItemGUI.size, StringUtils.color(noItemGUI.title));

        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> addContent(inventory));

        return inventory;
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    public abstract void onInventoryClick(InventoryClickEvent event);

    /**
     * Called when updating the Inventories contents
     */
    public abstract void addContent(Inventory inventory);

    /**
     * Checks the clicked item is back button or not
     *
     * @param event The InventoryClickEvent provided by Bukkit
     * @return a boolean, clicked item is back button or not
     */
    public boolean isBackButton(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (getNoItemGUI().backButton != null && getNoItemGUI().backButton.enabled && getNoItemGUI().backButton.item.slot == event.getSlot() && !getNoItemGUI().backButton.command.isEmpty()) {
            player.closeInventory();
            Bukkit.dispatchCommand(player, getNoItemGUI().backButton.command);
            return true;
        }
        return false;
    }

    public void clearInventory(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, getNoItemGUI().background);
        if (getNoItemGUI().backButton != null && getNoItemGUI().backButton.enabled && getNoItemGUI().backButton.item != null) {
            inventory.setItem(getNoItemGUI().backButton.item.slot, ItemStackUtils.makeItem(getNoItemGUI().backButton.item));
        }
    }

    public NoItemGUI getNoItemGUI() {
        return noItemGUI;
    }

}
