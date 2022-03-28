package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.gui.PagedGUI;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.configs.inventories.NoItemGUI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandTrusted;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * GUI which displays all trusted users of an Island.
 */
public class IslandTrustedGUI extends PagedGUI<IslandTrusted> {

    private final Island island;

    public IslandTrustedGUI(Island island, Inventory previousInventory) {
        super(1,
                IridiumSkyblock.getInstance().getInventories().trustedGUI.size,
                IridiumSkyblock.getInstance().getInventories().trustedGUI.background,
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
        NoItemGUI noItemGUI = IridiumSkyblock.getInstance().getInventories().trustedGUI;
        Inventory inventory = Bukkit.createInventory(this, getSize(), StringUtils.color(noItemGUI.title));
        addContent(inventory);
        return inventory;
    }

    @Override
    public Collection<IslandTrusted> getPageObjects() {
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager().getEntries(island);
    }

    @Override
    public ItemStack getItemStack(IslandTrusted islandTrusted) {
        List<Placeholder> placeholderList = new PlaceholderBuilder()
                .applyPlayerPlaceholders(islandTrusted.getUser())
                .applyIslandPlaceholders(island)
                .build();
        placeholderList.add(new Placeholder("trustee", islandTrusted.getTruster().getName()));
        return ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().trustedGUI.item, placeholderList);
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        IslandTrusted islandTrusted = getItem(event.getSlot());
        if (islandTrusted == null) return;
        IridiumSkyblock.getInstance().getCommands().unTrustCommand.execute(event.getWhoClicked(), new String[]{"", islandTrusted.getUser().getName()});
        addContent(event.getInventory());
    }

}
