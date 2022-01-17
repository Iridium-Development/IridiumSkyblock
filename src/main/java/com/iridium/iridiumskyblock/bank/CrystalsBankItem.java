package com.iridium.iridiumskyblock.bank;

import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBank;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.IslandBankGUI;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * Represents the crystals in the island bank.
 * Serialized in the Configuration files.
 */
@NoArgsConstructor
public class CrystalsBankItem extends BankItem {

    /**
     * The default constructor.
     *
     * @param defaultAmount The default withdrawal amount of this item
     * @param item          The Item which represents this bank item in the {@link IslandBankGUI}
     */
    public CrystalsBankItem(double defaultAmount, Item item) {
        super("crystals", "Crystals", defaultAmount, true, item);
    }

    /**
     * Withdraws the given amount of this item from the Player's bank.
     *
     * @param player The player who wants to withdraw
     * @param amount The amount which should be withdrawn
     */
    @Override
    public double withdraw(Player player, Number amount) {
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        Optional<Island> island = user.getIsland();

        if (island.isPresent()) {
            IslandBank islandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(island.get(), this);
            int crystals = Math.min(amount.intValue(), (int) islandBank.getNumber());

            if (crystals > 0) {
                islandBank.setNumber(islandBank.getNumber() - crystals);
                player.getInventory().addItem(IridiumSkyblock.getInstance().getIslandManager().getIslandCrystal(crystals)).values().forEach(itemStack ->
                        player.getWorld().dropItem(player.getLocation(), itemStack)
                );
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().bankWithdrew
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
                        .replace("%amount%", String.valueOf(crystals))
                        .replace("%type%", getDisplayName())
                );
            } else {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().insufficientFundsToWithdrew
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
                        .replace("%type%", getDisplayName())
                );
            }
            return crystals;
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
        return 0;
    }

    /**
     * Deposits the given amount of this item to the Player's bank.
     *
     * @param player The player who wants to deposit
     * @param amount The amount which should be deposited
     */
    @Override
    public double deposit(Player player, Number amount) {
        Optional<Island> islandOptional = IridiumSkyblock.getInstance().getUserManager().getUser(player).getIsland();
        if (!islandOptional.isPresent()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return 0;
        }

        int remainingItemAmount = amount.intValue();
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
        }

        if (depositAmount == 0) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().insufficientFundsToDeposit
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
                    .replace("%type%", getDisplayName())
            );
            return 0;
        }

        IslandBank islandBank = IridiumSkyblock.getInstance().getIslandManager().getIslandBank(islandOptional.get(), this);
        islandBank.setNumber(islandBank.getNumber() + depositAmount);
        player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().bankDeposited
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
                .replace("%amount%", String.valueOf(depositAmount))
                .replace("%type%", getDisplayName())
        );
        return depositAmount;
    }

    /**
     * Returns the string representation of the value of this item.
     *
     * @param number The number which should be formatted
     * @return The string representation of the provided number for this item
     */
    @Override
    public String toString(Number number) {
        return String.valueOf(number.intValue());
    }

}
