package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.database.LostItems;
import com.iridium.iridiumskyblock.databaseadapter.DatabaseAdapter;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class LostItemsTableManager extends TableManager<UUID, LostItems, Integer> {
    public LostItemsTableManager(DatabaseAdapter<LostItems, Integer> databaseAdapter) throws SQLException {
        super(LostItems::getUuid, databaseAdapter);
    }
}