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

/**
 * GUI which allows users to select the type of mission they want in the {@link MissionsGUI}.
 * @see Mission.MissionType
 */
public class MissionSelectGUI implements GUI {

    private final Island island;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public MissionSelectGUI(@NotNull Island island) {
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
        Inventory inventory = Bukkit.createInventory(this, 27, StringUtils.color("&7Island Missions"));

        InventoryUtils.fillInventory(inventory);

        inventory.setItem(IridiumSkyblock.getInstance().getInventories().dailyQuests.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().dailyQuests));
        inventory.setItem(IridiumSkyblock.getInstance().getInventories().oneTimeQuests.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().oneTimeQuests));

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
        if (event.getSlot() == IridiumSkyblock.getInstance().getInventories().dailyQuests.slot) {
            event.getWhoClicked().openInventory(new MissionsGUI(island, Mission.MissionType.DAILY).getInventory());
        } else if (event.getSlot() == IridiumSkyblock.getInstance().getInventories().oneTimeQuests.slot) {
            event.getWhoClicked().openInventory(new MissionsGUI(island, Mission.MissionType.ONCE).getInventory());
        }
    }

}
