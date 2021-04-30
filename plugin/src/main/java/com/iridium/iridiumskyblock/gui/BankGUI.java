package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.bank.BankItem;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBank;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.Placeholder;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;

/**
 * GUI which allows users to manage the Island bank.
 */
public class BankGUI implements GUI {

    private final Island island;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public BankGUI(@NotNull Island island) {
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
        Inventory inventory = Bukkit.createInventory(this, IridiumSkyblock.getInstance().getInventories().bankGUI.size, StringUtils.color(IridiumSkyblock.getInstance().getInventories().bankGUI.title));

        addContent(inventory);

        return inventory;
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory);

        for (BankItem bankItem : IridiumSkyblock.getInstance().getBankItemList()) {
            IslandBank islandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island, bankItem);
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

        switch (event.getClick()) {
            case LEFT:
                Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is withdraw " + bankItem.get().getName() + " " + bankItem.get().getDefaultAmount());
                break;
            case RIGHT:
                Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is deposit " + bankItem.get().getName() + " " + bankItem.get().getDefaultAmount());
        }

        event.getWhoClicked().openInventory(getInventory());
    }


}
