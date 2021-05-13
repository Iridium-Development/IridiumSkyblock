package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.bank.TransactionType;
import com.iridium.iridiumskyblock.utils.StringUtils;
import com.j256.ormlite.field.DatabaseField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class BankTransaction extends IslandData{

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "bank_item")
    private String bankItem;

    @DatabaseField(columnName = "amount")
    @Setter
    private double amount;

    @DatabaseField(columnName = "player")
    private UUID player;

    @DatabaseField(columnName = "date")
    private long date;

    @DatabaseField(columnName = "type")
    private TransactionType type;

    @DatabaseField(columnName = "purchase")
    private String purchase;


    /**
     * The default constructor.
     *
     * @param island   The Island of this Island bank
     * @param bankItem The bank item in the Island bank
     * @param amount   The amount of this currency in the Island bank
     * @param date     The date of transaction created at
     * @param player   The uuid of player
     * @param type     The type of transaction
     */
    public BankTransaction(@NotNull Island island, @NotNull String bankItem, double amount, long date, UUID player, TransactionType type) {
        super(island);
        this.bankItem = StringUtils.capitalize(bankItem);
        this.amount = amount;
        this.date = date;
        this.player = player;
        this.type = type;
    }

    /**
     * The default constructor.
     *
     * @param island   The Island of this Island bank
     * @param bankItem The bank item in the Island bank
     * @param amount   The amount of this currency in the Island bank
     * @param date     The date of transaction created at
     * @param player   The uuid of player
     * @param type     The type of transaction
     * @param purchase The purchased for what(upgrade booster shop etc.)
     */
    public BankTransaction(@NotNull Island island, @NotNull String bankItem, double amount, long date, UUID player, TransactionType type, String purchase) {
        super(island);
        this.bankItem = StringUtils.capitalize(bankItem);
        this.amount = amount;
        this.date = date;
        this.player = player;
        this.type = type;
        this.purchase = ChatColor.stripColor(purchase);
    }
}
