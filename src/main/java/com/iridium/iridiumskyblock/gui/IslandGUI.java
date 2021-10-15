package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.configs.inventories.NoItemGUI;
import com.iridium.iridiumskyblock.database.Island;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a clickable GUI.
 * Base for all other classes in this package.
 */
@NoArgsConstructor
public abstract class IslandGUI extends GUI {

    private @NotNull Island island;

    /**
     * The default constructor.
     *
     * @param noItemGUI The NoItemGUI of this GUI
     * @param island    The island of this GUI. Can be null
     */
    public IslandGUI(@NotNull NoItemGUI noItemGUI, @NotNull Island island, @Nullable Inventory backInventory) {
        super(noItemGUI, backInventory);
        this.island = island;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        String title = StringUtils.processMultiplePlaceholders(getNoItemGUI().title, new PlaceholderBuilder().applyIslandPlaceholders(island).build());
        Inventory inventory = Bukkit.createInventory(this, getNoItemGUI().size, StringUtils.color(title));

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

    @NotNull
    public Island getIsland() {
        return island;
    }
}
