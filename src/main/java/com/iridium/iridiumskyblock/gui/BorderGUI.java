package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.Color;
import com.iridium.iridiumcore.gui.BackGUI;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumteams.configs.inventories.NoItemGUI;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class BorderGUI extends BackGUI {

    public BorderGUI(Inventory previousInventory) {
        super(
                IridiumSkyblock.getInstance().getInventories().islandBorderGUI.background,
                previousInventory,
                IridiumSkyblock.getInstance().getInventories().backButton
        );
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        NoItemGUI noItemGUI = IridiumSkyblock.getInstance().getInventories().islandBorderGUI;
        Inventory inventory = Bukkit.createInventory(this, noItemGUI.size, StringUtils.color(noItemGUI.title));
        addContent(inventory);
        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        super.addContent(inventory);
        if (IridiumSkyblock.getInstance().getConfiguration().enabledBorders.getOrDefault(Color.BLUE, true)) {
            inventory.setItem(IridiumSkyblock.getInstance().getInventories().islandBorderGUI.blue.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandBorderGUI.blue));
        }
        if (IridiumSkyblock.getInstance().getConfiguration().enabledBorders.getOrDefault(Color.RED, true)) {
            inventory.setItem(IridiumSkyblock.getInstance().getInventories().islandBorderGUI.red.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandBorderGUI.red));
        }
        if (IridiumSkyblock.getInstance().getConfiguration().enabledBorders.getOrDefault(Color.GREEN, true)) {
            inventory.setItem(IridiumSkyblock.getInstance().getInventories().islandBorderGUI.green.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandBorderGUI.green));
        }
        if (IridiumSkyblock.getInstance().getConfiguration().enabledBorders.getOrDefault(Color.OFF, true)) {
            inventory.setItem(IridiumSkyblock.getInstance().getInventories().islandBorderGUI.off.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandBorderGUI.off));
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        super.onInventoryClick(event);

        if (IridiumSkyblock.getInstance().getConfiguration().enabledBorders.getOrDefault(Color.BLUE, true) && event.getSlot() == IridiumSkyblock.getInstance().getInventories().islandBorderGUI.blue.slot) {
            IridiumSkyblock.getInstance().getCommands().borderCommand.execute(event.getWhoClicked(), new String[]{"blue"}, IridiumSkyblock.getInstance());
        }

        if (IridiumSkyblock.getInstance().getConfiguration().enabledBorders.getOrDefault(Color.RED, true) && event.getSlot() == IridiumSkyblock.getInstance().getInventories().islandBorderGUI.red.slot) {
            IridiumSkyblock.getInstance().getCommands().borderCommand.execute(event.getWhoClicked(), new String[]{"red"}, IridiumSkyblock.getInstance());
        }

        if (IridiumSkyblock.getInstance().getConfiguration().enabledBorders.getOrDefault(Color.GREEN, true) && event.getSlot() == IridiumSkyblock.getInstance().getInventories().islandBorderGUI.green.slot) {
            IridiumSkyblock.getInstance().getCommands().borderCommand.execute(event.getWhoClicked(), new String[]{"green"}, IridiumSkyblock.getInstance());
        }

        if (IridiumSkyblock.getInstance().getConfiguration().enabledBorders.getOrDefault(Color.OFF, true) && event.getSlot() == IridiumSkyblock.getInstance().getInventories().islandBorderGUI.off.slot) {
            IridiumSkyblock.getInstance().getCommands().borderCommand.execute(event.getWhoClicked(), new String[]{"off"}, IridiumSkyblock.getInstance());
        }
    }

}
