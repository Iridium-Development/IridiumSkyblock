package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GUI which displays players on island
 */
public class IslandVisitorsGUI extends IslandGUI {

    private final int page;
    private final List<User> visitors;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public IslandVisitorsGUI(int page, @NotNull Island island) {
        super(IridiumSkyblock.getInstance().getInventories().visitorsGUI, island, islandMenu);
        this.page = page;
        this.visitors = new ArrayList<>();
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, getNoItemGUI().background);

        inventory.setItem(inventory.getSize() - 3, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().nextPage));
        backItem(this, inventory);
        inventory.setItem(inventory.getSize() - 7, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().previousPage));

        final long elementsPerPage = inventory.getSize() - 9;
        AtomicInteger slot = new AtomicInteger(0);

        IridiumSkyblock.getInstance().getIslandManager().getPlayersOnIsland(getIsland()).stream()
                .filter(user -> user.getIsland().map(Island::getId).orElse(0) != getIsland().getId())
                .skip((page - 1) * elementsPerPage)
                .limit(elementsPerPage)
                .forEachOrdered(user -> {
                    visitors.add(user);
                    List<Placeholder> placeholderList = new PlaceholderBuilder().applyPlayerPlaceholders(user).applyIslandPlaceholders(getIsland()).build();
                    inventory.setItem(slot.getAndIncrement(), ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().visitorsGUI.item, placeholderList));
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
        backItem(this, event);
        final int size = IridiumSkyblock.getInstance().getInventories().bansGUI.size;
        Player player = (Player) event.getWhoClicked();
        if (event.getSlot() == size - 7 && page > 1) {
            player.openInventory(new IslandVisitorsGUI(page - 1, getIsland()).getInventory());
            return;
        }

        if (event.getSlot() == size - 3 && (size - 9) * page < visitors.size()) {
            player.openInventory(new IslandVisitorsGUI(page + 1, getIsland()).getInventory());
            return;
        }
        if (visitors.size() > event.getSlot()) {
            User visitor = visitors.get(event.getSlot());
            String command = "";
            if (event.isLeftClick()) {
                command = IridiumSkyblock.getInstance().getCommands().expelCommand.aliases.get(0);
            } else if (event.isRightClick()) {
                command = IridiumSkyblock.getInstance().getCommands().banCommand.aliases.get(0);
            }
            Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is " + command + " " + visitor.getName());
            addContent(event.getInventory());
        }
    }
}
