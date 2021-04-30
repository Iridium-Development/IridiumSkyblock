package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandInvite;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

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

        addContent(inventory);

        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        List<IslandInvite> islandInvites = IridiumSkyblock.getInstance().getIslandManager().getInvitesByIsland(island);
        inventory.clear();
        InventoryUtils.fillInventory(inventory);


        int i = 0;
        for (IslandInvite islandInvite : islandInvites) {
            inventory.setItem(i, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandInvitesGUI.item, new PlaceholderBuilder().applyIslandPlaceholders(island).build()));
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
            Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is uninvite " + invites.get(event.getSlot()));
            event.getWhoClicked().openInventory(getInventory());
        }
    }

}
