package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.bank.BankItem;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBank;
import me.entity303.virtualanvil.virtual.anvil.VirtualAnvil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.AnvilInventory;

import java.util.*;

public class IslandCustomAmountBankGUI implements Listener {
    private final Map<UUID, Data> opened = new HashMap();

    public IslandCustomAmountBankGUI() {
        if (VirtualAnvil.getVirtualAnvil() == null)
            VirtualAnvil.load();

        Bukkit.getPluginManager().registerEvents(this, IridiumSkyblock.getInstance());
    }

    public void openInventory(Player player, Island island, BankItem bankItem, boolean deposit) {
        VirtualAnvil.getVirtualAnvil().openAnvil(player);
        IslandBank islandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island, bankItem);
        player.getOpenInventory().getTopInventory().setItem(0, ItemStackUtils.makeItem(bankItem.getItem(), Collections.singletonList(new Placeholder("amount", IridiumSkyblock.getInstance().getNumberFormatter().format(islandBank.getNumber())))));
        this.opened.put(player.getUniqueId(), new Data(island, deposit, bankItem));
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e) {
        if (this.opened.containsKey(e.getPlayer().getUniqueId())) {
            e.getInventory().setItem(0, null);
            this.opened.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!this.opened.containsKey(e.getWhoClicked().getUniqueId()))
            return;

        e.setCancelled(true);

        if (!(e.getClickedInventory() instanceof AnvilInventory))
            return;

        AnvilInventory anvilInventory = (AnvilInventory) e.getClickedInventory();

        if (e.getSlot() == 2) {
            if (e.getWhoClicked().getExpToLevel() >= anvilInventory.getRepairCost())
                ((Player) e.getWhoClicked()).giveExpLevels(0);
            Data data = this.opened.get(e.getWhoClicked().getUniqueId());
            e.setCancelled(true);
            if (data.isDeposit())
                try {
                    data.bankItem.deposit((Player) e.getWhoClicked(), Double.parseDouble(Objects.requireNonNull(anvilInventory.getRenameText())));
                } catch (NullPointerException | NumberFormatException ignored) {
                    e.getWhoClicked().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notANumber.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            else try {
                data.bankItem.withdraw((Player) e.getWhoClicked(), Double.parseDouble(Objects.requireNonNull(anvilInventory.getRenameText())));
            } catch (NullPointerException | NumberFormatException ignored) {
                e.getWhoClicked().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notANumber.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }

            e.getWhoClicked().openInventory(e.getInventory());
            e.getWhoClicked().closeInventory();
        }
    }

    private static final class Data {
        private final Island island;
        private final boolean deposit;
        private final BankItem bankItem;

        public Data(Island island, boolean deposit, BankItem bankItem) {
            this.island = island;
            this.deposit = deposit;
            this.bankItem = bankItem;
        }

        public Island getIsland() {
            return this.island;
        }

        public boolean isDeposit() {
            return this.deposit;
        }

        public BankItem getBankItem() {
            return this.bankItem;
        }
    }
}
