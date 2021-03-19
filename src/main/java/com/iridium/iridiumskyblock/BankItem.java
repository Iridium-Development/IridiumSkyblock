package com.iridium.iridiumskyblock;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public abstract class BankItem {
    private final String name;
    private final double defaultAmount;
    private final boolean enabled;
    private final Item item;

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
