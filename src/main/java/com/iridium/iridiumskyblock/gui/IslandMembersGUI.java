package com.iridium.iridiumskyblock.gui;


import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.configs.inventories.NoItemGUI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * GUI which displays all members of an Island and allows quick rank management.
 */
public class IslandMembersGUI extends PagedGUI<User> {

    private final Island island;

    public IslandMembersGUI(Island island, Inventory previousInventory) {
        super(1,
                IridiumSkyblock.getInstance().getInventories().membersGUI.size,
                IridiumSkyblock.getInstance().getInventories().membersGUI.background,
                IridiumSkyblock.getInstance().getInventories().previousPage,
                IridiumSkyblock.getInstance().getInventories().nextPage,
                previousInventory,
                IridiumSkyblock.getInstance().getInventories().backButton
        );
        this.island = island;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        NoItemGUI noItemGUI = IridiumSkyblock.getInstance().getInventories().membersGUI;
        Inventory inventory = Bukkit.createInventory(this, getSize(), StringUtils.color(noItemGUI.title));
        addContent(inventory);
        return inventory;
    }

    @Override
    public Collection<User> getPageObjects() {
        return IridiumSkyblock.getInstance().getIslandManager().getIslandMembers(island);
    }

    @Override
    public ItemStack getItemStack(User user) {
        return ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().membersGUI.item, new PlaceholderBuilder()
                .applyIslandPlaceholders(island)
                .applyPlayerPlaceholders(user)
                .build()
        );
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        super.onInventoryClick(event);
        User user = getItem(event.getSlot());
        if (user == null) return;
        switch (event.getClick()) {
            case LEFT:
                IridiumSkyblock.getInstance().getCommands().demoteCommand.execute(event.getWhoClicked(), new String[]{"", user.getName()});
                break;
            case RIGHT:
                IridiumSkyblock.getInstance().getCommands().promoteCommand.execute(event.getWhoClicked(), new String[]{"", user.getName()});
                break;
        }
        addContent(event.getInventory());
    }
}