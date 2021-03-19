package com.iridium.iridiumskyblock.bankitems;

import com.iridium.iridiumskyblock.BankItem;
import com.iridium.iridiumskyblock.Item;
import org.bukkit.entity.Player;

public class MoneyBankItem extends BankItem {

    public MoneyBankItem(double defaultAmount, Item item) {
        super("money", defaultAmount, true, item);
    }

    @Override
    public void withdraw(Player player, Number amount) {

    }

    @Override
    public void deposit(Player player, Number amount) {

    }

    @Override
    public String toString(Number number) {
        return String.valueOf(number.doubleValue());
    }
}
