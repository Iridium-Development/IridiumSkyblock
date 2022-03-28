package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.gui.PagedGUI;
import com.iridium.iridiumcore.utils.ItemStackUtils;
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
import java.util.stream.Collectors;

/**
 * GUI which shows a list of all Islands a user can visit.
 */
public class VisitGUI extends PagedGUI<Island> {

    private final User viewer;

    public VisitGUI(User viewer, Inventory previousInventory) {
        super(1,
                IridiumSkyblock.getInstance().getInventories().visitGUI.size,
                IridiumSkyblock.getInstance().getInventories().visitGUI.background,
                IridiumSkyblock.getInstance().getInventories().previousPage,
                IridiumSkyblock.getInstance().getInventories().nextPage,
                previousInventory,
                IridiumSkyblock.getInstance().getInventories().backButton
        );
        this.viewer = viewer;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        NoItemGUI noItemGUI = IridiumSkyblock.getInstance().getInventories().visitGUI;
        Inventory inventory = Bukkit.createInventory(this, getSize(), StringUtils.color(noItemGUI.title));
        addContent(inventory);
        return inventory;
    }

    @Override
    public Collection<Island> getPageObjects() {
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandTableManager().getEntries().stream()
                .filter(island -> viewer.isBypassing() || island.isVisitable())
                .collect(Collectors.toList());
    }

    @Override
    public ItemStack getItemStack(Island island) {
        return ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().visitGUI.item, new PlaceholderBuilder().applyIslandPlaceholders(island).build());
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Island island = getItem(event.getSlot());
        if (island == null) return;
        IridiumSkyblock.getInstance().getCommands().visitCommand.execute(event.getWhoClicked(), new String[]{"", island.getOwner().getName()});
        event.getWhoClicked().closeInventory();
    }

}
