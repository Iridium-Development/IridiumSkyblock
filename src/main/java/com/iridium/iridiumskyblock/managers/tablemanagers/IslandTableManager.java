package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.SortedList;
import com.iridium.iridiumskyblock.database.Island;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class IslandTableManager extends TableManager<Island, Integer> {
    private final SortedList<Island> islandEntries;

    public IslandTableManager(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Island.class, Comparator.comparing(Island::getId));
        this.islandEntries = new SortedList<>(Comparator.comparing(Island::getName, String.CASE_INSENSITIVE_ORDER));
        this.islandEntries.addAll(getEntries());
        sort();
    }

    @Override
    public void addEntry(Island island) {
        super.addEntry(island);
        islandEntries.add(island);
    }

    @Override
    public CompletableFuture<Void> delete(Island island) {
        islandEntries.remove(island);
        return super.delete(island);
    }

    @Override
    public CompletableFuture<Void> delete(Collection<Island> islands) {
        islandEntries.removeAll(islands);
        return super.delete(islands);
    }

    /**
     * Sort the list of entries by UUID
     */
    public void sort() {
        getEntries().sort(Comparator.comparing(Island::getId));
        islandEntries.sort(Comparator.comparing(Island::getName, String.CASE_INSENSITIVE_ORDER));
    }

    public Optional<Island> getIsland(int id) {
        int index = Collections.binarySearch(getEntries(), new Island(id), Comparator.comparing(Island::getId));
        if (index < 0) return Optional.empty();
        return Optional.of(getEntries().get(index));
    }

    public Optional<Island> getIsland(String name) {
        int index = Collections.binarySearch(islandEntries, new Island(name), Comparator.comparing(Island::getName, String.CASE_INSENSITIVE_ORDER));
        if (index < 0) return Optional.empty();
        return Optional.of(islandEntries.get(index));
    }
}
