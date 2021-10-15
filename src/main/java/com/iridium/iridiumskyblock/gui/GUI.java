package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.configs.inventories.NoItemGUI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Represents a clickable GUI.
 * Base for all other classes in this package.
 */
@NoArgsConstructor
public abstract class GUI implements InventoryHolder {

    private NoItemGUI noItemGUI;

    private Inventory backInventory;

    public static Inventory islandMenu = new InventoryConfigGUI(IridiumSkyblock.getInstance().getInventories().islandMenu, null).getInventory();


    /**
     * The default constructor.
     *
     * @param noItemGUI The NoItemGUI of this GUI
     */
    public GUI(@NotNull NoItemGUI noItemGUI, @Nullable Inventory backInventory) {
        this.noItemGUI = noItemGUI;
        this.backInventory = backInventory;
    }

    public GUI(@Nullable Inventory backInventory) {
        this.backInventory = backInventory;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, noItemGUI.size, StringUtils.color(noItemGUI.title));

        Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> addContent(inventory));

        return inventory;
    }

    public static void backItem(GUI islandGUI, Inventory inventory){
        if(islandGUI.backInventory != null){
            inventory.setItem(inventory.getSize() - 5, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().backPage));
        }
    }

    public static void backItem(GUI islandGUI, InventoryClickEvent event){
        if(islandGUI.backInventory != null && event.getSlot() == islandGUI.getInventory().getSize() - 5){
            User user = IridiumSkyblock.getInstance().getUserManager().getUser((Player) event.getWhoClicked());
            Optional<Island> island = user.getIsland();
            if(island.isPresent()) {
                event.getWhoClicked().openInventory(islandGUI.backInventory);
            }
            return;
        }
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

    public NoItemGUI getNoItemGUI() {
        return noItemGUI;
    }
}
