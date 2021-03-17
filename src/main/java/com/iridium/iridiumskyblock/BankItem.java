package com.iridium.iridiumskyblock;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public abstract class BankItem {
    private String name;
    private Number defaultAmount;
    private boolean enabled;
    private Item item;

    public abstract boolean withdraw(Player player, Number amount);

    public abstract boolean deposit(Player player, Number amount);
}
