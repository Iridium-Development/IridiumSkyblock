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

/**
 * GUI which allows users to manage invites.
 */
public class InvitesGUI extends GUI {

    private final HashMap<Integer, String> invites;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public InvitesGUI(@NotNull Island island) {
        super(IridiumSkyblock.getInstance().getInventories().islandInvitesGUI, island);
        invites = new HashMap<>();
    }

    @Override
    public void addContent(Inventory inventory) {
        List<IslandInvite> islandInvites = IridiumSkyblock.getInstance().getDatabaseManager().getIslandInviteTableManager().getEntries(getIsland());
        inventory.clear();
        InventoryUtils.fillInventory(inventory, getNoItemGUI().background);


        int i = 0;
        for (IslandInvite islandInvite : islandInvites) {
            List<Placeholder> placeholderList = new PlaceholderBuilder().applyPlayerPlaceholders(islandInvite.getUser()).applyIslandPlaceholders(getIsland()).build();
            placeholderList.add(new Placeholder("inviter", islandInvite.getInviter().getName()));
            placeholderList.add(new Placeholder("time", islandInvite.getTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat))));
            inventory.setItem(i, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.item, placeholderList));
            invites.put(i, islandInvite.getUser().getName());
            i++;
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
