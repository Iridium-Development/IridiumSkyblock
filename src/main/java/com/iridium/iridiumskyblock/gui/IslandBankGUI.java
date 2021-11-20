package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.bank.BankItem;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBank;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;

/**
 * GUI which allows users to manage the Island bank.
 */
public class IslandBankGUI extends IslandGUI {

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public IslandBankGUI(@NotNull Island island, Inventory previousInventory) {
        super(IridiumSkyblock.getInstance().getInventories().bankGUI, previousInventory, island);
    }

    @Override
    public void addContent(Inventory inventory) {
        inventory.clear();
        InventoryUtils.fillInventory(inventory, getNoItemGUI().background);

        for (BankItem bankItem : IridiumSkyblock.getInstance().getBankItemList()) {
            IslandBank islandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(getIsland(), bankItem);
            inventory.setItem(bankItem.getItem().slot, ItemStackUtils.makeItem(bankItem.getItem(), Collections.singletonList(new Placeholder("amount", IridiumSkyblock.getInstance().getNumberFormatter().format(islandBank.getNumber())))));
        }

        if (IridiumSkyblock.getInstance().getConfiguration().backButtons && getPreviousInventory() != null) {
            inventory.setItem(inventory.getSize() + IridiumSkyblock.getInstance().getInventories().backButton.slot, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().backButton));
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
        if (bankItem.isEmpty()) return;

        switch (event.getClick()) {
            case LEFT -> IridiumSkyblock.getInstance().getCommands().withdrawCommand.execute(event.getWhoClicked(), new String[]{"", bankItem.get().getName(), String.valueOf(bankItem.get().getDefaultAmount())});
            case RIGHT -> IridiumSkyblock.getInstance().getCommands().depositCommand.execute(event.getWhoClicked(), new String[]{"", bankItem.get().getName(), String.valueOf(bankItem.get().getDefaultAmount())});
        }

        addContent(event.getInventory());
    }


}
