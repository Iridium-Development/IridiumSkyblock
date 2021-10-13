package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.configs.inventories.NoItemGUI;
import com.iridium.iridiumskyblock.database.Island;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Represents a clickable GUI.
 * Base for all other classes in this package.
 */
@NoArgsConstructor
public abstract class GUI implements InventoryHolder {

    private NoItemGUI noItemGUI;
    public ItemStack backItem = ItemStackUtils.makeItem(new Item(XMaterial.BARRIER, 1,StringUtils.color("&c&lBack"), new ArrayList<>()));

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

    public NoItemGUI getNoItemGUI() {
        return noItemGUI;
    }
}
