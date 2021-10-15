package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.bank.BankItem;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
    public IslandBankGUI(@NotNull Island island) {
        super(IridiumSkyblock.getInstance().getInventories().bankGUI, island);
    }

    @Override
    public void addContent(Inventory inventory) {
        clearInventory(inventory);

        for (BankItem bankItem : IridiumSkyblock.getInstance().getBankItemList()) {
            IslandBank islandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(getIsland(), bankItem);
            inventory.setItem(bankItem.getItem().slot, ItemStackUtils.makeItem(bankItem.getItem(), Collections.singletonList(new Placeholder("amount", IridiumSkyblock.getInstance().getNumberFormatter().format(islandBank.getNumber())))));
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
        Player player = (Player) event.getWhoClicked();
        if (isBackButton(event)) return;

        Optional<BankItem> bankItem = IridiumSkyblock.getInstance().getBankItemList().stream().filter(item -> item.getItem().slot == event.getSlot()).findFirst();
        if (!bankItem.isPresent()) return;

        String command;
        switch (event.getClick()) {
            case LEFT:
                command = IridiumSkyblock.getInstance().getCommands().withdrawCommand.aliases.get(0);
                break;
            case RIGHT:
                command = IridiumSkyblock.getInstance().getCommands().depositCommand.aliases.get(0);
                break;
            default:
                return;
        }

        if (command != null) {
            Bukkit.getServer().dispatchCommand(player, "is " + command + " " + bankItem.get().getName() + " " + bankItem.get().getDefaultAmount());
        }

        addContent(event.getInventory());
    }


}
