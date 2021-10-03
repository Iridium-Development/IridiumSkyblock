package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.configs.inventories.IslandRanksInventoryConfig;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GUI which allows users to select ranks to edit in the {@link IslandPermissionsGUI}.
 */
public class IslandRanksGUI extends IslandGUI {

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public IslandRanksGUI(@NotNull Island island) {
        super(IridiumSkyblock.getInstance().getInventories().islandRanksGUI, island);
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        IslandRanksInventoryConfig islandRanks = IridiumSkyblock.getInstance().getInventories().islandRanksGUI;
        InventoryUtils.fillInventory(inventory, islandRanks.background);
        List<User> members = IridiumSkyblock.getInstance().getIslandManager().getIslandMembers(getIsland());
        inventory.setItem(islandRanks.owner.slot, ItemStackUtils.makeItem(islandRanks.owner,
                Collections.singletonList(new Placeholder("members", getIsland().getOwner().getName()))));
        inventory.setItem(islandRanks.coOwner.slot, ItemStackUtils.makeItem(islandRanks.coOwner,
                Collections.singletonList(new Placeholder("members", members.stream().filter(member -> member.getIslandRank().equals(IslandRank.CO_OWNER)).map(User::getName).collect(Collectors.joining(", "))))));
        inventory.setItem(islandRanks.moderator.slot, ItemStackUtils.makeItem(islandRanks.moderator,
                Collections.singletonList(new Placeholder("members", members.stream().filter(member -> member.getIslandRank().equals(IslandRank.MODERATOR)).map(User::getName).collect(Collectors.joining(", "))))));
        inventory.setItem(islandRanks.member.slot, ItemStackUtils.makeItem(islandRanks.member,
                Collections.singletonList(new Placeholder("members", members.stream().filter(member -> member.getIslandRank().equals(IslandRank.MEMBER)).map(User::getName).collect(Collectors.joining(", "))))));
        inventory.setItem(islandRanks.visitor.slot, ItemStackUtils.makeItem(islandRanks.visitor));
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        IslandRanksInventoryConfig islandRanks = IridiumSkyblock.getInstance().getInventories().islandRanksGUI;
        if (event.getSlot() == islandRanks.owner.slot)
            event.getWhoClicked().openInventory(new IslandPermissionsGUI(getIsland(), IslandRank.OWNER, 1).getInventory());
        else if (event.getSlot() == islandRanks.coOwner.slot)
            event.getWhoClicked().openInventory(new IslandPermissionsGUI(getIsland(), IslandRank.CO_OWNER, 1).getInventory());
        else if (event.getSlot() == islandRanks.moderator.slot)
            event.getWhoClicked().openInventory(new IslandPermissionsGUI(getIsland(), IslandRank.MODERATOR, 1).getInventory());
        else if (event.getSlot() == islandRanks.member.slot)
            event.getWhoClicked().openInventory(new IslandPermissionsGUI(getIsland(), IslandRank.MEMBER, 1).getInventory());
        else if (event.getSlot() == islandRanks.visitor.slot)
            event.getWhoClicked().openInventory(new IslandPermissionsGUI(getIsland(), IslandRank.VISITOR, 1).getInventory());

    }

}