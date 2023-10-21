package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumcore.utils.ItemStackUtils;
import com.iridium.iridiumteams.database.DatabaseObject;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@DatabaseTable(tableName = "lost_items")
public class LostItems extends DatabaseObject {
    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false, unique = true)
    private int id;

    @DatabaseField(columnName = "uuid", canBeNull = false)
    private @NotNull UUID uuid;

    @DatabaseField(columnName = "time")
    private LocalDateTime time;

    @DatabaseField(columnName = "items", dataType = DataType.SERIALIZABLE)
    private String[] items;

    public void setItems(ItemStack[] items) {
        this.items = Arrays.stream(items)
                .map(ItemStackUtils::serialize)
                .toArray(String[]::new);
    }

    public ItemStack[] getItems() {
        return Arrays.stream(items)
                .map(ItemStackUtils::deserialize)
                .toArray(ItemStack[]::new);
    }

    public LostItems(@NotNull UUID uuid, ItemStack[] items) {
        this.uuid = uuid;
        this.time = LocalDateTime.now();
        setItems(items);
    }
}
