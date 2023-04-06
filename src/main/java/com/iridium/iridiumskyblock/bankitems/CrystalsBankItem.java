package com.iridium.iridiumskyblock.bankitems;


import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumteams.IridiumTeams;
import com.iridium.iridiumteams.bank.BankItem;
import com.iridium.iridiumteams.bank.BankResponse;
import com.iridium.iridiumteams.database.TeamBank;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@NoArgsConstructor
public class CrystalsBankItem extends BankItem {


    public CrystalsBankItem(double defaultAmount, Item item) {
        super("Crystals", item, defaultAmount, true);
    }

    @Override
    public BankResponse withdraw(Player player, Number amount, TeamBank teamBank, IridiumTeams<?, ?> iridiumTeams) {
        int crystals = Math.min(amount.intValue(), (int) teamBank.getNumber());
        if (crystals > 0) {
            player.getInventory().addItem(IridiumSkyblock.getInstance().getIslandManager().getIslandCrystal(crystals)).values().forEach(itemStack ->
                    player.getWorld().dropItem(player.getLocation(), itemStack)
            );
            teamBank.setNumber(teamBank.getNumber() - crystals);
            return new BankResponse(crystals, true);
        }
        return new BankResponse(crystals, false);
    }

    @Override
    public BankResponse deposit(Player player, Number number, TeamBank teamBank, IridiumTeams<?, ?> iridiumTeams) {
        Optional<Island> islandOptional = IridiumSkyblock.getInstance().getUserManager().getUser(player).getIsland();
        if (!islandOptional.isPresent()) {
            return new BankResponse(0, false);
        }

        int remainingItemAmount = number.intValue();
        int depositAmount = 0;

        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length && remainingItemAmount > 0; i++) {
            ItemStack itemStack = contents[i];

            int crystalsPerItem = IridiumSkyblock.getInstance().getIslandManager().getIslandCrystals(itemStack);
            if (crystalsPerItem == 0) continue;

            int itemStackAmount = itemStack.getAmount();
            if (itemStackAmount <= remainingItemAmount) {
                player.getInventory().setItem(i, null);

                depositAmount += itemStackAmount * crystalsPerItem;
                remainingItemAmount -= itemStackAmount;
            } else {
                itemStack.setAmount(itemStack.getAmount() - remainingItemAmount);
                player.getInventory().setItem(i, itemStack);

                depositAmount += remainingItemAmount * crystalsPerItem;
                remainingItemAmount = 0;
            }

            return new BankResponse(0, false);
        }

        if (depositAmount == 0) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().insufficientFundsToDeposit
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
                    .replace("%type%", IridiumSkyblock.getInstance().getBankItems().crystalsBankItem.getName())
            );

            return new BankResponse(0.0, false);
        }

        TeamBank islandBank = IridiumSkyblock.getInstance().getIslandManager().getTeamBank(islandOptional.get(), String.valueOf(this));
        islandBank.setNumber(islandBank.getNumber() + depositAmount);
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().bankDeposited
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
                .replace("%amount%", String.valueOf(depositAmount))
                .replace("%type%", IridiumSkyblock.getInstance().getBankItems().crystalsBankItem.getName())
        );

        return new BankResponse(depositAmount, true);
    }



    private int removeCrystals(Inventory inventory) {
        int removedCrystals = 0;

        for (int slot = 0; slot < inventory.getContents().length; slot++) {
            ItemStack itemStack = inventory.getContents()[slot];
            if (itemStack == null) continue;
            int crystalsPerItem = IridiumSkyblock.getInstance().getIslandManager().getIslandCrystals(itemStack);
            int itemStackAmount = itemStack.getAmount();

            inventory.setItem(slot, null);
            removedCrystals += crystalsPerItem * itemStackAmount;
        }
        return removedCrystals;
    }
}