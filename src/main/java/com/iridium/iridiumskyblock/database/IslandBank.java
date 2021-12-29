package com.iridium.iridiumskyblock.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a currency in the Island bank.
 */
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_bank")
public class IslandBank extends IslandData {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false, uniqueCombo = true)
    private int id;

    @DatabaseField(columnName = "bank_item", uniqueCombo = true)
    private String bankItem;

    @DatabaseField(columnName = "number")
    private double number;

    /**
     * The default constructor.
     *
     * @param island   The Island of this Island bank
     * @param bankItem The bank item in the Island bank
     * @param number   The amount of this currency in the Island bank
     */
    public IslandBank(@NotNull Island island, @NotNull String bankItem, double number) {
        super(island);
        this.bankItem = bankItem;
        this.number = number;
    }

    @Override
    public @NotNull String getUniqueKey() {
        return bankItem + "-" + getIslandId();
    }

    public void setNumber(double number) {
        this.number = number;
        setChanged(true);
    }
}
