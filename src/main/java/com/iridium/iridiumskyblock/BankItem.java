package com.iridium.iridiumskyblock;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

@NoArgsConstructor
@Getter
public abstract class BankItem {

    private String name;
    private double defaultAmount;
    private boolean enabled;
    private Item item;

    public BankItem(String name, double defaultAmount, boolean enabled, Item item) {
        this.name = name;
        this.defaultAmount = defaultAmount;
        this.enabled = enabled;
        this.item = item;
    }

    public abstract void withdraw(Player player, Number amount);

    public abstract void deposit(Player player, Number amount);

    public abstract String toString(Number number);

}
