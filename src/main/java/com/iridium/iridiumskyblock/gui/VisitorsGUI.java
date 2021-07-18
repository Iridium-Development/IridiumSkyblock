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
import java.util.stream.Collectors;

/**
 * GUI which displays players on island
 */
public class VisitorsGUI extends GUI {

    private final int page;
    private List<Player> visitors;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public VisitorsGUI(int page, @NotNull Island island) {
        super(IridiumSkyblock.getInstance().getInventories().visitorsGUI, island);
        this.page = page;
        this.visitors = new ArrayList<>();
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, getNoItemGUI().background);

        inventory.setItem(inventory.getSize() - 3, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().nextPage));
        inventory.setItem(inventory.getSize() - 7, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().previousPage));

        visitors = getIsland().getPlayersOnIsland().stream().filter(player -> !getIsland().equals(IridiumSkyblock.getInstance().getUserManager().getUser(player).getIsland().orElse(null))).collect(Collectors.toList());

        final long elementsPerPage = inventory.getSize() - 9;
        AtomicInteger slot = new AtomicInteger(0);


        visitors.stream()
                .skip((page - 1) * elementsPerPage)
                .limit(elementsPerPage)
                .forEachOrdered(visitor -> {
                    User user = IridiumSkyblock.getInstance().getUserManager().getUser(visitor);
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
        final int size = IridiumSkyblock.getInstance().getInventories().bansGUI.size;
        Player player = (Player) event.getWhoClicked();
        if (event.getSlot() == size - 7 && page > 1) {
            player.openInventory(new VisitorsGUI(page - 1, getIsland()).getInventory());
            return;
        }

        if (event.getSlot() == size - 3 && (size - 9) * page < visitors.size()) {
            player.openInventory(new VisitorsGUI(page + 1, getIsland()).getInventory());
            return;
        }

        if (event.getSlot() + 1 <= visitors.size()) {
            int index = ((size - 9) * (page - 1)) + event.getSlot();
            if (visitors.size() > index) {
                Player visitor = visitors.get(event.getSlot());
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
}
