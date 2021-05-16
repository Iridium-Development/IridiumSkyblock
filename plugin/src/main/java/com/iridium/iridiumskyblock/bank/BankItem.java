package com.iridium.iridiumskyblock.bank;

import com.iridium.iridiumskyblock.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

/**
 * Represents a type of currency in the bank.
 * Inherit from this class in order to define a new item for the bank.
 */
@NoArgsConstructor
@Getter
public abstract class BankItem {

    private String name;
    private double defaultAmount;
    private boolean enabled;
    private Item item;

    /**
     * The default constructor.
     *
     * @param name          The name of this
     * @param defaultAmount The default withdrawal amount of this item
     * @param enabled       Whether or not this item is usable
     * @param item          The Item which represents this bank item in the {@link com.iridium.iridiumskyblock.gui.BankGUI}
     */
    public BankItem(String name, double defaultAmount, boolean enabled, Item item) {
        this.name = name;
        this.defaultAmount = defaultAmount;
        this.enabled = enabled;
        this.item = item;
    }

    /**
     * Withdraws the given amount of this item from the Player's bank.
     *
     * @param player The player who wants to withdraw
     * @param amount The amount which should be withdrawn
     * @return returns the resulting amount withdrawn
     */
    public abstract double withdraw(Player player, Number amount);

    /**
     * Deposits the given amount of this item to the Player's bank.
     *
     * @param player The player who wants to deposit
     * @param amount The amount which should be deposited
     * @return returns the resulting amount deposited
     */
    public abstract double deposit(Player player, Number amount);

    /**
     * Returns the string representation of the value of this item.
     *
     * @param number The number which should be formatted
     * @return The string representation of the provided number for this item
     */
    public abstract String toString(Number number);

}
