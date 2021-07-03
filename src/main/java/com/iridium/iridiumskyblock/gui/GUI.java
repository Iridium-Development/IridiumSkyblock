package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.configs.inventories.NoItemGUI;
import com.iridium.iridiumskyblock.database.Island;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a clickable GUI.
 * Base for all other classes in this package.
 */
@NoArgsConstructor
public abstract class GUI implements InventoryHolder {

    private NoItemGUI noItemGUI;
    private @Nullable Island island;

    /**
     * The default constructor.
     *
     * @param noItemGUI The NoItemGUI of this GUI
     * @param island    The island of this GUI. Can be null
     */
    public GUI(@NotNull NoItemGUI noItemGUI, @Nullable Island island) {
        this.noItemGUI = noItemGUI;
        this.island = island;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        String title = island == null ? noItemGUI.title : StringUtils.processMultiplePlaceholders(noItemGUI.title, new PlaceholderBuilder().applyIslandPlaceholders(island).build());
        Inventory inventory = Bukkit.createInventory(this, noItemGUI.size, StringUtils.color(title));

        addContent(inventory);

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

    public Island getIsland() {
        return island;
    }
}
