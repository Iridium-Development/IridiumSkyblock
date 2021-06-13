package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.configs.inventories.IslandRanksInventoryConfig;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GUI which allows users to select ranks to edit in the {@link PermissionsGUI}.
 */
public class IslandRanksGUI implements GUI {

    private final Island island;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public IslandRanksGUI(@NotNull Island island) {
        this.island = island;
    }

    /**
     * Builds and returns this inventory.
     *
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().islandRanksGUI.size, StringUtils.color(IridiumSkyblock.getInstance().getInventories().islandRanksGUI.title));

        addContent(inventory);

        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        IslandRanksInventoryConfig islandRanks = IridiumSkyblock.getInstance().getInventories().islandRanksGUI;
        InventoryUtils.fillInventory(inventory, islandRanks.background);
        List<User> members = IridiumSkyblock.getInstance().getIslandManager().getIslandMembers(island);
        inventory.setItem(islandRanks.owner.slot, ItemStackUtils.makeItem(islandRanks.owner,
                Collections.singletonList(new Placeholder("members", island.getOwner().getName()))));
        inventory.setItem(islandRanks.co_owner.slot, ItemStackUtils.makeItem(islandRanks.co_owner,
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
        if (event.getSlot() == islandRanks.owner.slot) event.getWhoClicked().openInventory(new PermissionsGUI(island, IslandRank.OWNER).getInventory());
        else if (event.getSlot() == islandRanks.co_owner.slot) event.getWhoClicked().openInventory(new PermissionsGUI(island, IslandRank.CO_OWNER).getInventory());
        else if (event.getSlot() == islandRanks.moderator.slot) event.getWhoClicked().openInventory(new PermissionsGUI(island, IslandRank.MODERATOR).getInventory());
        else if (event.getSlot() == islandRanks.member.slot) event.getWhoClicked().openInventory(new PermissionsGUI(island, IslandRank.MEMBER).getInventory());
        else if (event.getSlot() == islandRanks.visitor.slot) event.getWhoClicked().openInventory(new PermissionsGUI(island, IslandRank.VISITOR).getInventory());

    }

}
