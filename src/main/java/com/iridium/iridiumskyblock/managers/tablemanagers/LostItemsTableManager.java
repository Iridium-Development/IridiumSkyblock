package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.database.LostItems;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class LostItemsTableManager extends TableManager<LostItems, Integer> {
    public LostItemsTableManager(ConnectionSource connectionSource, Class<LostItems> clazz, Comparator<LostItems> comparator) throws SQLException {
        super(connectionSource, clazz, comparator);
        sort();
    }

    public void sort() {
        getEntries().sort(Comparator.comparing(LostItems::getUuid));
    }

    public List<LostItems> getLostItems(UUID uuid) {
        return getEntries().stream()
                .filter(item -> item.getUuid().equals(uuid))
                .sorted(Comparator.comparing(LostItems::getTime).reversed())
                .collect(Collectors.toList());
    }
}