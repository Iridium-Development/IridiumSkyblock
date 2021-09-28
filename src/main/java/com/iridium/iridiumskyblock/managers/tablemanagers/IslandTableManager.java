package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.database.Island;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

/**
 * Used for handling Crud operations on a table + handling cache
 */
public class IslandTableManager extends TableManager<Island, Integer> {

    public IslandTableManager(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Island.class, Comparator.comparing(Island::getId));
        sort();
    }

    @Override
    public void addEntry(Island island) {
        int index = Collections.binarySearch(getEntries(), island, Comparator.comparing(Island::getId));
        getEntries().add(index < 0 ? -(index + 1) : index, island);
    }

    /**
     * Sort the list of entries by island id
     */
    private void sort() {
        getEntries().sort(Comparator.comparing(Island::getId));
    }

    public Optional<Island> getIsland(int id) {
        int index = Collections.binarySearch(getEntries(), new Island(id), Comparator.comparing(Island::getId));
        if (index < 0) return Optional.empty();
        return Optional.of(getEntries().get(index));
    }
}
