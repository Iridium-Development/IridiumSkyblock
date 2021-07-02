package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.bank.BankItem;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBank;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;

/**
 * GUI which allows users to manage the Island bank.
 */
public class BankGUI extends GUI {

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public BankGUI(@NotNull Island island) {
        super(IridiumSkyblock.getInstance().getInventories().bankGUI, island);
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, getNoItemGUI().background);

        for (BankItem bankItem : IridiumSkyblock.getInstance().getBankItemList()) {
            IslandBank islandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(getIsland(), bankItem);
            inventory.setItem(bankItem.getItem().slot, ItemStackUtils.makeItem(bankItem.getItem(), Collections.singletonList(new Placeholder("amount", bankItem.toString(islandBank.getNumber())))));
        }
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Optional<BankItem> bankItem = IridiumSkyblock.getInstance().getBankItemList().stream().filter(item -> item.getItem().slot == event.getSlot()).findFirst();
        if (!bankItem.isPresent()) return;

        String command = null;
        switch (event.getClick()) {
            case LEFT:
                command = IridiumSkyblock.getInstance().getCommands().withdrawCommand.aliases.get(0);
                break;
            case RIGHT:
                command = IridiumSkyblock.getInstance().getCommands().depositCommand.aliases.get(0);
        }
        if (command != null)
            Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is " + command + " " + bankItem.get().getName() + " " + bankItem.get().getDefaultAmount());

        event.getWhoClicked().openInventory(getInventory());
    }


}
