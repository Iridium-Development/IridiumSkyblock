package com.iridium.iridiumskyblock.bankitems;

import com.iridium.iridiumskyblock.BankItem;
import com.iridium.iridiumskyblock.Item;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

@NoArgsConstructor
public class CrystalsBankItem extends BankItem {

    public CrystalsBankItem(double defaultAmount, Item item) {
        super("crystals", defaultAmount, true, item);
    }

    @Override
    public void withdraw(Player player, Number amount) {
        //TODO cba to do this atm
    }

    @Override
    public void deposit(Player player, Number amount) {
        //TODO cba to do this atm
    }

    @Override
    public String toString(Number number) {
        return String.valueOf(number.intValue());
    }
}
