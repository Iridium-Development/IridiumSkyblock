package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBan;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Gui which displays all banned users of an Island
 */
public class IslandBansGUI extends IslandGUI {

    private final int page;
    private List<IslandBan> islandBans;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public IslandBansGUI(int page, @NotNull Island island) {
        super(IridiumSkyblock.getInstance().getInventories().bansGUI, island);
        this.page = page;
    }

    @Override
    public void addContent(Inventory inventory) {
        clearInventory(inventory);

        inventory.setItem(inventory.getSize() - 3, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().nextPage));
        inventory.setItem(inventory.getSize() - 7, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().previousPage));

        islandBans = new ArrayList<>(IridiumSkyblock.getInstance().getDatabaseManager().getIslandBanTableManager().getEntries(getIsland()));

        final long elementsPerPage = inventory.getSize() - 9;
        AtomicInteger slot = new AtomicInteger(0);

        islandBans.stream()
                .skip((page - 1) * elementsPerPage)
                .limit(elementsPerPage)
                .forEachOrdered(islandBan -> {
                    List<Placeholder> placeholderList = new PlaceholderBuilder().applyPlayerPlaceholders(islandBan.getBannedUser()).applyIslandPlaceholders(getIsland()).build();
                    placeholderList.add(new Placeholder("ban_time", islandBan.getBanTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat))));
                    placeholderList.add(new Placeholder("banned_by", islandBan.getBanner().getName()));
                    inventory.setItem(slot.getAndIncrement(), ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().bansGUI.item, placeholderList));
                });
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        final int size = IridiumSkyblock.getInstance().getInventories().bansGUI.size;
        Player player = (Player) event.getWhoClicked();
        if (isBackButton(event)) return;

        if (event.getSlot() == size - 7 && page > 1) {
            player.openInventory(new IslandBansGUI(page - 1, getIsland()).getInventory());
            return;
        }

        if (event.getSlot() == size - 3 && (size - 9) * page < islandBans.size()) {
            player.openInventory(new IslandBansGUI(page + 1, getIsland()).getInventory());
            return;
        }

        if (event.isLeftClick() && event.getSlot() + 1 <= islandBans.size()) {
            int index = ((size - 9) * (page - 1)) + event.getSlot();
            if (islandBans.size() > index) {
                IslandBan islandBan = islandBans.get(event.getSlot());
                String command = IridiumSkyblock.getInstance().getCommands().unBanCommand.aliases.get(0);
                Bukkit.getServer().dispatchCommand(player, "is " + command + " " + islandBan.getBannedUser().getName());
                addContent(event.getInventory());
            }
        }
    }
}
