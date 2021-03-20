package com.iridium.iridiumskyblock.gui;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.BankItem;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBank;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.Placeholder;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;

public class BankGUI implements GUI {

    private final Island island;

    public BankGUI(@NotNull Island island) {
        this.island = island;
    }

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

    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, StringUtils.color("&7Island Bank"));
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, XMaterial.BLACK_STAINED_GLASS_PANE.parseItem());
        }
        for (BankItem bankItem : IridiumSkyblock.getInstance().getBankItemList()) {
            IslandBank islandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island, bankItem);
            inventory.setItem(bankItem.getItem().slot, ItemStackUtils.makeItem(bankItem.getItem(), Collections.singletonList(new Placeholder("amount", bankItem.toString(islandBank.getNumber())))));
        }
        return inventory;
    }
}
