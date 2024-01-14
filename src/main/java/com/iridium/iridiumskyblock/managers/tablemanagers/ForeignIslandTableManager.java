package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.databaseadapter.DatabaseAdapter;
import com.iridium.iridiumskyblock.managers.DatabaseKey;
import com.iridium.iridiumteams.database.TeamData;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public class ForeignIslandTableManager<Key, Value extends TeamData> extends TableManager<Key, Value, Integer> {

    public ForeignIslandTableManager(DatabaseKey<Key, Value> databaseKey, DatabaseAdapter<Value, Integer> databaseAdapter) throws SQLException {
        super(databaseKey, databaseAdapter);
    }

    public List<Value> getEntries(@NotNull Island island) {
        return super.getEntries(value -> value.getTeamID() == island.getId());
    }
}
