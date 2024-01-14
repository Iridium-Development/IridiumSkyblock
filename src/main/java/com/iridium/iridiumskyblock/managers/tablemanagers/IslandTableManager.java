package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.databaseadapter.DatabaseAdapter;
import com.iridium.iridiumteams.database.Team;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Optional;

public class IslandTableManager extends TableManager<Integer, Island, Integer> {

    public IslandTableManager(DatabaseAdapter<Island, Integer> databaseAdapter) throws SQLException {
        super(Team::getId, databaseAdapter);
    }

    public Optional<Island> getIsland(int id) {
        return super.getEntry(id);
    }

    public Optional<Island> getIsland(String name) {
        return super.getEntry(island -> island.getName().equalsIgnoreCase(name));
    }
}
