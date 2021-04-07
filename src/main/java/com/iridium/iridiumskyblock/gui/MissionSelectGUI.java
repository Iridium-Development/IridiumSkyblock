package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.Mission;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class MissionSelectGUI implements GUI {

    private final Island island;

    public MissionSelectGUI(@NotNull Island island) {
        this.island = island;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() == IridiumSkyblock.getInstance().getInventories().dailyQuests.slot) {
            event.getWhoClicked().openInventory(new MissionsGUI(island, Mission.MissionType.DAILY).getInventory());
        } else if (event.getSlot() == IridiumSkyblock.getInstance().getInventories().oneTimeQuests.slot) {
            event.getWhoClicked().openInventory(new MissionsGUI(island, Mission.MissionType.ONCE).getInventory());
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, StringUtils.color("&7Island Missions"));

        InventoryUtils.fillInventory(inventory);

        inventory.setItem(IridiumSkyblock.getInstance().getInventories().dailyQuests.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().dailyQuests));
        inventory.setItem(IridiumSkyblock.getInstance().getInventories().oneTimeQuests.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().oneTimeQuests));

        return inventory;
    }

}
