package com.iridium.iridiumskyblock.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "island_bank")
public class IslandBank {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "island_id")
    private int island;

    @DatabaseField(columnName = "bank_item")
    private String bankItem;

    @DatabaseField(columnName = "number")
    @Setter
    private double number;

    public IslandBank(@NotNull Island island, @NotNull String bankItem, double number) {
        this.island = island.getId();
        this.bankItem = bankItem;
        this.number = number;
    }

}
