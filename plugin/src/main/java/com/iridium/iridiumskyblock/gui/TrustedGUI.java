package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.configs.inventories.SingleItemGUI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandTrusted;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.Placeholder;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * GUI which displays all trusted users of an Island.
 */
public class TrustedGUI implements GUI {

    private final Island island;
    private final HashMap<Integer, User> members;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public TrustedGUI(@NotNull Island island) {
        this.island = island;
        this.members = new HashMap<>();
    }

    /**
     * Builds and returns this inventory.
     *
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        SingleItemGUI singleItemGUI = IridiumSkyblock.getInstance().getInventories().trustedGUI;
        Inventory inventory = Bukkit.createInventory(this, singleItemGUI.size, StringUtils.color(singleItemGUI.title));

        addContent(inventory);

        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory);

        int i = 0;
        List<IslandTrusted> islandTrustedList = IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager().getEntries(island);
        for (IslandTrusted islandTrusted : islandTrustedList) {
            List<Placeholder> placeholderList =
                    new PlaceholderBuilder().applyPlayerPlaceholders(islandTrusted.getUser()).applyIslandPlaceholders(island).build();
            placeholderList.add(new Placeholder("trustee", islandTrusted.getTruster().getName()));
            inventory.setItem(i, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().trustedGUI.item, placeholderList));
            members.put(i, islandTrusted.getUser());
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
        if (members.containsKey(event.getSlot())) {
            User user = members.get(event.getSlot());
            Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is untrust " + user.getName());
            addContent(event.getInventory());
        }
    }

}
