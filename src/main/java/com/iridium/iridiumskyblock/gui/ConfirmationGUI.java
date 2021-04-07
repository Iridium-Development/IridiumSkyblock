package com.iridium.iridiumskyblock.gui;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class ConfirmationGUI implements GUI {

    private final @NotNull Runnable runnable;
    private final @NotNull IridiumSkyblock iridiumSkyblock;

    public ConfirmationGUI(@NotNull IridiumSkyblock iridiumSkyblock, @NotNull Runnable runnable) {
        this.iridiumSkyblock = iridiumSkyblock;
        this.runnable = runnable;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, StringUtils.color(iridiumSkyblock.getInventories().ConfirmationGUITitle));
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().filler));
        }
        inventory.setItem(11, ItemStackUtils.makeItem(iridiumSkyblock.getInventories().no));
        inventory.setItem(15, ItemStackUtils.makeItem(iridiumSkyblock.getInventories().yes));
        return inventory;
    }

    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() == 11) {
            event.getWhoClicked().closeInventory();
        } else if (event.getSlot() == 15) {
            runnable.run();
            event.getWhoClicked().closeInventory();
        }
    }
}
