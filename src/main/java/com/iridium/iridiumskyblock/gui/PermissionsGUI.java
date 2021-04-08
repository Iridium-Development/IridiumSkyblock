package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.Permission;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.Placeholder;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * GUI which allows users to alter the Island's permissions.
 */
public class PermissionsGUI implements GUI {

    private final Island island;
    private final IslandRank islandRank;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     * @param islandRank The rank which is being configured
     */
    public PermissionsGUI(@NotNull Island island, @NotNull IslandRank islandRank) {
        this.island = island;
        this.islandRank = islandRank;
    }

    /**
     * Builds and returns this inventory.
     *
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 45, StringUtils.color("&7Island Permissions"));

        InventoryUtils.fillInventory(inventory);

        for (Permission permission : IridiumSkyblock.getInstance().getPermissionList()) {
            boolean allowed = IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island, islandRank, permission);
            inventory.setItem(permission.getItem().slot, ItemStackUtils.makeItem(permission.getItem(), Collections.singletonList(new Placeholder("permission", allowed ? IridiumSkyblock.getInstance().getPermissions().allowed : IridiumSkyblock.getInstance().getPermissions().denied))));
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
        for (Permission permission : IridiumSkyblock.getInstance().getPermissionList()) {
            if (permission.getItem().slot != event.getSlot()) continue;

            User user = IridiumSkyblockAPI.getInstance().getUser((Player) event.getWhoClicked());
            if (user.getIslandRank().getLevel() <= islandRank.getLevel() || !IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island, user, IridiumSkyblock.getInstance().getPermissions().changePermissions)) {
                event.getWhoClicked().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotChangePermissions.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            } else {
                boolean allowed = IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island, islandRank, permission);
                IridiumSkyblock.getInstance().getIslandManager().setIslandPermission(island, islandRank, permission, !allowed);
                event.getWhoClicked().openInventory(getInventory());
            }
            return;
        }
    }

}
