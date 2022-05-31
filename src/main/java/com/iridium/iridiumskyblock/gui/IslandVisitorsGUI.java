package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.gui.PagedGUI;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.configs.inventories.NoItemGUI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * GUI which displays players on island
 */
public class IslandVisitorsGUI extends PagedGUI<User> {

    private final Island island;

    public IslandVisitorsGUI(Island island, Inventory previousInventory) {
        super(1,
                IridiumSkyblock.getInstance().getInventories().visitorsGUI.size,
                IridiumSkyblock.getInstance().getInventories().visitorsGUI.background,
                IridiumSkyblock.getInstance().getInventories().previousPage,
                IridiumSkyblock.getInstance().getInventories().nextPage,
                previousInventory,
                IridiumSkyblock.getInstance().getInventories().backButton
        );
        this.island = island;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        NoItemGUI noItemGUI = IridiumSkyblock.getInstance().getInventories().visitorsGUI;
        Inventory inventory = Bukkit.createInventory(this, getSize(), StringUtils.color(noItemGUI.title));
        addContent(inventory);
        return inventory;
    }

    @Override
    public Collection<User> getPageObjects() {
        return IridiumSkyblock.getInstance().getIslandManager().getPlayersOnIsland(island);
    }

    @Override
    public ItemStack getItemStack(User user) {
        List<Placeholder> placeholderList = new PlaceholderBuilder()
                .applyPlayerPlaceholders(user)
                .applyIslandPlaceholders(island)
                .build();
        return ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().visitorsGUI.item, placeholderList);
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        super.onInventoryClick(event);
        User visitor = getItem(event.getSlot());
        if (visitor == null) return;
        if (event.isLeftClick()) {
            IridiumSkyblock.getInstance().getCommands().expelCommand.execute(event.getWhoClicked(), new String[]{"", visitor.getName()});
        } else if (event.isRightClick()) {
            IridiumSkyblock.getInstance().getCommands().banCommand.execute(event.getWhoClicked(), new String[]{"", visitor.getName()});
        }
        addContent(event.getInventory());
    }
}
