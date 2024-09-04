package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.database.LostItems;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.UUID;

public class LostItemsTableManager extends TableManager<UUID, LostItems, Integer> {
    public LostItemsTableManager(ConnectionSource connectionSource, Class<LostItems> clazz) throws SQLException {
        super(LostItems::getUuid, connectionSource, clazz);
    }
}