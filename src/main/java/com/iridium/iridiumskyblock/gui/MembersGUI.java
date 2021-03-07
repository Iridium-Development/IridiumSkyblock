package com.iridium.iridiumskyblock.gui;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.Placeholder;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;

public class MembersGUI implements GUI {

    private final Island island;
    private final HashMap<Integer, User> members;

    public MembersGUI(@NotNull Island island) {
        this.island = island;
        this.members = new HashMap<>();
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (members.containsKey(event.getSlot())) {
            User user = members.get(event.getSlot());
            if (event.getClick().equals(ClickType.LEFT)) {
                Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is demote " + user.getName());
            } else if (event.getClick().equals(ClickType.RIGHT)) {
                Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is promote " + user.getName());
            }
            event.getWhoClicked().openInventory(getInventory());
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, StringUtils.color("&7Island Members"));
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
        }
        int i = 0;
        for (User member : island.getMembers()) {
            inventory.setItem(i, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandMember, Arrays.asList(
                    new Placeholder("player", member.getName()),
                    new Placeholder("rank", member.getIslandRank().name()),
                    new Placeholder("time", member.getJoinTime().format(DateTimeFormatter.ofPattern(IridiumSkyblock.getInstance().getConfiguration().dateTimeFormat)))
            )));
            members.put(i, member);
            i++;
        }
        return inventory;
    }
}
