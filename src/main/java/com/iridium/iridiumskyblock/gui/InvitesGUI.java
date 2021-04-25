package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandInvite;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.Placeholder;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * GUI which allows users to manage invites.
 */
public class InvitesGUI implements GUI {

    private final Island island;
    private final HashMap<Integer, String> invites;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public InvitesGUI(@NotNull Island island) {
        this.island = island;
        invites = new HashMap<>();
    }

    /**
     * Builds and returns this inventory.
     *
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.size, StringUtils.color(IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.title));
        List<IslandInvite> islandInvites = IridiumSkyblock.getInstance().getIslandManager().getInvitesByIsland(island);

        InventoryUtils.fillInventory(inventory);

        int i = 0;
        for (IslandInvite islandInvite : islandInvites) {
            inventory.setItem(i, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.item, Arrays.asList(
                    new Placeholder("inviter", islandInvite.getInviter().getName()),
                    new Placeholder("player", islandInvite.getUser().getName()),
                    new Placeholder("time", islandInvite.getTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat)))
            )));
            invites.put(i, islandInvite.getUser().getName());
            i++;
        }

        return inventory;
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
            Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is uninvite " + invites.get(event.getSlot()));
            event.getWhoClicked().openInventory(getInventory());
        }
    }

}
