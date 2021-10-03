package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandInvite;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GUI which allows users to manage invites.
 */
public class IslandInvitesGUI extends IslandGUI {

    private final Map<Integer, String> invites;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public IslandInvitesGUI(@NotNull Island island) {
        super(IridiumSkyblock.getInstance().getInventories().islandInvitesGUI, island);
        invites = new HashMap<>();
    }

    @Override
    public void addContent(Inventory inventory) {
        List<IslandInvite> islandInvites = IridiumSkyblock.getInstance().getDatabaseManager().getIslandInviteTableManager().getEntries(getIsland());
        inventory.clear();
        InventoryUtils.fillInventory(inventory, getNoItemGUI().background);

        AtomicInteger slot = new AtomicInteger(0);
        for (int i = 0; i < islandInvites.size(); i++) {
            Bukkit.getScheduler().runTaskAsynchronously(IridiumSkyblock.getInstance(), () -> {
                int itemSlot = slot.getAndIncrement();
                List<Placeholder> placeholderList = new PlaceholderBuilder().applyPlayerPlaceholders(islandInvites.get(itemSlot).getUser()).applyIslandPlaceholders(getIsland()).build();
                placeholderList.add(new Placeholder("inviter", islandInvites.get(itemSlot).getInviter().getName()));
                placeholderList.add(new Placeholder("time", islandInvites.get(itemSlot).getTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat))));
                inventory.setItem(itemSlot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.item, placeholderList));
                invites.put(itemSlot, islandInvites.get(itemSlot).getUser().getName());
            });
        }
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (invites.containsKey(event.getSlot())) {
            String command = IridiumSkyblock.getInstance().getCommands().unInviteCommand.aliases.get(0);
            Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is " + command + " " + invites.get(event.getSlot()));
            event.getWhoClicked().openInventory(getInventory());
        }
    }

}
