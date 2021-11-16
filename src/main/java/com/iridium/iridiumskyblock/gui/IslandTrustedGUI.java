package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandTrusted;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GUI which displays all trusted users of an Island.
 */
public class IslandTrustedGUI extends IslandGUI {

    private final Map<Integer, User> members;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public IslandTrustedGUI(@NotNull Island island) {
        super(IridiumSkyblock.getInstance().getInventories().trustedGUI, null, island);
        this.members = new HashMap<>();
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, IridiumSkyblock.getInstance().getInventories().trustedGUI.background);

        List<IslandTrusted> islandTrustedList = IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager().getEntries(getIsland());
        AtomicInteger slot = new AtomicInteger(0);
        for (IslandTrusted islandTrusted : islandTrustedList) {
            int itemSlot = slot.getAndIncrement();
            List<Placeholder> placeholderList = new PlaceholderBuilder().applyPlayerPlaceholders(islandTrusted.getUser()).applyIslandPlaceholders(getIsland()).build();
            placeholderList.add(new Placeholder("trustee", islandTrusted.getTruster().getName()));
            inventory.setItem(itemSlot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().trustedGUI.item, placeholderList));
            members.put(itemSlot, islandTrusted.getUser());
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
            IridiumSkyblock.getInstance().getCommands().unTrustCommand.execute(event.getWhoClicked(), new String[]{"", user.getName()});
            addContent(event.getInventory());
        }
    }

}
