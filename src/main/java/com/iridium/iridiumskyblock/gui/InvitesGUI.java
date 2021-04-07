package com.iridium.iridiumskyblock.gui;

import com.cryptomorin.xseries.XMaterial;
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

public class InvitesGUI implements GUI {

    private final Island island;

    private final HashMap<Integer, String> invites;

    public InvitesGUI(@NotNull Island island) {
        this.island = island;
        invites = new HashMap<>();
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (invites.containsKey(event.getSlot())) {
            Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is uninvite " + invites.get(event.getSlot()));
            event.getWhoClicked().openInventory(getInventory());
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, StringUtils.color("&7Island Invites"));
        List<IslandInvite> islandInvites = IridiumSkyblock.getInstance().getIslandManager().getInvitesByIsland(island);

        InventoryUtils.fillInventory(inventory);

        int i = 0;
        for (IslandInvite islandInvite : islandInvites) {
            inventory.setItem(i, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandInvite, Arrays.asList(
                    new Placeholder("inviter", islandInvite.getInviter().getName()),
                    new Placeholder("player", islandInvite.getUser().getName()),
                    new Placeholder("time", islandInvite.getTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat)))
            )));
            invites.put(i, islandInvite.getUser().getName());
            i++;
        }

        return inventory;
    }
}
