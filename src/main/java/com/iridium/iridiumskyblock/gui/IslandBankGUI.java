package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumcore.utils.InventoryUtils;
import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.bank.BankItem;
import com.iridium.iridiumskyblock.bank.MobCoinsBankItem;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBank;
import com.opblocks.utils.SignContainer;
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
        inventory.clear();
        InventoryUtils.fillInventory(inventory, getNoItemGUI().background);

        for (BankItem bankItem : IridiumSkyblock.getInstance().getBankItemList()) {
            IslandBank islandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(getIsland(), bankItem);
            inventory.setItem(bankItem.getItem().slot, ItemStackUtils.makeItem(bankItem.getItem(), Collections.singletonList(new Placeholder("amount", IridiumSkyblock.getInstance().getNumberFormatter().format(islandBank.getNumber())))));
        }
        inventory.setItem(22, backItem);
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
        if (event.getSlot() == 22) {
            player.openInventory(new InventoryConfigGUI(IridiumSkyblock.getInstance().getInventories().islandMenu).getInventory());
            return;
        }

        Optional<BankItem> bankItem = IridiumSkyblock.getInstance().getBankItemList().stream().filter(item -> item.getItem().slot == event.getSlot()).findFirst();
        if (!bankItem.isPresent()) return;

        switch (event.getClick()) {
            case LEFT:
                player.closeInventory();
                if(bankItem.get() instanceof MobCoinsBankItem) {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getConfiguration().prefix + " &7You can't withdraw Mob Coins from your bank!"));
                    return;
                }

                SignContainer.openGUIFor(player, "", "^^^^^^^^^^^^^^^^", "Enter the", "withdraw amount", new SignContainer.SignGUIListener() {
                    @Override
                    public void onSignDone(Player player, String[] lines) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(IridiumSkyblock.getInstance(), () -> Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is " + IridiumSkyblock.getInstance().getCommands().withdrawCommand.aliases.get(0) + " " + bankItem.get().getName() + " " + lines[0]));
                    }
                });
                break;
            case RIGHT:
                player.closeInventory();
                SignContainer.openGUIFor(player, "", "^^^^^^^^^^^^^^^^", "Enter the", "deposit amount", new SignContainer.SignGUIListener() {
                    @Override
                    public void onSignDone(Player player, String[] lines) {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(IridiumSkyblock.getInstance(), () -> Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is " + IridiumSkyblock.getInstance().getCommands().depositCommand.aliases.get(0) + " " + bankItem.get().getName() + " " + lines[0]));
                    }
                });
                break;
            default:
                return;
        }
    }


}
