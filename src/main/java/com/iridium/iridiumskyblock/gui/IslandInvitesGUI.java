package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.gui.PagedGUI;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.configs.inventories.NoItemGUI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandInvite;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

/**
 * GUI which allows users to manage invites.
 */
public class IslandInvitesGUI extends PagedGUI<IslandInvite> {

    private final Island island;

    public IslandInvitesGUI(Island island, Inventory previousInventory) {
        super(1,
                IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.size,
                IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.background,
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
        NoItemGUI noItemGUI = IridiumSkyblock.getInstance().getInventories().islandInvitesGUI;
        Inventory inventory = Bukkit.createInventory(this, getSize(), StringUtils.color(noItemGUI.title));
        addContent(inventory);
        return inventory;
    }

    @Override
    public Collection<IslandInvite> getPageObjects() {
        return IridiumSkyblock.getInstance().getDatabaseManager().getIslandInviteTableManager().getEntries(island);
    }

    @Override
    public ItemStack getItemStack(IslandInvite islandInvite) {
        List<Placeholder> placeholderList = new PlaceholderBuilder()
                .applyPlayerPlaceholders(islandInvite.getUser())
                .applyIslandPlaceholders(island)
                .build();
        placeholderList.add(new Placeholder("inviter", islandInvite.getInviter().getName()));
        placeholderList.add(new Placeholder("time", islandInvite.getTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat))));
        return ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.item, placeholderList);
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
        IslandInvite islandInvite = getItem(event.getSlot());
        if (islandInvite == null) return;
        IridiumSkyblock.getInstance().getCommands().unInviteCommand.execute(event.getWhoClicked(), new String[]{"", islandInvite.getUser().getName()});
        addContent(event.getInventory());
    }
}
