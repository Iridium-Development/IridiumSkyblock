package com.iridium.iridiumskyblock.bank;

import com.iridium.iridiumskyblock.Item;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

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
     * @param item          The Item which represents this bank item in the {@link com.iridium.iridiumskyblock.gui.BankGUI}
     */
    public CrystalsBankItem(double defaultAmount, Item item) {
        super("crystals", defaultAmount, true, item);
    }

    /**
     * Withdraws the given amount of this item from the Player's bank.
     *
     * @param player The player who wants to withdraw
     * @param amount The amount which should be withdrawn
     */
    @Override
    public double withdraw(Player player, Number amount) {
        // TODO cba to do this atm
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
        // TODO cba to do this atm
        return 0;
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
