package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.SortedList;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.managers.DatabaseKey;
import com.iridium.iridiumteams.database.DatabaseObject;
import com.iridium.iridiumteams.database.TeamData;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ForeignIslandTableManager<Key, Value extends TeamData> extends TableManager<Key, Value, Integer> {

    public ForeignIslandTableManager(DatabaseKey<Key, Value> databaseKey, ConnectionSource connectionSource, Class<Value> clazz) throws SQLException {
        super(databaseKey, connectionSource, clazz);
    }

    public List<Value> getEntries(@NotNull Island island) {
        return super.getEntries(value -> value.getTeamID() == island.getId());
    }
}
